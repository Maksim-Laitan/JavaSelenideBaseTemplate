package base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class BaseUiTestClass {
    TestDriver testDriver = new TestDriver();

    @BeforeEach
    void setup() {

        /*
        Beginning of workaround due to issue https://github.com/codeborne/selenide/issues/676
         */

        setWebDriver(testDriver.getDriver());
        /*
        End of workaround
         */

        /*
        Exist issue with set capabilities by default method.
        Issue link:  https://github.com/codeborne/selenide/issues/676

        Configuration.browser = getBrowserName();
        Configuration.remote = DataReader.getPropertyValueByKey("HUB_URL");

        MutableCapabilities options = getOptions();
        Configuration.browserCapabilities = new DesiredCapabilities();
        Configuration.browserCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
         */
    }

    /*
    After each module is a part of workaround
     */
    @AfterEach
    void terminateDriver() {
        testDriver.terminateDriver();
    }

    /*
    Temporary solution until issue https://github.com/codeborne/selenide/issues/676 will be resolved
     */
    protected void open(String url) {
        testDriver.getDriver().navigate().to(url);
    }

}
