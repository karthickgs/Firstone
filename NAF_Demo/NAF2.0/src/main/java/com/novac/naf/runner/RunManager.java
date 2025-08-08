/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: RunManager.java
 * Description: Manages test execution with robust scenario filtering and test data context
 */

package com.novac.naf.runner;

import com.novac.naf.config.ConfigLoader;
import com.novac.naf.reporting.ReportManager;
import com.novac.naf.reporting.XmlJsonReportGenerator;
import com.novac.naf.qmetry.QMetryClient;
import com.novac.naf.webdriver.WebDriverManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Manages test execution based on RunManager.xlsx with enhanced scenario filtering
 */
public class RunManager {
    private static final Logger logger = LoggerFactory.getLogger(RunManager.class);
    private static final String RUN_MANAGER_SHEET = "Run Manager";
    
    private final ConfigLoader configLoader;
    private final String excelPath;
    private final List<TestCase> testCasesToExecute = new ArrayList<>();
    private final CucumberLauncher cucumberLauncher;
    private final QMetryClient qMetryClient;
    private final XmlJsonReportGenerator xmlJsonReportGenerator;
    
    /**
     * Constructs a new RunManager instance
     * 
     * @param configLoader The configuration loader instance
     * @throws RunManagerException If initialization fails
     */
    public RunManager(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.excelPath = configLoader.getConfigValue("ExcelPath");
        this.cucumberLauncher = new CucumberLauncher();
        this.qMetryClient = new QMetryClient(configLoader);
        this.xmlJsonReportGenerator = new XmlJsonReportGenerator(configLoader);
        
        validateConfiguration();
        loadTestCases();
        
        // Ensure screenshot directory exists
        String screenshotsDir = ReportManager.getReportPath() + "/Screenshots";
        try {
            File screenshotDir = new File(screenshotsDir);
            if (!screenshotDir.exists()) {
                boolean created = screenshotDir.mkdirs();
                if (created) {
                    logger.info("Created screenshots directory: {}", screenshotsDir);
                } else {
                    logger.warn("Failed to create screenshots directory: {}", screenshotsDir);
                }
            }
        } catch (Exception e) {
            logger.error("Error creating screenshots directory: {}", e.getMessage());
        }
    }
    
    /**
     * Validates the RunManager configuration
     * 
     * @throws RunManagerException If configuration is invalid
     */
    private void validateConfiguration() {
        // Check if Excel path is valid
        if (excelPath == null || excelPath.trim().isEmpty()) {
            throw new RunManagerException("Excel path is not configured in the ConfigLoader");
        }
        
        logger.info("Using Excel file at: {}", excelPath);
        
        // Validate Excel file exists
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) {
            throw new RunManagerException("Excel file not found at: " + excelPath);
        }
        
