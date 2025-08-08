
/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: CucumberLauncher.java
 * Description: Manages Cucumber feature execution with proper scenario filtering
 */

package com.novac.naf.runner;

import io.cucumber.core.cli.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages Cucumber feature execution with enhanced scenario filtering
 */
public class CucumberLauncher {
    private static final Logger logger = LoggerFactory.getLogger(CucumberLauncher.class);

    /**
     * Runs a specific feature file with tag filtering for specific test cases
     * This is the primary method that ensures only Y-flagged scenarios execute
     * 
     * @param featurePath Path to the feature file
     * @param testCaseIds List of test case IDs to execute (will be converted to tag filters)
     * @return true if execution was successful, false otherwise
     */
    public boolean runFeatureWithTags(String featurePath, List<String> testCaseIds) {
        // Ensure path is trimmed
        featurePath = featurePath != null ? featurePath.trim() : null;
        
        logger.info("Running feature: {} with test cases: {}", featurePath, testCaseIds);
        
        // Validate inputs
        if (testCaseIds == null || testCaseIds.isEmpty()) {
            logger.error("No test case IDs provided for filtering");
            return false;
        }
        
        try {
            // Verify feature file exists
            File featureFile = new File(featurePath);
            if (!featureFile.exists()) {
                logger.error("Feature file not found: {}", featurePath);
                logger.error("Absolute path: {}", featureFile.getAbsolutePath());
                return false;
            }
            
            // Use canonical path for feature file to normalize it
            String normalizedPath = featureFile.getCanonicalPath().replace('\\', '/');
            logger.debug("Using normalized feature path: {}", normalizedPath);
            
            // Extract feature file name for logging purposes
            String featureFileName = featureFile.getName();
            logger.info("Running feature file: {} with filtered test cases: {}", featureFileName, testCaseIds);
            
            // Set the test case context for each test case that will be executed
            for (String testCaseId : testCaseIds) {
                TestCaseManager.setCurrentTestCaseId(testCaseId);
                logger.info("Set test case context: {}", testCaseId);
            }
            
            // Create tag filter from test case IDs - this is the core filtering mechanism
            String tagFilter = createTagFilter(testCaseIds);
            logger.info("Created tag filter for execution: {}", tagFilter);
            
            // Invoke Cucumber programmatically using the CLI entry point with tag filtering
            boolean success = runWithCucumberCli(normalizedPath, tagFilter);
            
            logger.info("Feature execution completed: {} - Success: {}", featurePath, success);
            
            return success;
        } catch (Exception e) {
            logger.error("Error running feature file with tags: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Runs a specific feature file without filtering (legacy method)
     * NOTE: This should only be used for backward compatibility
     * 
     * @param featurePath Path to the feature file
     * @return true if execution was successful, false otherwise
     */
    @Deprecated
    public boolean runFeature(String featurePath) {
        logger.warn("Using deprecated runFeature method without filtering. Consider using runFeatureWithTags for proper scenario filtering.");
        
        // Ensure path is trimmed
        featurePath = featurePath != null ? featurePath.trim() : null;
        
        logger.info("Running feature: {}", featurePath);
        
        try {
            // Verify feature file exists
            File featureFile = new File(featurePath);
            if (!featureFile.exists()) {
                logger.error("Feature file not found: {}", featurePath);
                return false;
            }
            
            // Use canonical path for feature file to normalize it
            String normalizedPath = featureFile.getCanonicalPath().replace('\\', '/');
            logger.debug("Using normalized feature path: {}", normalizedPath);
            
            // Invoke Cucumber programmatically using the CLI entry point
            boolean success = runWithCucumberCli(normalizedPath, null);
            
            logger.info("Feature execution completed: {} - Success: {}", featurePath, success);
            
            return success;
        } catch (Exception e) {
            logger.error("Error running feature file: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Creates a tag filter string from test case IDs for Cucumber execution
     * This method implements the core scenario filtering logic
     * 
     * @param testCaseIds List of test case IDs
     * @return Tag filter string in Cucumber format (e.g., "@STATIMCM-TC-459 or @STATIMCM-TC-460")
     */
    private String createTagFilter(List<String> testCaseIds) {
        if (testCaseIds == null || testCaseIds.isEmpty()) {
            logger.warn("No test case IDs provided for tag filtering");
            return null;
        }
        
        // Create OR condition for multiple test case IDs
        // This ensures only scenarios with matching tags execute
        StringBuilder tagFilter = new StringBuilder();
        for (int i = 0; i < testCaseIds.size(); i++) {
            String testCaseId = testCaseIds.get(i).trim();
            
            // Add @ prefix if not already present
            if (!testCaseId.startsWith("@")) {
                testCaseId = "@" + testCaseId;
            }
            
            tagFilter.append(testCaseId);
            
            // Add " or " between test case IDs (except for the last one)
            if (i < testCaseIds.size() - 1) {
                tagFilter.append(" or ");
            }
        }
        
        String result = tagFilter.toString();
        logger.info("Generated tag filter for scenario filtering: {}", result);
        return result;
    }
    
    /**
     * Runs Cucumber with the specified feature path using Cucumber CLI with enhanced tag filtering
     * 
     * @param normalizedPath Normalized path to the feature file
     * @param tagFilter Tag filter for scenario selection (enforces Y-flagged scenarios only)
     * @return true if execution was successful, false otherwise
     */
    private boolean runWithCucumberCli(String normalizedPath, String tagFilter) {
        try {
            // Prepare Cucumber CLI arguments
            List<String> args = new ArrayList<>();
            
            // Add glue path - specifying exactly where the step definitions are located
            args.add("--glue");
            args.add("com.novac.naf.steps");
            
            // Add plugins
            args.add("--plugin");
            args.add("pretty");
            args.add("--plugin");
            args.add("html:target/cucumber-reports");
            
            // Add monochrome option
            args.add("--monochrome");
            
            // CRITICAL: Add tag filter if provided (this enforces scenario filtering)
            if (tagFilter != null && !tagFilter.trim().isEmpty()) {
                args.add("--tags");
                args.add(tagFilter);
                logger.info("Applied tag filter for scenario filtering: {}", tagFilter);
            } else {
                logger.warn("No tag filter applied - ALL scenarios in the feature file will run. This may not respect RunManager Y/N flags.");
            }
            
            // Add the actual feature file path as the last argument
            args.add(normalizedPath);
            
            // Log the Cucumber options being used
            logger.info("Executing Cucumber with feature path: {}", normalizedPath);
            if (tagFilter != null) {
                logger.info("Using tag filter to enforce scenario filtering: {}", tagFilter);
            }
            
            // Convert to string array for Cucumber CLI
            String[] cucumberArgs = args.toArray(new String[0]);
            
            // Log all arguments for debugging
            logger.debug("Cucumber CLI arguments: {}", String.join(" ", args));
            
            // Run Cucumber and capture exit status
            byte exitStatus = Main.run(cucumberArgs, Thread.currentThread().getContextClassLoader());
            
            // Exit status 0 means success
            boolean success = (exitStatus == 0);
            
            if (!success) {
                logger.error("Test execution failed with exit status: {}", exitStatus);
                if (tagFilter != null) {
                    logger.error("Tag filter was applied: {}. Please ensure scenarios are properly tagged with the test case IDs.", tagFilter);
                }
            } else {
                logger.info("Test execution completed successfully with tag filtering");
            }
            
            return success;
        } catch (Exception e) {
            logger.error("Error executing Cucumber: {}", e.getMessage(), e);
            return false;
        }
    }
}
