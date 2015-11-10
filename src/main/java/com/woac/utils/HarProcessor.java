package com.woac.utils;

import com.google.common.base.Function;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.HarEntry;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.concurrent.TimeUnit;

/**
 * This is a set of useful utils to work with proxy logs.
 */
public class HarProcessor {
    private final BrowserMobProxy proxy;

    public HarProcessor(BrowserMobProxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Waits for specified token to appear in HAR log.
     * @param token - specifies token / part of url to appear
     * @return {@code HarEntry} - returns har entry that contains expected token
     */
    public final HarEntry waitForRequestToAppear(String token) {
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
