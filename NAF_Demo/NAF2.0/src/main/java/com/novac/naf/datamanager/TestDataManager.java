
/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: TestDataManager.java
 * Description: Centralized facade for accessing both static (Excel) and dynamic (runtime) test data
 */

package com.novac.naf.datamanager;

import com.novac.naf.runner.TestCaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Centralized manager for all test data access combining static Excel data and dynamic runtime data
 */
public class TestDataManager {
    private static final Logger logger = LoggerFactory.getLogger(TestDataManager.class);
    
    // Pattern for cross-test case static data: SheetName.TestCaseID.ColumnName
    private static final Pattern CROSS_TESTCASE_PATTERN = Pattern.compile("([^.]+)\\.([^.]+)\\.(.+)");
    
    // Pattern for current test case static data: SheetName.ColumnName
    private static final Pattern CURRENT_TESTCASE_PATTERN = Pattern.compile("([^.]+)\\.(.+)");
    
    // Pattern for dynamic data references: ${key} or ${TestCaseID.key}
    private static final Pattern DYNAMIC_DATA_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    
    // Pattern for legacy TD references: {{TD.SheetName.ColumnName}}
    private static final Pattern LEGACY_TD_PATTERN = Pattern.compile("\\{\\{TD\\.([^}]+)\\}\\}");
    
    /**
     * Main method to process any data reference and return the resolved value
     * Supports multiple reference formats with priority order:
     * 1. Dynamic context data (${key})
     * 2. Current test case static data (SheetName.ColumnName)
     * 3. Cross-test case static data (SheetName.TestCaseID.ColumnName)
     * 4. Legacy TD references ({{TD.SheetName.ColumnName}})
     * 
     * @param reference The data reference to process
     * @return The resolved value
     */
    public static String processDataReference(String reference) {
        if (reference == null || reference.trim().isEmpty()) {
            return reference;
        }
        
        logger.debug("Processing data reference: '{}'", reference);
        
        // Get current test case ID for context
        String currentTestCaseId = TestCaseManager.getCurrentTestCaseId();
        
        try {
            // 1. Check for dynamic data references first (highest priority)
            Matcher dynamicMatcher = DYNAMIC_DATA_PATTERN.matcher(reference);
            if (dynamicMatcher.find()) {
                String dynamicKey = dynamicMatcher.group(1);
                String dynamicValue = resolveDynamicReference(dynamicKey);
                if (dynamicValue != null) {
                    String result = dynamicMatcher.replaceAll(dynamicValue);
                    logger.debug("Resolved dynamic reference: {} -> {}", reference, result);
                    return result;
                }
            }
            
            // 2. Check for cross-test case static data: SheetName.TestCaseID.ColumnName
            Matcher crossTestCaseMatcher = CROSS_TESTCASE_PATTERN.matcher(reference);
            if (crossTestCaseMatcher.matches()) {
                String sheetName = crossTestCaseMatcher.group(1);
                String targetTestCaseId = crossTestCaseMatcher.group(2);
                String columnName = crossTestCaseMatcher.group(3);
                
                String value = resolveStaticReference(targetTestCaseId, sheetName, columnName);
                if (value != null) {
                    logger.debug("Resolved cross-test case reference: {} -> {}", reference, value);
                    return value;
                }
            }
            
            // 3. Check for current test case static data: SheetName.ColumnName
            Matcher currentTestCaseMatcher = CURRENT_TESTCASE_PATTERN.matcher(reference);
            if (currentTestCaseMatcher.matches() && currentTestCaseId != null) {
                String sheetName = currentTestCaseMatcher.group(1);
                String columnName = currentTestCaseMatcher.group(2);
                
                String value = resolveStaticReference(currentTestCaseId, sheetName, columnName);
                if (value != null) {
                    logger.debug("Resolved current test case reference: {} -> {}", reference, value);
                    return value;
                }
            }
            
            // 4. Check for legacy TD references: {{TD.SheetName.ColumnName}}
            Matcher legacyMatcher = LEGACY_TD_PATTERN.matcher(reference);
            if (legacyMatcher.find() && currentTestCaseId != null) {
                String tdKey = legacyMatcher.group(1);
                if (tdKey.contains(".")) {
                    String sheetName = tdKey.substring(0, tdKey.indexOf('.'));
                    String columnName = tdKey.substring(tdKey.indexOf('.') + 1);
                    
                    String value = resolveStaticReference(currentTestCaseId, sheetName, columnName);
                    if (value != null) {
                        String result = legacyMatcher.replaceAll(value);
                        logger.debug("Resolved legacy TD reference: {} -> {}", reference, result);
                        return result;
                    }
                }
            }
            
            // If no pattern matched or no value found, return original reference
            logger.debug("No matching pattern found or value not found for reference: '{}'", reference);
            return reference;
            
        } catch (Exception e) {
            logger.error("Error processing data reference '{}': {}", reference, e.getMessage(), e);
            return reference; // Return original reference on error for graceful degradation
        }
    }
    
