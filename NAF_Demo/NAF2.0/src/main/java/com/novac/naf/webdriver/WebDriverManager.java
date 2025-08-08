package com.novac.naf.webdriver;

import com.novac.naf.config.ConfigLoader;
import com.novac.naf.reporting.ReportManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Manages WebDriver instances and provides browser automation capabilities
 * Enhanced with singleton session management for batch execution optimization
 */
public class WebDriverManager {
    private static final Logger logger = LoggerFactory.getLogger(WebDriverManager.class);
    private static WebDriver driver;
    private static ConfigLoader configLoader;
    
    // Batch execution session management
    private static boolean isBatchMode = false;
    private static boolean sessionInitialized = false;
    private static long sessionStartTime = 0;
    private static int testCaseExecutionCount = 0;
    
    /**
     * Default constructor
     */
    public WebDriverManager() {
        // Default constructor
    }
    
    /**
     * Initializes WebDriverManager with configuration
     * 
     * @param config The configuration loader
     */
    public static void initialize(ConfigLoader config) {
        configLoader = config;
    }
    
    /**
     * Starts a batch execution session - creates a single browser instance
     * that will be reused across multiple test cases
     */
    public static void startBatchSession() {
        if (sessionInitialized) {
            logger.warn("Batch session already started. Use endBatchSession() before starting a new one.");
            return;
        }
        
        logger.info("Starting batch execution session...");
        isBatchMode = true;
        sessionStartTime = System.currentTimeMillis();
        testCaseExecutionCount = 0;
        
        // Initialize the driver for batch mode
        initializeDriver();
        sessionInitialized = true;
        
        logger.info("Batch session started successfully. Browser instance ready for reuse across test cases.");
    }
    
    /**
     * Ends the batch execution session and closes the browser
     */
    public static void endBatchSession() {
        if (!sessionInitialized) {
            logger.warn("No batch session to end.");
            return;
        }
        
        logger.info("Ending batch execution session...");
        
        // Calculate session duration
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        logger.info("Batch session executed {} test cases in {} ms", testCaseExecutionCount, sessionDuration);
        
        // Quit the driver
        quitDriver();
        
        // Reset session state
        isBatchMode = false;
        sessionInitialized = false;
        testCaseExecutionCount = 0;
        sessionStartTime = 0;
        
        logger.info("Batch session ended successfully.");
    }
    
    /**
     * Prepares the browser for the next test case in batch mode
     * Performs cleanup without closing the browser session
     * 
     * @param testCaseId The ID of the test case about to be executed
     */
    public static void prepareForNextTestCase(String testCaseId) {
        if (!isBatchMode || !sessionInitialized) {
            logger.debug("Not in batch mode, skipping test case preparation");
            return;
        }
        
        testCaseExecutionCount++;
        logger.info("Preparing browser for test case: {} (Test #{} in batch)", testCaseId, testCaseExecutionCount);
        
        try {
            if (driver != null) {
                // Clear browser state for clean test execution
                clearBrowserState();
                
                // Wait a moment for cleanup to complete
                Thread.sleep(500);
                
                logger.debug("Browser prepared for test case: {}", testCaseId);
            }
        } catch (Exception e) {
            logger.error("Error preparing browser for test case {}: {}", testCaseId, e.getMessage(), e);
            
            // If cleanup fails, try to recover by reinitializing the driver
            try {
                logger.warn("Attempting to recover browser session...");
                quitDriver();
                initializeDriver();
                logger.info("Browser session recovered successfully");
            } catch (Exception recoveryException) {
                logger.error("Failed to recover browser session: {}", recoveryException.getMessage(), recoveryException);
                throw new RuntimeException("Browser session recovery failed", recoveryException);
            }
        }
    }
    
