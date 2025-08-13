package com.saucedemo.driver;

import com.saucedemo.util.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * DriverFactory is a singleton-style class that manages WebDriver instances across the project.
 * It ensures a single driver per test thread using ThreadLocal and supports multiple browsers.
 *
 * <p>Supported browsers: Chrome (default), Firefox, Edge, Safari</p>
 */
public final class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private DriverFactory() {
        throw new UnsupportedOperationException("DriverFactory is a utility class and should not be instantiated.");
    }

    /**
     * Enum representing supported browser types.
     */
    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI;

        /**
         * Returns a BrowserType enum from a given string input.
         * Defaults to CHROME if the input is null, empty, or unrecognised.
         *
         * @param name the browser name (e.g., "chrome", "firefox")
         * @return BrowserType corresponding to the name, or CHROME as default
         */
        public static BrowserType from(String name) {
            if (name == null || name.trim().isEmpty()) {
                return CHROME;
            }
            try {
                return BrowserType.valueOf(name.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                System.err.println("[WARN] Unsupported browser: " + name + ". Falling back to CHROME.");
                return CHROME;
            }
        }
    }

    /**
     * Gets the WebDriver instance for the current thread.
     * Initializes a new WebDriver if not already set.
     *
     * @return WebDriver - A thread-safe WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            String browserName = ConfigReader.getConfig("browser");
            BrowserType browser = BrowserType.from(browserName);
            WebDriver driver = initDriver(browser);

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driverThreadLocal.set(driver);

            logger.info("WebDriver for {} initialized successfully.", browser);
        }
        return driverThreadLocal.get();
    }

    /**
     * Initializes the WebDriver instance based on the given browser type.
     *
     * @param browser BrowserType - browser type (CHROME, FIREFOX, EDGE, SAFARI)
     * @return WebDriver - A WebDriver instance for the specified browser
     */
    private static WebDriver initDriver(BrowserType browser) {
        switch (browser) {
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();

            case EDGE:
                WebDriverManager.edgedriver().setup();
                return new EdgeDriver();

            case SAFARI:
                if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
                    throw new UnsupportedOperationException("Safari is only supported on macOS");
                }
                return new SafariDriver();

            case CHROME:
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                return new ChromeDriver(options);
        }
    }

    /**
     * Quits and cleans up the WebDriver instance for the current thread.
     * Safely cleans up the browser instance after test execution.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Quitting WebDriver instance.");
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}