package base;

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

public class TestDriver {
    private static SupportedBrowsers type;
    //WebDriver needed due to issue https://github.com/codeborne/selenide/issues/676 not resolved
    private static WebDriver webDriver = null;

    private void driverInitialization() {
        try {
            webDriver = new RemoteWebDriver(new URL(PropertyReader.getGlobalProperty("HUB_URL")), getOptions());
        } catch (WebDriverException | IOException e) {
            System.out.println("Error starting browser!");
            e.printStackTrace();
        }
    }

    public void terminateDriver() {
        System.out.println("Terminating driver.");
        if (!isDriverOpened()) {
            webDriver.close();
            webDriver.quit();
            webDriver = null;
        }
    }

    private boolean isDriverOpened() {
        return webDriver != null;
    }

    public WebDriver getDriver(){
        if (!isDriverOpened()) {
            driverInitialization();
        }
        return webDriver;
    }

    private void detectBrowserType() {
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

    private MutableCapabilities getOptions() {
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

    private String getBrowserName() {
        return PropertyReader.getGlobalProperty("browser").toLowerCase();
    }
}
