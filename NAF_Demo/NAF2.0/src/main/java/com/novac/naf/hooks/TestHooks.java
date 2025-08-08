
/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: TestHooks.java
 * Description: Test hooks for managing TestContext lifecycle and framework setup/teardown
 */

package com.novac.naf.hooks;

import com.novac.naf.datamanager.TestContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hooks for managing test execution lifecycle and context management
 */
public class TestHooks {
    private static final Logger logger = LoggerFactory.getLogger(TestHooks.class);
    
    /**
     * Executed once before all tests in the test suite
     * Initializes the TestContext and framework components
     */
    @BeforeAll
    public static void beforeTestSuite() {
        logger.info("=== Starting Test Suite - Initializing Framework ===");
        
        try {
            // Initialize TestContext for the test suite
            TestContext.clearAll(); // Ensure clean state
            logger.info("TestContext initialized and cleared for new test suite");
            
            // Log framework readiness
            logger.info("Enhanced test data framework initialized with static and dynamic data support");
            logger.info("Supported data reference formats:");
            logger.info("  Static (current test): SheetName.ColumnName");
            logger.info("  Static (cross-test): SheetName.TestCaseID.ColumnName");
            logger.info("  Dynamic: ${{key}} or ${{GLOBAL.key}}");
            logger.info("  Legacy: {{{{TD.SheetName.ColumnName}}}}");
            
        } catch (Exception e) {
            logger.error("Failed to initialize framework in beforeTestSuite: {}", e.getMessage(), e);
            throw new RuntimeException("Framework initialization failed", e);
        }
        
        logger.info("=== Framework Initialization Complete ===");
    }
    
    /**
     * Executed before each individual test case
     * Clears test-level context while preserving global data
     */
    @BeforeEach
    public void beforeEachTest() {
        logger.debug("--- Starting Individual Test - Clearing Test-Level Context ---");
        
        try {
            // Clear test-level context data (preserving global data)
            TestContext.clear();
            logger.debug("Test-level context cleared, global data preserved");
            
        } catch (Exception e) {
            logger.error("Failed to clear test context in beforeEachTest: {}", e.getMessage(), e);
            // Don't throw exception as this shouldn't fail the test
        }
    }
    
    /**
     * Executed after each individual test case
     * Logs context state for debugging purposes
     */
    @AfterEach
    public void afterEachTest() {
        logger.debug("--- Test Complete - Context State ---");
        
        try {
            int contextSize = TestContext.size();
            logger.debug("Current context size: {} entries", contextSize);
            
            if (logger.isDebugEnabled() && contextSize > 0) {
                String[] keys = TestContext.getAllKeys();
                logger.debug("Active context keys: {}", String.join(", ", keys));
            }
            
        } catch (Exception e) {
            logger.error("Error logging context state in afterEachTest: {}", e.getMessage(), e);
        }
        
        logger.debug("--- Test Context State Logged ---");
    }
    
    /**
     * Executed once after all tests in the test suite
     * Performs final cleanup and memory leak prevention
     */
    @AfterAll
    public static void afterTestSuite() {
        logger.info("=== Test Suite Complete - Final Cleanup ===");
        
        try {
            // Log final statistics
            int finalContextSize = TestContext.size();
            logger.info("Final context size before cleanup: {} entries", finalContextSize);
            
            // Perform complete cleanup
            TestContext.clearAll();
            logger.info("All test context data cleared - memory cleanup complete");
            
            // Log framework shutdown
            logger.info("Enhanced test data framework shutdown complete");
            
        } catch (Exception e) {
            logger.error("Error during framework cleanup in afterTestSuite: {}", e.getMessage(), e);
        }
        
        logger.info("=== Framework Shutdown Complete ===");
    }
    
    /**
     * Emergency cleanup method that can be called manually if needed
     * Useful for error recovery scenarios
     */
    public static void emergencyCleanup() {
        logger.warn("=== Emergency Cleanup Initiated ===");
        
        try {
            TestContext.clearAll();
            logger.warn("Emergency cleanup completed - all context data cleared");
            
        } catch (Exception e) {
            logger.error("Failed emergency cleanup: {}", e.getMessage(), e);
        }
        
        logger.warn("=== Emergency Cleanup Complete ===");
    }
}