    /**
     * Resolves dynamic data references from TestContext
     * Supports: key, GLOBAL.key, TestCaseID.key
     * 
     * @param dynamicKey The dynamic key to resolve
     * @return The resolved value or null if not found
     */
    private static String resolveDynamicReference(String dynamicKey) {
        try {
            // Check if it's a global reference
            if (dynamicKey.startsWith("GLOBAL.")) {
                String globalKey = dynamicKey.substring(7); // Remove "GLOBAL." prefix
                return TestContext.retrieveGlobalAsString(globalKey);
            }
            
            // Check if it's a cross-test case dynamic reference: TestCaseID.key
            if (dynamicKey.contains(".")) {
                // For now, treat as regular key - can be enhanced later for cross-test case dynamic data
                return TestContext.retrieveAsString(dynamicKey);
            }
            
            // Regular dynamic key
            return TestContext.retrieveAsString(dynamicKey);
            
        } catch (Exception e) {
            logger.error("Error resolving dynamic reference '{}': {}", dynamicKey, e.getMessage());
            return null;
        }
    }
    
    /**
     * Resolves static data references from Excel using TestDataUtility
     * 
     * @param testCaseId The test case ID
     * @param sheetName The sheet name
     * @param columnName The column name
     * @return The resolved value or null if not found
     */
    private static String resolveStaticReference(String testCaseId, String sheetName, String columnName) {
        try {
            // Use existing TestDataUtility's DataManager to get the value
            return TestDataUtility.getTestDataValue(testCaseId, sheetName, columnName);
            
        } catch (Exception e) {
            logger.error("Error resolving static reference for testCase: {}, sheet: {}, column: {}: {}", 
                    testCaseId, sheetName, columnName, e.getMessage());
            return null;
        }
    }
    
    /**
     * Stores a dynamic value in the test context
     * 
     * @param key The key to store under
     * @param value The value to store
     */
    public static void storeDynamicValue(String key, Object value) {
        TestContext.store(key, value);
    }
    
    /**
     * Stores a global dynamic value accessible across test cases
     * 
     * @param key The key to store under
     * @param value The value to store
     */
    public static void storeGlobalDynamicValue(String key, Object value) {
        TestContext.storeGlobal(key, value);
    }
    
    /**
     * Gets a static test data value for the current test case
     * 
     * @param sheetName The sheet name
     * @param columnName The column name
     * @return The test data value
     */
    public static String getStaticTestData(String sheetName, String columnName) {
        String currentTestCaseId = TestCaseManager.getCurrentTestCaseId();
        if (currentTestCaseId == null) {
            throw new RuntimeException("No current test case ID available");
        }
        return resolveStaticReference(currentTestCaseId, sheetName, columnName);
    }
    
    /**
     * Gets a static test data value for a specific test case
     * 
     * @param testCaseId The test case ID
     * @param sheetName The sheet name
     * @param columnName The column name
     * @return The test data value
     */
    public static String getStaticTestData(String testCaseId, String sheetName, String columnName) {
        return resolveStaticReference(testCaseId, sheetName, columnName);
    }
    
    /**
     * Gets a dynamic value from the test context
     * 
     * @param key The key to retrieve
     * @return The dynamic value as String
     */
    public static String getDynamicValue(String key) {
        return TestContext.retrieveAsString(key);
    }
    
    /**
     * Gets a global dynamic value from the test context
     * 
     * @param key The key to retrieve (without GLOBAL prefix)
     * @return The dynamic value as String
     */
    public static String getGlobalDynamicValue(String key) {
        return TestContext.retrieveGlobalAsString(key);
    }
}
