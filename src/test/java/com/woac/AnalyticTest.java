package com.woac;

import com.google.common.base.Function;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.HarEntry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This is basic analytic test using Browsermob-proxy and Selenium WebDriver.
 * All the provided code is fairly basic, but you'll got the basic idea of such type of testing.
 * Feel free to amend.
 */
public class AnalyticTest {
    private BrowserMobProxy proxy;
    private WebDriver driver;

    @Before
    public void setUp() {
        this.proxy = startUpProxy();
        this.driver = configureWebDriverWithProxy(proxy);
    }

    /**
     * Initialise the basic proxy server on random port.
     * @return {@code BrowserMobProxy}
     */
    private BrowserMobProxy startUpProxy() {
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.start();
        return proxy;
    }

    /**
     * Creates web driver instance to proxy support.
     * @param proxy - specifies configured proxy object
     * @return {@code WebDriver}
     */
    private WebDriver configureWebDriverWithProxy(BrowserMobProxy proxy) {
        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        return new ChromeDriver(capabilities);
    }

    @After
    public void tearDown() {
        this.driver.close();
        this.proxy.stop();
    }

    /**
     * Test case. Steps
     * 1. Open google home page.
     * 2. Enter some random search term.
     * 3. Verify that request is being send to server with "q=search_term" parameter.
     */
    @Test
    public void firstAnalyticTest() {
        String searchTerm = "automation" + UUID.randomUUID().toString(); // Lets add some randomness to our search
        proxy.newHar(GooglePage.NAME); // Start proxy to record har file that contains the REST logs
        new GooglePage(driver)
                .navigate() // Open google page
                .searchFor(searchTerm); // Search for testing term

        HarEntry entry = waitForRequestToAppear(searchTerm); // Wait for HAR log to contain expected token, or analytic string

        String actualRequestUrl = entry.getRequest().getUrl(),
                expectedPartOfUrl = String.format("q=%s", searchTerm); // Verify that search parameter is as expected, q="automation"
        // Note: Go further and describe your search term not in terms of String, but in terms of your DSL that will suits your business needs

        Assert.assertTrue(String.format("Beacon was not as expected. Actual: %s, Expected: %s", actualRequestUrl, searchTerm),
                actualRequestUrl.contains(expectedPartOfUrl));
    }

    /**
     * Waits for specified token to appear in HAR log.
     * @param token - specifies token / part of url to appear
     * @return {@code HarEntry} - returns har entry that contains expected token
     */
    private HarEntry waitForRequestToAppear(String token) {
        Function<BrowserMobProxy, HarEntry> searchFunc = prxy -> // This is about wait function
                prxy.getHar().getLog().getEntries().stream()
                        .filter(entry ->
                                entry.getRequest().getUrl().contains(token)) // if some entry url contains expected token
                        .findFirst().orElse(null); // return it or otherwise just return null, that will foster to wait longer

        System.out.println(String.format("Waiting for request [%s] to appear in logs", token));
        return new FluentWait<>(proxy)
                .withTimeout(15L, TimeUnit.SECONDS)
                .withMessage(String.format("Expected token [%s] did not appear in HarLog", token))
                .until(searchFunc);
    }
}
