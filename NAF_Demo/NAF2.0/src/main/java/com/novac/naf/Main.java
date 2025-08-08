
/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: Main.java
 * Description: Main entry point for the Novac Automation Framework that initializes and executes the test run
 * Enhanced with batch execution optimization and centralized step management
 */

package com.novac.naf;

import com.novac.naf.config.ConfigLoader;
import com.novac.naf.datamanager.TestDataUtility;
import com.novac.naf.runner.RunManager;
import com.novac.naf.reporting.ReportManager;
import com.novac.naf.webdriver.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for Novac Automation Framework
 * Enhanced with batch execution and centralized step management
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        try {
            logger.info("Starting Novac Automation Framework with batch execution optimization...");
            System.out.println("Starting Novac Automation Framework with batch execution optimization...");
            
            // Load configuration from RunManager.xlsx
            ConfigLoader configLoader = new ConfigLoader("./TestData/RunManager.xlsx");
            
            // Initialize TestDataUtility with configLoader - CRITICAL for framework operation
            TestDataUtility.initialize(configLoader);
            logger.info("TestDataUtility initialized successfully");
            
            // Initialize reporting with CENTRALIZED timestamp (ReportManager will generate its own ONCE)
            // DO NOT create timestamp here - let ReportManager handle it completely
            String baseReportPath = "./Reports";
            ReportManager.initializeReporting(baseReportPath, configLoader.getProjectName());
            
            // Initialize WebDriver manager with configuration
            WebDriverManager.initialize(configLoader);
            
            // Set screenshot mode from configuration
            ReportManager.setScreenshotMode(configLoader.getScreenshotMode());
            logger.info("Screenshot mode set to: {}", configLoader.getScreenshotMode());
                    
            logger.info("Configuration loaded successfully.");
            System.out.println("Configuration loaded successfully.");
            
            // Initialize and execute test run with batch optimization
            RunManager runManager = new RunManager(configLoader);
            
            logger.info("Starting batch execution of test cases...");
            System.out.println("Starting batch execution of test cases...");
            
            runManager.executeTestRun();
            
            logger.info("Batch test execution completed.");
            System.out.println("Batch test execution completed.");
            
            // Finalize reporting
            ReportManager.finalizeReporting();
            
            // Close TestDataUtility resources
            TestDataUtility.close();
            
            // Use getCurrentReportFolder to get consistent path
            String finalReportPath = ReportManager.getCurrentReportFolder();
            logger.info("Reports generated at: " + finalReportPath);
            System.out.println("Reports generated at: " + finalReportPath);
            System.out.println("NAF execution completed successfully with batch optimization.");
            
        } catch (Exception e) {
            logger.error("Error in NAF execution: " + e.getMessage(), e);
            System.err.println("Error in NAF execution: " + e.getMessage());
            e.printStackTrace();
            
            // Ensure batch session is ended in case of error
            try {
                WebDriverManager.endBatchSession();
                logger.info("Emergency batch session cleanup completed");
            } catch (Exception sessionEx) {
                logger.error("Error during emergency batch session cleanup: " + sessionEx.getMessage());
            }
            
            // Close TestDataUtility resources in case of error
            try {
                TestDataUtility.close();
            } catch (Exception closeEx) {
                logger.error("Error closing TestDataUtility: " + closeEx.getMessage());
            }
            
            System.exit(1);  // Exit with error status
        }
    }
}
