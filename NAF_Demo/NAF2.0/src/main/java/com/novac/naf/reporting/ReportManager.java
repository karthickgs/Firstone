/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: ReportManager.java
 * Description: Manages test reporting including HTML reports, Excel reports, and screenshots
 */

package com.novac.naf.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages test reporting including HTML reports, Excel reports, and screenshots
 */
public class ReportManager {
    private static final Logger logger = LoggerFactory.getLogger(ReportManager.class);
    private static ExtentReports extent;
    private static ExtentTest currentTest;
    private static String reportPath;
    private static String projectName = "Novac Automation Framework";
    private static boolean initialized = false;
    
    // PER-TEST STEP COUNTER - Resets for each test case
    private static int currentTestStepCounter = 1;
    
    private static String screenshotMode = "fail_only";
    private static Map<String, Object> testResults = new HashMap<>();
    private static ExcelReportGenerator excelReportGenerator;
    
    // ULTIMATE CENTRALIZED TIMESTAMP - Generated ONCE and NEVER changed
    private static final String EXECUTION_TIMESTAMP = generateExecutionTimestamp();
    
    // Map to store detailed test execution data for Excel report
    private static Map<String, Object> executionData = new HashMap<>();
    private static List<Map<String, Object>> testCases = new ArrayList<>();
    private static Map<String, Object> currentTestData;
    private static List<Map<String, Object>> currentTestSteps;
    private static String currentTestId;
    private static String currentTestName;
    private static String currentTestStartTime;
    
