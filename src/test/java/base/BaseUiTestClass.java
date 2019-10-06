package base;

import com.codeborne.selenide.Configuration;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.PropertyReader;

import java.io.IOException;
import java.net.URL;

import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

public class BaseUiTestClass {

    private static SupportedBrowsers type;
    //WebDriver needed due to issue https://github.com/codeborne/selenide/issues/676 not resolved
    private static WebDriver webDriver = null;

    @BeforeEach
    void setup() {

        /*
        Beginning of workaround due to issue https://github.com/codeborne/selenide/issues/676
         */
        try {
            webDriver = new RemoteWebDriver(new URL(PropertyReader.getGlobalProperty("HUB_URL")), getOptions());
        } catch (WebDriverException | IOException e) {
            System.out.println("Error starting browser!");
            e.printStackTrace();
        }
        setWebDriver(webDriver);
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
        System.out.println("Terminating driver.");
        if (webDriver != null) {
            webDriver.close();
            webDriver.quit();
            webDriver = null;
        }
    }

    private static void detectBrowserType() {
        switch (getBrowserName()) {
            case "chrome":
                type = SupportedBrowsers.CHROME;
                break;
            case "firefox":
                type = SupportedBrowsers.FIREFOX;
                break;
            default:
                throw new NotFoundException("Browser not found: " + getBrowserName());
        }
    }

    private static void setBrowserOptions() {
        detectBrowserType();
        switch (type) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                Configuration.browser = getBrowserName();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("disable-infobars");
                chromeOptions.addArguments("--log-level=3");
                chromeOptions.addArguments("--silent");
                chromeOptions.addArguments("--incognito");
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                Configuration.browser = getBrowserName();
                break;
            default:
                throw new NotFoundException("Browser options not found: " + type);
        }
    }

    private static MutableCapabilities getOptions() {
        detectBrowserType();
        switch (type) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("disable-infobars");
                chromeOptions.addArguments("--log-level=3");
                chromeOptions.addArguments("--silent");
                chromeOptions.addArguments("--incognito");
                return chromeOptions;
            case FIREFOX:
                return new FirefoxOptions();
            default:
                throw new NotFoundException("Browser options not found: " + type);
        }
    }

    private static String getBrowserName() {
        return PropertyReader.getGlobalProperty("browser").toLowerCase();
    }

    /*
    Temporary solution until issue https://github.com/codeborne/selenide/issues/676 will be resolved
     */
    protected void open(String url) {
        webDriver.navigate().to(url);
    }

}