        // Validate if path is accessible
        if (!excelFile.canRead()) {
            throw new RunManagerException("Excel file exists but cannot be read: " + excelPath);
        }
    }
    
    /**
     * Loads test cases from the Excel file with duplicate prevention
     * 
     * @throws RunManagerException If loading test cases fails
     */
    private void loadTestCases() {
        logger.info("Loading test cases from: {}", excelPath);
        
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
             
            Sheet runSheet = workbook.getSheet(RUN_MANAGER_SHEET);
            if (runSheet == null) {
                throw new RunManagerException(RUN_MANAGER_SHEET + " sheet not found in " + excelPath);
            }
            
            logger.debug("Total rows in {} sheet: {}", RUN_MANAGER_SHEET, runSheet.getPhysicalNumberOfRows());
            
            // Clear any existing test cases before loading
            testCasesToExecute.clear();
            
            // Use a Set to track already processed test case IDs to prevent duplicates
            Set<String> processedTestCaseIds = new HashSet<>();
            
            // Process each row (skipping header)
            for (int i = 1; i <= runSheet.getLastRowNum(); i++) {
                Row row = runSheet.getRow(i);
                if (row == null) {
                    logger.debug("Skipping empty row at index: {}", i);
                    continue;
                }
                
                Cell testIdCell = row.getCell(0);
                Cell featureFileCell = row.getCell(1);
                Cell executeCell = row.getCell(2);
                
                if (testIdCell == null || featureFileCell == null || executeCell == null) {
                    logger.debug("Skipping incomplete row at index: {} (missing required cells)", i);
                    continue;
                }
                
                String testId = getCellValueAsString(testIdCell).trim();
                String featureFile = getCellValueAsString(featureFileCell).trim();
                String execute = getCellValueAsString(executeCell).trim();
                
                logger.debug("Row {}: ID={}, Feature={}, Execute={}", i, testId, featureFile, execute);
                
                if (testId.isEmpty() || featureFile.isEmpty()) {
                    logger.debug("Skipping row {} with empty test ID or feature file", i);
                    continue;
                }
                
                // CRITICAL: Only process test cases marked with "Y" for execution
                if ("Y".equalsIgnoreCase(execute)) {
                    // Check if this test case ID has already been processed to prevent duplicates
                    if (processedTestCaseIds.contains(testId)) {
                        logger.warn("Test case {} already scheduled for execution - skipping duplicate entry at row {}", testId, i);
                        continue;
                    }
                    
                    TestCase testCase = new TestCase(testId, featureFile);
                    testCasesToExecute.add(testCase);
                    processedTestCaseIds.add(testId);
                    logger.info("Added test case for execution: {} - {} (Execute: {})", testId, featureFile, execute);
                } else {
                    logger.info("Skipping test case {} - Execute flag is '{}' (not Y)", testId, execute);
                }
            }
            
            logger.info("Loaded {} unique test cases marked for execution (Y flag)", testCasesToExecute.size());
            
            // Log all loaded test cases for verification
            for (TestCase testCase : testCasesToExecute) {
                logger.info("Test case to execute: {} - {}", testCase.getTestId(), testCase.getFeatureFile());
            }
            
        } catch (IOException e) {
            throw new RunManagerException("IO error reading Excel file: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RunManagerException("Error loading test cases: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets cell value as string, handling different cell types
     * 
     * @param cell The cell to get value from
     * @return The cell value as string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Convert numeric to string without trailing zeros
                    return String.valueOf(cell.getNumericCellValue()).replaceAll("\\.0+$", "");
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue()).replaceAll("\\.0+$", "");
                    } catch (Exception ex) {
                        return cell.getCellFormula();
                    }
                }
            default:
                return "";
        }
    }
    
    /**
     * Executes the test run with enhanced scenario filtering based on Y-flagged test cases only
     */
    public void executeTestRun() {
        if (testCasesToExecute.isEmpty()) {
            logger.warn("No test cases marked for execution (Y flag) in {}", excelPath);
            System.out.println("No test cases marked for execution. Please check the Run Manager sheet and ensure Execute column has 'Y' for desired test cases.");
            return;
        }
        
        logger.info("Starting execution of {} test cases marked with Y flag (enhanced scenario filtering)", testCasesToExecute.size());
        System.out.println("Starting execution of " + testCasesToExecute.size() + " test cases with scenario filtering");
        
        // Record start time for execution
        long startTime = System.currentTimeMillis();
        
        // START BATCH SESSION for optimized browser reuse
        WebDriverManager.startBatchSession();
        
        int successCount = 0;
        int failureCount = 0;
        
        try {
            // Group test cases by feature file to optimize execution
            List<FeatureExecution> featureExecutions = groupTestCasesByFeature();
            
            // Execute each feature file with its selected test cases using enhanced filtering
            for (FeatureExecution featureExecution : featureExecutions) {
                logger.info("Executing feature file: {} with {} Y-flagged test cases", 
                    featureExecution.getFeatureFile(), featureExecution.getTestCaseIds().size());
                
                // CRITICAL: Set test case context before execution
                for (String testCaseId : featureExecution.getTestCaseIds()) {
                    TestCaseManager.setCurrentTestCaseId(testCaseId);
                    logger.info("Set test case context for execution: {}", testCaseId);
                }
                
                // Execute the feature with tag filtering for selected test cases only
                // This ensures only Y-flagged scenarios execute
                boolean featureResult = executeFeatureWithEnhancedTags(featureExecution);
                
                if (featureResult) {
                    successCount += featureExecution.getTestCaseIds().size();
                    logger.info("Feature {} completed successfully for Y-flagged test cases: {}", 
                        featureExecution.getFeatureFile(), featureExecution.getTestCaseIds());
                } else {
                    failureCount += featureExecution.getTestCaseIds().size();
                    logger.error("Feature {} failed for Y-flagged test cases: {}", 
                        featureExecution.getFeatureFile(), featureExecution.getTestCaseIds());
                }
                
                // Small delay between feature executions for stability
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Test execution interrupted");
                    break;
                }
            }
        } finally {
            // END BATCH SESSION - Close browser once at the end
            WebDriverManager.endBatchSession();
            
            // Clear the test case context when execution is complete
            TestCaseManager.clearCurrentTestCaseId();
        }
        
        // Calculate and log total execution time
        long totalExecutionTime = System.currentTimeMillis() - startTime;
        logger.info("Enhanced scenario filtering execution completed: {} successful, {} failed, total time: {} seconds", 
            successCount, failureCount, totalExecutionTime / 1000);
        System.out.println(String.format("Scenario filtering execution completed: %d successful, %d failed, total time: %d seconds", 
            successCount, failureCount, totalExecutionTime / 1000));
        
        // Generate XML/JSON report if configured
        String reportFilePath = xmlJsonReportGenerator.generateReport();
        if (reportFilePath != null) {
            logger.info("Generated {} report: {}", 
                configLoader.getConfigValue("ResultUploadFormat"), reportFilePath);
            
            // Upload the generated report file to QMetry
            File reportFile = new File(reportFilePath);
            if (reportFile.exists()) {
                boolean uploadSuccess = qMetryClient.uploadResultsFile(reportFile);
                if (uploadSuccess) {
                    logger.info("Successfully uploaded results to QMetry");
                } else {
                    logger.error("Failed to upload results to QMetry");
                }
            } else {
                logger.warn("Report file not found for QMetry upload: {}", reportFilePath);
            }
        }
        
        // Make sure to finalize the report after all test cases are executed
        ReportManager.finalizeReporting();
    }
    
    /**
     * Groups test cases by feature file to optimize execution while preserving Excel order
     * 
     * @return List of FeatureExecution objects containing feature files and their Y-flagged test case IDs in Excel order
     */
    private List<FeatureExecution> groupTestCasesByFeature() {
        List<FeatureExecution> featureExecutions = new ArrayList<>();
        
        // Group test cases by feature file using LinkedHashMap to preserve Excel order
        testCasesToExecute.stream()
            .collect(Collectors.groupingBy(
                TestCase::getFeatureFile,
                LinkedHashMap::new,
                Collectors.toList()))
            .forEach((featureFile, testCases) -> {
                List<String> testCaseIds = testCases.stream()
                    .map(TestCase::getTestId)
                    .collect(Collectors.toList());
                
                featureExecutions.add(new FeatureExecution(featureFile, testCaseIds));
                logger.info("Grouped feature {}: Y-flagged test cases {}", featureFile, testCaseIds);
            });
        
        return featureExecutions;
    }
    
    /**
     * Executes a feature file with enhanced tag filtering for Y-flagged test cases only
     * 
     * @param featureExecution The feature execution containing feature file and Y-flagged test case IDs
     * @return true if execution was successful, false otherwise
     */
    private boolean executeFeatureWithEnhancedTags(FeatureExecution featureExecution) {
        try {
            String featureFile = featureExecution.getFeatureFile();
            List<String> testCaseIds = featureExecution.getTestCaseIds();
            
            logger.info("Executing feature file: {} with Y-flagged test cases: {}", featureFile, testCaseIds);
            System.out.println("Executing feature: " + featureFile + " with Y-flagged test cases: " + testCaseIds);
            
            // Record start time for feature execution
            Instant featureStartTime = Instant.now();
            
            // Verify feature file exists
            String featurePath = "./src/main/resources/TestScripts/" + featureFile.trim();
            verifyFeatureFileExists(featurePath, testCaseIds.get(0));
            
            // CRITICAL: Execute Cucumber feature file with enhanced tag filtering
            // This ensures only Y-flagged scenarios execute
            boolean result = cucumberLauncher.runFeatureWithTags(featurePath, testCaseIds);
            
            // Calculate execution time
            Instant featureEndTime = Instant.now();
            long executionTimeMs = Duration.between(featureStartTime, featureEndTime).toMillis();
            
            // Add results for each Y-flagged test case to XML/JSON report generator
            for (String testCaseId : testCaseIds) {
                String errorMessage = result ? null : "Test execution failed. Check report for details.";
                
                xmlJsonReportGenerator.addTestResult(
                    testCaseId, 
                    featureFile, 
                    result ? "PASS" : "FAIL",
                    executionTimeMs / testCaseIds.size(), // Distribute time across test cases
                    errorMessage,
                    null // Steps will be populated by the report generator if needed
                );
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("Error executing feature with enhanced tags {}: {}", featureExecution.getTestCaseIds(), e.getMessage(), e);
            System.err.println("Error executing feature " + featureExecution.getFeatureFile() + ": " + e.getMessage());
            
            handleFeatureExecutionException(featureExecution, e);
            return false;
        }
    }
    
    /**
     * Verifies that a feature file exists, creating directories if needed
     * 
     * @param featurePath The path to the feature file
     * @param testId The test ID for reporting errors
     * @throws RunManagerException If the feature file cannot be found or accessed
     */
    private void verifyFeatureFileExists(String featurePath, String testId) {
        File featureFile = new File(featurePath);
        
        if (!featureFile.exists()) {
            throw new RunManagerException("Feature file not found: " + featurePath + " for test case: " + testId);
        }
    }
    
    /**
     * Handles exceptions during feature execution
     * 
     * @param featureExecution The feature execution that failed
     * @param e The exception that occurred
     */
    private void handleFeatureExecutionException(FeatureExecution featureExecution, Exception e) {
        try {
            // Create test entries for each test case in the feature if they don't exist
            for (String testCaseId : featureExecution.getTestCaseIds()) {
                if (!ReportManager.isTestActive()) {
                    ReportManager.startTest(testCaseId, featureExecution.getFeatureFile());
                }
                ReportManager.logFail("Exception during test execution", e.getMessage());
                ReportManager.endTest();
                
                // Add failed result to XML/JSON report generator
                xmlJsonReportGenerator.addTestResult(
                    testCaseId,
                    featureExecution.getFeatureFile(),
                    "FAIL",
                    0,
                    "Exception: " + e.getMessage(),
                    null
                );
            }
        } catch (Exception reportException) {
            logger.error("Error handling test reporting: {}", reportException.getMessage());
        }
    }
    
    /**
     * Custom exception class for RunManager errors
     */
    public static class RunManagerException extends RuntimeException {
        public RunManagerException(String message) {
            super(message);
        }
        
        public RunManagerException(String message, Throwable cause) {
            super(message, cause);
        }
    }
    
    /**
     * Model class for a test case to be executed
     */
    public static class TestCase {
        private final String testId;
        private final String featureFile;
        
        public TestCase(String testId, String featureFile) {
            this.testId = testId;
            this.featureFile = featureFile;
        }
        
        public String getTestId() {
            return testId;
        }
        
        public String getFeatureFile() {
            return featureFile;
        }
    }
    
    /**
     * Model class for feature execution containing feature file and associated Y-flagged test case IDs
     */
    public static class FeatureExecution {
        private final String featureFile;
        private final List<String> testCaseIds;
        
        public FeatureExecution(String featureFile, List<String> testCaseIds) {
            this.featureFile = featureFile;
            this.testCaseIds = new ArrayList<>(testCaseIds);
        }
        
        public String getFeatureFile() {
            return featureFile;
        }
        
        public List<String> getTestCaseIds() {
            return new ArrayList<>(testCaseIds);
        }
    }
}
