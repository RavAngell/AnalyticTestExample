# How to do basic analytics testing?
This is basic web analytics test project to provide an idea about how perform such type of testing. 
Fairly often you as QA engineer are being asked to verify that after clicking button 'X' some request is being sent 
to server, some event or "beacon" is fired. And you just open you traffic sniffer (DevTools) and trying to locate and 
compare event with expected one. But it is pretty easy to automate and save you lifetime:) ! 

# Test case
1. Open google home page.
2. Enter some random "search_term".
3. Verify that request is being send to server with "q=search_term" parameter.

# Tools / Technology
- [Java](http://openjdk.java.net/)
- [Selenium WebDriver](https://github.com/SeleniumHQ/selenium)
- [BrowserMob-proxy](https://github.com/lightbody/browsermob-proxy/)
- [AssertJ](http://joel-costigliola.github.io/assertj/)
- [Maven](https://maven.apache.org/)
- [JUnit](http://junit.org/)
- [Page Object model](https://code.google.com/p/selenium/wiki/PageObjects)

# How it works?
This is all about simple MITM proxy configuration that sniffs browser REST requests. Then you code just parses logs, 
finds and compares expected event.

# Before running
Before running it on your local machine, make sure that ChromeDriver is installed or use your one.

# What is the output if it fails?
Something like:
```
java.lang.AssertionError: [Search parameter in logs was not as expected] 
Expecting:
 <"https://www.google.com:443/s?&q=automation3a80b6e8-72f0-4f59-a6ff-c7f89008678e">
to contain:
 <"q=automation3a80b6e8-72f0-4f59-a6ff-c7f89008678eABRACADABRA"> 
 ```
 
# What the next?
With this approach you can test and do a lot of interesting things besides web analytics testing. For example, you can 
download file after pressing on 'Download' button if href attribute is not present in the DOM, but being retrieved via
JavaScript.
 