    /**
     * Generates the execution timestamp ONCE at class loading time
     * This ensures the timestamp is NEVER regenerated during execution
     * 
     * @return The execution timestamp string
     */
    private static String generateExecutionTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("EXECUTION TIMESTAMP GENERATED (ONCE): {}", timestamp);
        return timestamp;
    }
    
    /**
     * Gets the current step number for the active test and increments it
     * This counter RESETS for each test case
     * 
     * @return The current step number before incrementing
     */
    public static int getAndIncrementStepNumber() {
        int currentStep = currentTestStepCounter;
        currentTestStepCounter++;
        logger.debug("Test step counter incremented to: {}", currentTestStepCounter);
        return currentStep;
    }
    
    /**
     * Gets the current step number without incrementing
     * 
     * @return The current step number
     */
    public static int getCurrentStepNumber() {
        return currentTestStepCounter;
    }
    
    /**
     * Resets the step counter for a new test case
     * Called when starting each new test case
     */
    public static void resetStepCounterForNewTest() {
        currentTestStepCounter = 1;
        logger.info("Step counter reset to 1 for new test case");
    }
    
    /**
     * Gets the ULTIMATE centralized timestamp - NEVER changes during execution
     * This timestamp is generated ONCE at class loading and reused everywhere
     * 
     * @return The execution timestamp string
     */
    public static String getExecutionTimestamp() {
        return EXECUTION_TIMESTAMP;
    }
    
    /**
     * Initializes the reporting framework with ULTIMATE centralized folder creation
     * This method ensures ONLY ONE folder is created per execution
     * 
     * @param baseReportPath The base path where reports will be saved
     * @param projectName The name of the project
     */
    public static void initializeReporting(String baseReportPath, String projectName) {
        if (initialized) {
            logger.warn("Reporting already initialized. Using existing report folder: {}", reportPath);
            return;
        }
        
        try {
            ReportManager.projectName = projectName != null ? projectName : "Novac Automation Framework";
            
            // Use the ULTIMATE centralized timestamp - NEVER regenerate
            String timestamp = getExecutionTimestamp();
            
            // Create SINGLE timestamped report directory - ONLY ONCE
            reportPath = baseReportPath + File.separator + timestamp;
            Path reportDir = Paths.get(reportPath);
            
            // Create directory structure ONLY if it doesn't exist
            if (!Files.exists(reportDir)) {
                Files.createDirectories(reportDir);
                logger.info("CREATED SINGLE REPORT DIRECTORY: {}", reportPath);
            } else {
                logger.info("REUSING EXISTING REPORT DIRECTORY: {}", reportPath);
            }
            
            // Create Screenshots directory immediately to avoid later creation attempts
            Path screenshotsDir = Paths.get(reportPath, "Screenshots");
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
                logger.info("CREATED SCREENSHOTS DIRECTORY: {}", screenshotsDir);
            }
            
            logger.info("Initializing reporting at: {}", reportPath);
            
            // Create HTML report filename with ULTIMATE centralized timestamp
            String htmlReportFilename = projectName.replaceAll("\\s+", "_") + "_Report_" + timestamp + ".html";
            String htmlReportPath = reportPath + File.separator + htmlReportFilename;
            
            // Create HTML reporter
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(htmlReportPath);
            htmlReporter.config().setDocumentTitle(projectName + " - Test Report");
            htmlReporter.config().setReportName(projectName);
            htmlReporter.config().setTheme(Theme.STANDARD);
            
            // Initialize ExtentReports and attach the HTML reporter
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            
            // Set system info
            extent.setSystemInfo("Project", projectName);
            extent.setSystemInfo("Environment", "Test");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Report Generated", timestamp);
            
            // Initialize Excel report generator with ULTIMATE centralized timestamp
            String excelReportFilename = projectName.replaceAll("\\s+", "_") + "_Report_" + timestamp + ".xlsx";
            excelReportGenerator = new ExcelReportGenerator(reportPath + File.separator + excelReportFilename);
            
            // Initialize execution data for Excel report
            executionData.clear();
            executionData.put("projectName", projectName);
            executionData.put("executionDate", timestamp);
            executionData.put("totalTests", 0);
            executionData.put("passedTests", 0);
            executionData.put("failedTests", 0);
            testCases.clear();
            executionData.put("testCases", testCases);
            
            initialized = true;
            testResults.clear();
            
            logger.info("REPORTING INITIALIZED SUCCESSFULLY - SINGLE FOLDER: {}", reportPath);
            logger.info("ULTIMATE TIMESTAMP USED: {}", timestamp);
            
        } catch (Exception e) {
            logger.error("Error initializing reporting: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize reporting", e);
        }
    }
    
    /**
     * Returns the current report folder path - GUARANTEED to be the same throughout execution
     * 
     * @return The current report folder path
     */
    public static String getCurrentReportFolder() {
        return reportPath;
    }
    
    /**
     * Returns the report path - alias for backward compatibility
     * 
     * @return The current report folder path
     */
    public static String getReportPath() {
        return reportPath;
    }
    
    /**
     * Gets the ULTIMATE centralized timestamp for external use (e.g., screenshot naming)
     * This method ensures ALL components use the SAME timestamp
     * 
     * @return The ULTIMATE centralized timestamp string
     */
    public static String getCurrentTimestamp() {
        return getExecutionTimestamp();
    }
    
    /**
     * Sets the screenshot mode
     * 
     * @param mode The screenshot mode ("all", "pass_fail", "fail_only", "none")
     */
    public static void setScreenshotMode(String mode) {
        ReportManager.screenshotMode = mode;
        logger.info("Screenshot mode set to: {}", mode);
    }
    
    /**
     * Starts a new test in the report
     * RESETS step counter for each new test case
     * 
     * @param testId The ID of the test case
     * @param testName The name of the test case
     */
    public static void startTest(String testId, String testName) {
        if (!initialized) {
            logger.error("Reporting not initialized. Call initializeReporting() first.");
            return;
        }
        
        if (currentTest != null) {
            logger.warn("Test already started. End the current test before starting a new one.");
            endTest();
        }
        
        // RESET step counter for each new test case
        resetStepCounterForNewTest();
        
        currentTest = extent.createTest(testName + " (" + testId + ")");
        currentTest.assignCategory(testId);
        testResults.put(testId, "PENDING");
        
        // Store test data for Excel reporting
        currentTestId = testId;
        currentTestName = testName;
        
        // Initialize test case data for Excel report
        currentTestData = new HashMap<>();
        currentTestData.put("testId", testId);
        currentTestData.put("description", testName);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        currentTestStartTime = LocalDateTime.now().format(formatter);
        currentTestData.put("startTime", currentTestStartTime);
        
        currentTestData.put("status", "PENDING");
        currentTestData.put("totalSteps", 0);
        currentTestData.put("passedSteps", 0);
        currentTestData.put("failedSteps", 0);
        
        currentTestSteps = new ArrayList<>();
        currentTestData.put("steps", currentTestSteps);
        
        testCases.add(currentTestData);
        
        logger.info("Started test: {} - {} (Step counter reset)", testId, testName);
    }
    
    /**
     * Checks if a test is currently active
     * 
     * @return True if a test is active, false otherwise
     */
    public static boolean isTestActive() {
        return currentTest != null;
    }
    
    /**
     * Logs a step with a status - single unified method using per-test step counter
     * 
     * @param status The status of the step (PASS, FAIL, INFO, WARNING, SKIP)
     * @param stepName The name of the step
     * @param details Additional details about the step
     * @param screenshotPath Optional screenshot path (can be null)
     */
    private static void logStepUnified(Status status, String stepName, String details, String screenshotPath) {
        if (!initialized) {
            logger.error("Reporting not initialized. Call initializeReporting() first.");
            return;
        }
        
        if (currentTest == null) {
            logger.warn("No test started. Start a test before logging steps.");
            return;
        }
        
        // Get per-test step number - RESETS for each test case
        int stepNumber = getAndIncrementStepNumber();
        String logMessage = String.format("Step %d: %s - %s", stepNumber, stepName, details);
        
        try {
            // Log to ExtentReports with or without screenshot
            if (screenshotPath != null && !screenshotPath.trim().isEmpty()) {
                // Verify screenshot exists before attaching
                Path screenshotAbsolutePath = Paths.get(reportPath, "Screenshots", screenshotPath);
                if (Files.exists(screenshotAbsolutePath)) {
                    String relativeScreenshotPath = "Screenshots/" + screenshotPath;
                    currentTest.log(status, logMessage, MediaEntityBuilder.createScreenCaptureFromPath(relativeScreenshotPath).build());
                    logger.info(logMessage + " (Screenshot attached: {})", screenshotPath);
                } else {
                    currentTest.log(status, logMessage + " (Screenshot not found)");
                    logger.warn(logMessage + " (Screenshot not found at: {})", screenshotAbsolutePath);
                    screenshotPath = null; // Clear invalid screenshot path
                }
            } else {
                currentTest.log(status, logMessage);
                logger.info(logMessage);
            }
            
            // Update test step counter for Excel report
            if (currentTestData != null) {
                currentTestData.put("totalSteps", (Integer)currentTestData.getOrDefault("totalSteps", 0) + 1);
                
                if (status == Status.PASS) {
                    currentTestData.put("passedSteps", (Integer)currentTestData.getOrDefault("passedSteps", 0) + 1);
                } else if (status == Status.FAIL) {
                    currentTestData.put("failedSteps", (Integer)currentTestData.getOrDefault("failedSteps", 0) + 1);
                    // Update test result to FAIL
                    testResults.put(currentTestId, "FAIL");
                    currentTestData.put("status", "FAIL");
                }
                
                // Store step details for Excel report with per-test step number
                Map<String, Object> stepData = new HashMap<>();
                stepData.put("stepNumber", stepNumber);
                stepData.put("description", stepName);
                stepData.put("status", status.toString());
                stepData.put("details", details);
                stepData.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                
                if (screenshotPath != null && !screenshotPath.trim().isEmpty()) {
                    stepData.put("screenshotPath", "Screenshots/" + screenshotPath);
                }
                
                currentTestSteps.add(stepData);
            }
            
        } catch (Exception e) {
            logger.error("Error logging step: {}", e.getMessage(), e);
            currentTest.log(status, logMessage + " (Error during logging)");
        }
    }
    
    /**
     * Logs a passing step
     * 
     * @param stepName The name of the step
     * @param details Additional details about the step
     */
    public static void logPass(String stepName, String details) {
        String screenshotPath = null;
        
        // Check if screenshot should be taken
        if (screenshotMode.equals("all") || screenshotMode.equals("pass_fail")) {
            screenshotPath = com.novac.naf.webdriver.WebDriverManager.takeScreenshot(currentTestId, getCurrentStepNumber());
        }
        
        logStepUnified(Status.PASS, stepName, details, screenshotPath);
    }
    
    /**
     * Logs a failing step
     * 
     * @param stepName The name of the step
     * @param details The details of the failure
     */
    public static void logFail(String stepName, String details) {
        String screenshotPath = null;
        
        // Always attach screenshot on failure if mode is not "none"
        if (!screenshotMode.equals("none")) {
            screenshotPath = com.novac.naf.webdriver.WebDriverManager.takeScreenshot(currentTestId, getCurrentStepNumber());
        }
        
        logStepUnified(Status.FAIL, stepName, details, screenshotPath);
    }
    
    /**
     * Logs an informational message
     * 
     * @param message The message to log
     */
    public static void logInfo(String message) {
        logStepUnified(Status.INFO, "Info", message, null);
    }
    
    /**
     * Logs a warning message
     * 
     * @param message The message to log
     */
    public static void logWarning(String message) {
        logStepUnified(Status.WARNING, "Warning", message, null);
    }
    
    /**
     * Logs a skipped step
     * 
     * @param stepName The name of the step
     * @param details Additional details about the step
     */
    public static void logSkip(String stepName, String details) {
        logStepUnified(Status.SKIP, stepName, details, null);
    }
    
    /**
     * Ends the current test and updates the overall result
     */
    public static void endTest() {
        if (!initialized) {
            logger.error("Reporting not initialized. Call initializeReporting() first.");
            return;
        }
        
        if (currentTest == null) {
            logger.warn("No test started. Cannot end test.");
            return;
        }
        
        // Get the test result
        String testResult = (String) testResults.getOrDefault(currentTestId, "PASS");
        
        if ("FAIL".equals(testResult)) {
            currentTest.fail("Test Failed");
            logger.info("Test {} ended with status: FAIL", currentTestId);
            
            // Update execution stats
            executionData.put("failedTests", (Integer)executionData.getOrDefault("failedTests", 0) + 1);
        } else {
            currentTest.pass("Test Passed");
            logger.info("Test {} ended with status: PASS", currentTestId);
            
            // Update test status in Excel report data if it was not already marked as FAIL
            if (currentTestData != null && !"FAIL".equals(currentTestData.get("status"))) {
                currentTestData.put("status", "PASS");
            }
            
            // Update execution stats
            executionData.put("passedTests", (Integer)executionData.getOrDefault("passedTests", 0) + 1);
        }
        
        // Update total tests count
        executionData.put("totalTests", (Integer)executionData.getOrDefault("totalTests", 0) + 1);
        
        // Set test end time and duration for Excel report
        if (currentTestData != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String endTime = LocalDateTime.now().format(formatter);
            currentTestData.put("endTime", endTime);
            
            // Calculate duration if start time is available
            if (currentTestStartTime != null) {
                try {
                    LocalDateTime start = LocalDateTime.parse(currentTestStartTime, formatter);
                    LocalDateTime end = LocalDateTime.parse(endTime, formatter);
                    double durationSeconds = java.time.Duration.between(start, end).getSeconds();
                    currentTestData.put("durationSeconds", durationSeconds);
                } catch (Exception e) {
                    logger.warn("Error calculating test duration: {}", e.getMessage());
                    currentTestData.put("durationSeconds", 0.0);
                }
            }
        }
        
        currentTest = null;
        currentTestId = null;
        currentTestName = null;
        currentTestData = null;
        currentTestSteps = null;
        currentTestStartTime = null;
    }
    
    /**
     * Finalizes the reporting and generates the reports
     */
    public static void finalizeReporting() {
        if (!initialized) {
            logger.error("Reporting not initialized. Call initializeReporting() first.");
            return;
        }
        
        try {
            logger.info("Finalizing reporting...");
            
            // End current test if still active
            if (currentTest != null) {
                endTest();
            }
            
            // Flush the ExtentReports instance to generate the HTML report
            extent.flush();
            
            // Generate the Excel report with the detailed execution data
            ExcelReportGenerator.generateExcelReport(reportPath, executionData);
            
            String timestamp = getExecutionTimestamp();
            logger.info("REPORTS GENERATED SUCCESSFULLY IN SINGLE FOLDER: {}", reportPath);
            logger.info("HTML Report: {}", reportPath + File.separator + projectName.replaceAll("\\s+", "_") + "_Report_" + timestamp + ".html");
            logger.info("Excel Report: {}", reportPath + File.separator + projectName.replaceAll("\\s+", "_") + "_Report_" + timestamp + ".xlsx");
            
            initialized = false;
            
        } catch (Exception e) {
            logger.error("Error finalizing reporting: {}", e.getMessage(), e);
        }
    }
}
