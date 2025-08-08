
/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: TestCaseManager.java
 * Description: Manages current test case context for the framework with thread-safe storage
 */

package com.novac.naf.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the current test case context across the framework with enhanced thread safety
 */
public class TestCaseManager {
    private static final Logger logger = LoggerFactory.getLogger(TestCaseManager.class);
    
    // Thread-local storage for test case ID to ensure thread safety
    private static final ThreadLocal<String> currentTestCaseId = new ThreadLocal<>();
    
    // Global fallback for single-threaded scenarios
    private static volatile String globalTestCaseId;
    
    /**
     * Sets the current test case ID with thread-safe storage
     * 
     * @param testCaseId The test case ID to set
     */
    public static void setCurrentTestCaseId(String testCaseId) {
        currentTestCaseId.set(testCaseId);
        globalTestCaseId = testCaseId;
        logger.info("Current test case ID set to: {} (Thread: {})", testCaseId, Thread.currentThread().getName());
    }
    
    /**
     * Gets the current test case ID with fallback mechanisms
     * 
     * @return The current test case ID or null if not set
     */
    public static String getCurrentTestCaseId() {
        String threadLocalId = currentTestCaseId.get();
        if (threadLocalId != null) {
            logger.debug("Retrieved test case ID from thread-local: {}", threadLocalId);
            return threadLocalId;
        }
        
        if (globalTestCaseId != null) {
            logger.debug("Retrieved test case ID from global fallback: {}", globalTestCaseId);
            return globalTestCaseId;
        }
        
        logger.warn("No test case ID available in either thread-local or global storage");
        return null;
    }
    
    /**
     * Clears the current test case ID from both thread-local and global storage
     */
    public static void clearCurrentTestCaseId() {
        String currentId = getCurrentTestCaseId();
        logger.debug("Clearing current test case ID: {}", currentId);
        currentTestCaseId.remove();
        globalTestCaseId = null;
    }
    
    /**
     * Checks if a test case ID is currently set
     * 
     * @return true if a test case ID is available, false otherwise
     */
    public static boolean hasCurrentTestCaseId() {
        return getCurrentTestCaseId() != null;
    }
    
    /**
     * Validates that a test case ID is set, throwing an exception if not
     * 
     * @throws IllegalStateException if no test case ID is set
     */
    public static void validateTestCaseIdSet() {
        if (!hasCurrentTestCaseId()) {
            throw new IllegalStateException("No test case ID is currently set. Ensure TestCaseManager.setCurrentTestCaseId() is called before processing test data.");
        }
    }
}
