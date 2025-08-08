/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: TestDataUtility.java
 * Description: Utility for processing test data references with enhanced static and dynamic data support
 */

package com.novac.naf.datamanager;

import com.novac.naf.config.ConfigLoader;
import com.novac.naf.runner.TestCaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Enhanced utility for processing both static and dynamic test data references
 */
public class TestDataUtility {
    private static final Logger logger = LoggerFactory.getLogger(TestDataUtility.class);
    private static final Pattern TD_PATTERN = Pattern.compile("\\{\\{TD\\.([^}]+)\\}\\}"); //{{TD.k(*}}
    
    private static DataManager dataManager;
    
    /**
     * Initializes the TestDataUtility with a DataManager instance
     * 
     * @param configLoader The configuration loader
     */
    public static void initialize(ConfigLoader configLoader) {
        dataManager = new DataManager(configLoader);
        logger.info("TestDataUtility initialized with enhanced static and dynamic data support");
    }
    
    /**
     * Enhanced method to process test data references supporting both static and dynamic data
     * Now delegates to TestDataManager for comprehensive data resolution
     * 
     * @param reference The reference string which may contain test data placeholders
     * @return The processed string with placeholders replaced by actual values
     * @throws RuntimeException if test data cannot be resolved and no fallback is available
     */
    public static String processTestDataReference(String reference) {
        if (reference == null || reference.isEmpty()) {
            return reference;
        }
        
        logger.debug("Processing test data reference: '{}'", reference);
        
        // Validate that we have a test case context before proceeding
        String testCaseId = TestCaseManager.getCurrentTestCaseId();
        if (testCaseId == null || testCaseId.isEmpty()) {
            logger.error("CRITICAL: No current test case ID available from TestCaseManager");
            logger.error("Cannot process test data references without proper test case context");
            logger.error("Reference that failed: '{}'", reference);
            throw new RuntimeException("No test case ID context available. Ensure TestCaseManager.setCurrentTestCaseId() is called before processing test data references: " + reference);
        }
        
        logger.info("Processing test data reference: '{}' for test case: {}", reference, testCaseId);
        
        // Check if DataManager is properly initialized
        if (dataManager == null) {
            logger.error("CRITICAL: DataManager not initialized, cannot process test data references");
            throw new RuntimeException("DataManager not initialized. Call TestDataUtility.initialize() first.");
        }
        
        try {
            // Use enhanced TestDataManager for comprehensive data resolution
            String processedRef = TestDataManager.processDataReference(reference);
            
            // Final validation: ensure we didn't return the original reference for a data pattern
            if (isDataReference(reference) && processedRef.equals(reference)) {
                // Try alternative resolution before failing
                processedRef = tryAlternativeResolution(reference, testCaseId);
            }
            
            logger.info("Successfully processed test data reference: {} -> {}", reference, processedRef);
            return processedRef;
            
        } catch (Exception e) {
            logger.error("CRITICAL ERROR processing test data reference '{}' for test case '{}': {}", reference, testCaseId, e.getMessage());
            logger.error("This will cause test failures. Check test data sheets and ensure proper data is available.");
            throw new RuntimeException("Failed to process test data reference '" + reference + "' for test case '" + testCaseId + "': " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets a specific test data value from Excel for cross-test case access
     * 
     * @param testCaseId The test case ID
     * @param sheetName The sheet name
     * @param columnName The column name
     * @return The value
     */
    public static String getTestDataValue(String testCaseId, String sheetName, String columnName) {
        logger.debug("Getting test data value for testCase: {}, sheet: {}, column: {}", testCaseId, sheetName, columnName);
        
        if (dataManager == null) {
            throw new RuntimeException("DataManager not initialized. Call TestDataUtility.initialize() first.");
        }
        
        return dataManager.getTestDataValue(testCaseId, sheetName, columnName);
    }
    
    /**
     * Tries alternative approaches to resolve test data references
     * 
     * @param reference The original reference
     * @param testCaseId The test case ID
     * @return The resolved value or throws exception
     */
    private static String tryAlternativeResolution(String reference, String testCaseId) {
        logger.info("Attempting alternative resolution for: {} with test case: {}", reference, testCaseId);
        
        try {
            // Try with different case variations of the test case ID
            String[] testCaseVariations = {
                testCaseId,
                testCaseId.toUpperCase(),
                testCaseId.toLowerCase(),
                testCaseId.replace("-", "_"),
                testCaseId.replace("_", "-")
            };
            
            for (String variation : testCaseVariations) {
                try {
                    String result = dataManager.processDataReference(reference, variation);
                    if (!result.equals(reference)) {
                        logger.info("Successfully resolved using test case variation: {} -> {}", variation, result);
                        return result;
                    }
                } catch (Exception e) {
                    logger.debug("Failed with test case variation {}: {}", variation, e.getMessage());
                }
            }
            
            // If all variations fail, throw a comprehensive error
            throw new RuntimeException("All alternative resolution attempts failed for reference: " + reference + " with test case: " + testCaseId);
            
        } catch (Exception e) {
            logger.error("Alternative resolution failed for '{}': {}", reference, e.getMessage());
            throw new RuntimeException("Alternative resolution failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Checks if a string appears to be a test data reference
     * 
     * @param text The text to check
     * @return true if it looks like a data reference pattern
     */
    private static boolean isDataReference(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        
        // Check for patterns that indicate test data references
        return text.contains(".") && 
               (text.matches("^[A-Za-z]+\\.[A-Za-z]+.*") || text.contains("{{TD.") || text.contains("${"));
    }
    
    /**
     * Closes resources used by the TestDataUtility
     */
    public static void close() {
        if (dataManager != null) {
            dataManager.closeConnection();
        }
    }
}
