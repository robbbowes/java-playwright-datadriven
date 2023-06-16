package core;

import com.aventstack.extentreports.Status;
import com.microsoft.playwright.*;
import listeners.ExtentReportListener;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.log4testng.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Listeners(value = ExtentReportListener.class)
public class GlobalTestBase {

    private static final Properties OR = new Properties();
    private static final ThreadLocal<Playwright> PW = new ThreadLocal<>();
    private static final ThreadLocal<Browser> BR = new ThreadLocal<>();
    private static final ThreadLocal<Page> PG = new ThreadLocal<>();
    private final Logger logger = Logger.getLogger(this.getClass());

    public static Playwright getPlaywright() {
        return PW.get();
    }

    public static Browser getBrowser() {
        return BR.get();
    }

    public static Page getPage() {
        return PG.get();
    }

    @BeforeSuite
    public void setup() {
        logger.info("Test execution started!!");
        try {
            FileInputStream fileInputStream = new FileInputStream("./src/test/resources/properties/OR.properties");
            OR.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("OR properties loaded");
    }

    public boolean isElementPresent(String locatorKey) {
        boolean visible;
        try {
            getPage().waitForSelector(OR.getProperty(locatorKey), new Page.WaitForSelectorOptions().setTimeout(2000));
            String message = "Found element with locator key '" + locatorKey + "'";
            ExtentReportListener.getTest().log(Status.INFO, message);
            logger.info(message);
            visible = true;
        } catch (Throwable t) {
            String message = "Could not find element with locator key '" + locatorKey + "'";
            logger.info(message, t);
            ExtentReportListener.getTest().log(Status.INFO, message);
            visible = false;
        }
        return visible;
    }

    public void click(String locatorKey) {
        try {
            getPage().locator(OR.getProperty(locatorKey)).click();
            ExtentReportListener.getTest().log(Status.INFO, "Clicking element with locator key '" + locatorKey + "'");
        } catch (Throwable t) {
            String message = "Error when clicking on element with locator key '" + locatorKey + "'";
            logger.error(message, t);
            ExtentReportListener.getTest().log(Status.FAIL, message);
            Assert.fail(t.getMessage());
        }
    }

    public void type(String locatorKey, String value) {
        try {
            getPage().locator(OR.getProperty(locatorKey)).fill(value);
            ExtentReportListener.getTest().log(Status.INFO, "Typing in element with locator key '" + locatorKey + "'");
        } catch (Throwable t) {
            String message = "Error when typing to element with locator key '" + locatorKey + "'";
            logger.error(message, t);
            ExtentReportListener.getTest().log(Status.FAIL, message);
            Assert.fail(t.getMessage());
        }
    }

    public void navigate(String url) {
        final String message = "Navigating to '" + url + "'";
        Page page = getBrowser().newPage();
        PG.set(page);
        getPage().navigate(url);
        ExtentReportListener.getTest().log(Status.INFO, message);
        logger.info(message);
    }

    public Browser getBrowser(String browserName) {
        final String message = "Launching " + browserName;
        logger.info(message);
        ExtentReportListener.getTest().log(Status.INFO, message);

        Playwright playwright = Playwright.create();
        PW.set(playwright);

        Browser browser;
        switch (browserName) {
            case "chrome" -> browser = getPlaywright().chromium().launch(
                    new BrowserType.LaunchOptions().setChannel("chromium").setHeadless(false));

            case "headless" -> browser = getPlaywright().chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true));

            case "firefox" -> browser = getPlaywright().firefox().launch(
                    new BrowserType.LaunchOptions().setChannel("firefox").setHeadless(false));

            case "webkit" -> browser = getPlaywright().webkit().launch(
                    new BrowserType.LaunchOptions().setHeadless(true));

            default -> throw new IllegalStateException("Unexpected value: " + browserName);
        }
        BR.set(browser);
        return getBrowser();
    }

    @AfterMethod
    public void quit() {
        getBrowser().close();
        getPage().close();
        getPlaywright().close();
    }

}