    /**
     * Clears browser state between test cases while keeping the session alive
     */
    private static void clearBrowserState() {
        try {
            if (driver != null) {
                // Clear cookies
                driver.manage().deleteAllCookies();
                
                // Clear local storage and session storage via JavaScript
                if (driver instanceof JavascriptExecutor) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("window.localStorage.clear();");
                    js.executeScript("window.sessionStorage.clear();");
                }
                
                logger.debug("Browser state cleared successfully");
            }
        } catch (Exception e) {
            logger.warn("Error clearing browser state: {}", e.getMessage());
            // Continue anyway - this is not critical
        }
    }
    
    /**
     * Gets the current WebDriver instance, creating one if it doesn't exist
     * 
     * @return The WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }
    
    /**
     * Initializes the WebDriver based on configuration
     */
    private static void initializeDriver() {
        String browser = configLoader != null ? configLoader.getBrowser() : "Chrome";
        boolean headless = configLoader != null && configLoader.isHeadlessMode();
        
        logger.info("Initializing {} WebDriver, headless: {}, batch mode: {}", browser, headless, isBatchMode);
        
        try {
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) {
                        chromeOptions.addArguments("--headless=new");
                    }
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    
                    // Additional options for batch execution stability
                    if (isBatchMode) {
                        chromeOptions.addArguments("--disable-extensions");
                        chromeOptions.addArguments("--disable-plugins");
                        chromeOptions.addArguments("--disable-images");
                        chromeOptions.addArguments("--disable-javascript");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                    }
                    
                    driver = new ChromeDriver(chromeOptions);
                    break;
                    
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) {
                        firefoxOptions.addArguments("-headless");
                    }
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                    
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) {
                        edgeOptions.addArguments("--headless");
                    }
                    driver = new EdgeDriver(edgeOptions);
                    break;
                    
                case "safari":
                    driver = new SafariDriver();
                    break;
                    
                default:
                    logger.warn("Unknown browser type: {}, defaulting to Chrome", browser);
                    driver = new ChromeDriver();
            }
            
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().window().maximize();
            
            logger.info("WebDriver initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize WebDriver", e);
        }
    }
    
    /**
     * Quits the WebDriver instance
     */
    public static void quitDriver() {
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully");
            } catch (Exception e) {
                logger.error("Error quitting WebDriver: {}", e.getMessage(), e);
            } finally {
                driver = null;
            }
        }
    }
    
    /**
     * Closes the current browser window but keeps the WebDriver session active
     */
    public static void closeDriver() {
        if (driver != null) {
            try {
                driver.close();
                logger.info("WebDriver window closed successfully");
            } catch (Exception e) {
                logger.error("Error closing WebDriver window: {}", e.getMessage(), e);
            }
        }
    }
    
    /**
     * Takes a screenshot and saves it to the screenshots folder
     * 
     * @param testCaseId The test case ID to include in the filename
     * @param stepNumber The step number to include in the filename
     * @return The path to the saved screenshot file
     */
    public static String takeScreenshot(String testCaseId, int stepNumber) {
        if (driver == null) {
            logger.error("Cannot take screenshot: WebDriver is not initialized");
            return null;
        }
        
        try {
            // Ensure page has loaded before taking screenshot
            waitForPageLoad(driver, 30);
            
            // Get report folder path
            String reportFolder = ReportManager.getCurrentReportFolder();
            String screenshotsDir = reportFolder + "/Screenshots";
            
            // Create directory if it doesn't exist
            File screenshotDir = new File(screenshotsDir);
            if (!screenshotDir.exists()) {
                boolean created = screenshotDir.mkdirs();
                if (created) {
                    logger.debug("Created screenshots directory: {}", screenshotsDir);
                } else {
                    logger.warn("Failed to create screenshots directory: {}", screenshotsDir);
                }
            }
            
            // Format timestamp for filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testCaseId + "_Step" + stepNumber + "_" + timestamp + ".png";
            String filePath = screenshotsDir + "/" + fileName;
            
            logger.debug("Taking screenshot: {}", filePath);
            
            // Take screenshot using TakesScreenshot interface
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File targetFile = new File(filePath);
            
            // Copy the screenshot file to the target location
            Files.copy(screenshotFile.toPath(), targetFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            
            // Verify the screenshot was saved
            if (targetFile.exists() && targetFile.length() > 0) {
                logger.info("Screenshot saved successfully: {}", filePath);
                // Return relative path for reports (path relative to the report folder)
                return fileName;
            } else {
                logger.warn("Screenshot file exists but appears empty: {}", filePath);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error taking screenshot: {}", e.getMessage(), e);
            
            // Try an alternative screenshot method
            try {
                logger.info("Trying alternative screenshot method...");
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String fileName = testCaseId + "_Step" + stepNumber + "_alt_" + timestamp + ".png";
                String reportFolder = ReportManager.getCurrentReportFolder();
                String filePath = reportFolder + "/Screenshots/" + fileName;
                
                java.nio.file.Files.copy(
                    srcFile.toPath(),
                    Paths.get(filePath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );
                
                logger.info("Alternative screenshot method successful: {}", filePath);
                return fileName;
            } catch (Exception altEx) {
                logger.error("Alternative screenshot method also failed: {}", altEx.getMessage(), altEx);
                return null;
            }
        }
    }
    
    /**
     * Waits for the page to be fully loaded
     * 
     * @param driver The WebDriver instance
     * @param timeoutSeconds Maximum time to wait in seconds
     */
    private static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            
            // Wait for the document to be ready
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            
            // Additional time for any JavaScript to render content
            Thread.sleep(500);
            
            logger.debug("Page fully loaded and ready for screenshot");
        } catch (Exception e) {
            logger.warn("Timeout waiting for page load: {}", e.getMessage());
            // Continue anyway, try to take screenshot in current state
        }
    }
    
    /**
     * Navigates to a URL
     * 
     * @param url The URL to navigate to
     */
    public static void navigateTo(String url) {
        WebDriver driver = getDriver();
        
        try {
            logger.info("Navigating to URL: {}", url);
            driver.get(url);
            waitForPageLoad(driver, 30);
            logger.info("Successfully navigated to: {}", url);
        } catch (Exception e) {
            logger.error("Error navigating to {}: {}", url, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Gets the title of the current page
     * 
     * @return The page title
     */
    public static String getPageTitle() {
        WebDriver driver = getDriver();
        try {
            return driver.getTitle();
        } catch (Exception e) {
            logger.error("Error getting page title: {}", e.getMessage(), e);
            return "";
        }
    }
}
