/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: XmlJsonReportGenerator.java
 * Description: Generates XML (JUnit) and JSON (Cucumber) reports for QMetry integration
 */

package com.novac.naf.reporting;

import com.novac.naf.config.ConfigLoader;
import com.novac.naf.qmetry.QMetryClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates XML and JSON reports for QMetry integration
 */
public class XmlJsonReportGenerator {
    private static final Logger logger = LoggerFactory.getLogger(XmlJsonReportGenerator.class);
    private final ConfigLoader configLoader;
    private final String reportFormat;
    private final List<TestResult> testResults = new ArrayList<>();
    private final QMetryClient qmetryClient;
    private String generatedJsonFilePath = null; // Track JSON file for QMetry upload
    
    public XmlJsonReportGenerator(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        this.reportFormat = configLoader.getConfigValue("ResultUploadFormat");
        this.qmetryClient = new QMetryClient(configLoader);
        logger.info("XmlJsonReportGenerator initialized with format: {}", reportFormat);
    }
    
    /**
     * Adds a test result to be included in the final report
     */
    public void addTestResult(String testCaseId, String featureFile, String status, 
                             long executionTime, String errorMessage, List<String> steps) {
        TestResult result = new TestResult(testCaseId, featureFile, status, executionTime, errorMessage, steps);
        testResults.add(result);
        logger.debug("Added test result: {} - {} (Total results: {})", testCaseId, status, testResults.size());
        
        // Log the test result details for debugging
        logger.debug("Test result details - ID: {}, Feature: {}, Status: {}, Time: {}ms, Error: {}", 
            testCaseId, featureFile, status, executionTime, errorMessage);
    }
    
    /**
     * Generates the report based on the configured format and uploads to QMetry if enabled
     */
    public String generateReport() {
        if (reportFormat == null || "None".equalsIgnoreCase(reportFormat)) {
            logger.info("ResultUploadFormat is None - skipping XML/JSON report generation");
            return null;
        }
        
        // Verify we have test results to report
        if (testResults.isEmpty()) {
            logger.warn("No test results available for report generation");
            return null;
        }
        
        logger.info("Generating {} report with {} test results", reportFormat, testResults.size());
        
        try {
            String reportPath = null;
            
            // Always generate JSON for QMetry upload with proper naming convention
            generatedJsonFilePath = generateJsonReportForQMetry();
            logger.info("Generated JSON report for QMetry upload: {}", generatedJsonFilePath);
            
            if ("XML".equalsIgnoreCase(reportFormat)) {
                reportPath = generateXmlReport();
                logger.info("Generated XML report for local use: {}", reportPath);
            } else if ("JSON".equalsIgnoreCase(reportFormat)) {
                reportPath = generatedJsonFilePath; // Use QMetry JSON file for local use too
                logger.info("Using JSON report for both local use and QMetry upload: {}", reportPath);
            } else {
                logger.warn("Unknown ResultUploadFormat: {}. Valid values are XML, JSON, or None", reportFormat);
                return null;
            }
            
            logger.info("Successfully generated {} report: {}", reportFormat, reportPath);
            
            // Verify the generated file exists and has content
            if (reportPath != null) {
                File reportFile = new File(reportPath);
                if (reportFile.exists()) {
                    logger.info("Report file size: {} bytes", reportFile.length());
                    if (reportFile.length() == 0) {
                        logger.error("Generated report file is empty!");
                        return null;
                    }
                } else {
                    logger.error("Generated report file does not exist: {}", reportPath);
                    return null;
                }
            }
            
            // Upload JSON file to QMetry if enabled (always use JSON for QMetry)
            if (generatedJsonFilePath != null && qmetryClient.isQMetryEnabled()) {
                File jsonFile = new File(generatedJsonFilePath);
                if (jsonFile.exists()) {
                    logger.info("Uploading JSON file to QMetry: {}", jsonFile.getName());
                    logger.info("JSON file size: {} bytes", jsonFile.length());
                    
                    boolean uploaded = qmetryClient.uploadResultsFile(jsonFile);
                    if (uploaded) {
                        logger.info("Successfully uploaded JSON report to QMetry");
                    } else {
                        logger.warn("Failed to upload JSON report to QMetry");
                    }
                } else {
                    logger.error("JSON file for QMetry upload does not exist: {}", generatedJsonFilePath);
                }
            } else {
                logger.info("QMetry upload skipped - either disabled or no JSON file generated");
            }
            
            return reportPath;
            
        } catch (Exception e) {
            logger.error("Error generating {} report: {}", reportFormat, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Generates JUnit XML format report with comprehensive test details
     */
    private String generateXmlReport() throws IOException {
        String reportDir = ReportManager.getReportPath();
        String xmlFilePath = reportDir + "/TestResults.xml";
        
        logger.info("Generating enhanced XML report with {} test results", testResults.size());
        
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<testsuite name=\"NAF Test Suite\" ");
        xml.append("tests=\"").append(testResults.size()).append("\" ");
        xml.append("failures=\"").append(getFailureCount()).append("\" ");
        xml.append("errors=\"0\" ");
        xml.append("time=\"").append(getTotalExecutionTime()).append("\" ");
        xml.append("timestamp=\"").append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\" ");
        xml.append("hostname=\"localhost\" ");
        xml.append("package=\"com.novac.naf.tests\">\n");
        
        // Add properties section for environment info
        xml.append("  <properties>\n");
        xml.append("    <property name=\"framework\" value=\"Novac Automation Framework\"/>\n");
        xml.append("    <property name=\"java.version\" value=\"").append(System.getProperty("java.version")).append("\"/>\n");
        xml.append("    <property name=\"os.name\" value=\"").append(escapeXml(System.getProperty("os.name"))).append("\"/>\n");
        xml.append("    <property name=\"execution.date\" value=\"").append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\"/>\n");
        xml.append("  </properties>\n");
        
        for (TestResult result : testResults) {
            logger.debug("Adding enhanced test case to XML: {} - {}", result.testCaseId, result.status);
            
            xml.append("  <testcase classname=\"").append(escapeXml(result.featureFile)).append("\" ");
            xml.append("name=\"").append(escapeXml(result.testCaseId)).append("\" ");
            xml.append("time=\"").append(result.executionTime / 1000.0).append("\">\n");
            
            // Add system-out section with test steps and execution details
            xml.append("    <system-out><![CDATA[\n");
            xml.append("=== Test Execution Details ===\n");
            xml.append("Test Case ID: ").append(result.testCaseId).append("\n");
            xml.append("Feature File: ").append(result.featureFile).append("\n");
            xml.append("Execution Time: ").append(result.executionTime).append(" ms\n");
            xml.append("Status: ").append(result.status).append("\n");
            xml.append("Execution Timestamp: ").append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n\n");
            
            // Add step-by-step execution log
            xml.append("=== Test Steps Execution ===\n");
            if (result.steps != null && !result.steps.isEmpty()) {
                for (int i = 0; i < result.steps.size(); i++) {
                    xml.append("Step ").append(i + 1).append(": ").append(result.steps.get(i));
                    if ("FAIL".equals(result.status) && i == result.steps.size() - 1) {
                        xml.append(" [FAILED]");
                    } else {
                        xml.append(" [PASSED]");
                    }
                    xml.append("\n");
                }
            } else {
                xml.append("Step 1: Execute test case ").append(result.testCaseId);
                if ("FAIL".equals(result.status)) {
                    xml.append(" [FAILED]");
                } else {
                    xml.append(" [PASSED]");
                }
                xml.append("\n");
            }
            
            xml.append("\n=== Test Environment ===\n");
            xml.append("Browser: Web Driver\n");
            xml.append("Framework: Novac Automation Framework\n");
            xml.append("Test Data: ").append(result.featureFile).append("\n");
            xml.append("]]></system-out>\n");
            
            // Add failure section if test failed
            if ("FAIL".equals(result.status)) {
                xml.append("    <failure message=\"Test Case Failed\" type=\"AssertionError\">\n");
                xml.append("      <![CDATA[\n");
                xml.append("=== Failure Details ===\n");
                xml.append("Test Case: ").append(result.testCaseId).append("\n");
                xml.append("Feature: ").append(result.featureFile).append("\n");
                xml.append("Execution Time: ").append(result.executionTime).append(" ms\n\n");
                
                if (result.errorMessage != null && !result.errorMessage.trim().isEmpty()) {
                    xml.append("Error Message:\n").append(result.errorMessage).append("\n\n");
                }
                
                xml.append("Failed Step Details:\n");
                if (result.steps != null && !result.steps.isEmpty()) {
                    xml.append("Last executed step: ").append(result.steps.get(result.steps.size() - 1)).append("\n");
                    xml.append("Total steps executed: ").append(result.steps.size()).append("\n");
                    
                    xml.append("\nAll executed steps:\n");
                    for (int i = 0; i < result.steps.size(); i++) {
                        xml.append((i + 1)).append(". ").append(result.steps.get(i));
                        if (i == result.steps.size() - 1) {
                            xml.append(" [FAILED - This step caused the failure]");
                        } else {
                            xml.append(" [PASSED]");
                        }
                        xml.append("\n");
                    }
                } else {
                    xml.append("No detailed step information available.\n");
                    xml.append("Test case execution failed during: ").append(result.testCaseId).append("\n");
                }
                
                xml.append("\nRecommendations:\n");
                xml.append("1. Review the failed step execution\n");
                xml.append("2. Check test data and object repository\n");
                xml.append("3. Verify application state and timing\n");
                xml.append("4. Review browser console for JavaScript errors\n");
                xml.append("      ]]>\n");
                xml.append("    </failure>\n");
            }
            
            xml.append("  </testcase>\n");
        }
        
        // Add system-out for overall test suite information
        xml.append("  <system-out><![CDATA[\n");
        xml.append("=== Test Suite Summary ===\n");
        xml.append("Framework: Novac Automation Framework\n");
        xml.append("Total Tests: ").append(testResults.size()).append("\n");
        xml.append("Passed: ").append(testResults.size() - getFailureCount()).append("\n");
        xml.append("Failed: ").append(getFailureCount()).append("\n");
        xml.append("Total Execution Time: ").append(getTotalExecutionTime()).append(" seconds\n");
        xml.append("Report Generated: ").append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n");
        xml.append("]]></system-out>\n");
        xml.append("</testsuite>\n");
        
        // Write to file
        try (FileWriter writer = new FileWriter(xmlFilePath)) {
            writer.write(xml.toString());
            writer.flush();
        }
        
        logger.info("Enhanced XML report written to: {}", xmlFilePath);
        
        // Verify file was written correctly
        File xmlFile = new File(xmlFilePath);
        if (xmlFile.exists() && xmlFile.length() > 0) {
            logger.info("XML file verified - size: {} bytes", xmlFile.length());
        } else {
            logger.error("XML file verification failed - file exists: {}, size: {}", 
                xmlFile.exists(), xmlFile.exists() ? xmlFile.length() : "N/A");
        }
        
        return xmlFilePath;
    }
    
    /**
     * Generates Cucumber JSON format report with proper naming convention for QMetry
     */
    private String generateJsonReportForQMetry() throws IOException {
        String reportDir = ReportManager.getReportPath();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String jsonFilePath = reportDir + "/RunReport_" + timestamp + ".json";
        
        logger.info("Generating JSON report for QMetry with {} test results", testResults.size());
        
        JSONArray features = new JSONArray();
        
        for (TestResult result : testResults) {
            logger.debug("Adding test case to JSON: {} - {}", result.testCaseId, result.status);
            
            JSONObject feature = new JSONObject();
            feature.put("id", result.testCaseId.toLowerCase().replace("-", "_"));
            feature.put("name", "Feature: " + result.featureFile);
            feature.put("description", "");
            feature.put("line", 1);
            feature.put("keyword", "Feature");
            feature.put("uri", "features/" + result.featureFile);
            
            JSONArray elements = new JSONArray();
            JSONObject scenario = new JSONObject();
            scenario.put("id", result.testCaseId.toLowerCase().replace("-", "_") + ";scenario");
            scenario.put("name", "Scenario: " + result.testCaseId);
            scenario.put("description", "");
            scenario.put("line", 3);
            scenario.put("keyword", "Scenario");
            scenario.put("type", "scenario");
            
            // Add tags
            JSONArray tags = new JSONArray();
            JSONObject tag = new JSONObject();
            tag.put("name", "@" + result.testCaseId);
            tag.put("line", 2);
            tags.put(tag);
            scenario.put("tags", tags);
            
            // Add steps
            JSONArray steps = new JSONArray();
            if (result.steps != null && !result.steps.isEmpty()) {
                for (int i = 0; i < result.steps.size(); i++) {
                    JSONObject step = new JSONObject();
                    step.put("name", result.steps.get(i));
                    step.put("line", 4 + i);
                    step.put("keyword", "When ");
                    
                    JSONObject stepResult = new JSONObject();
                    stepResult.put("status", "FAIL".equals(result.status) && i == result.steps.size() - 1 ? "failed" : "passed");
                    stepResult.put("duration", result.executionTime * 1000000); // nanoseconds
                    
                    if ("FAIL".equals(result.status) && i == result.steps.size() - 1 && result.errorMessage != null) {
                        stepResult.put("error_message", result.errorMessage);
                    }
                    
                    step.put("result", stepResult);
                    steps.put(step);
                }
            } else {
                // Default step if no steps recorded
                JSONObject step = new JSONObject();
                step.put("name", "Execute test case " + result.testCaseId);
                step.put("line", 4);
                step.put("keyword", "When ");
                
                JSONObject stepResult = new JSONObject();
                stepResult.put("status", "FAIL".equals(result.status) ? "failed" : "passed");
                stepResult.put("duration", result.executionTime * 1000000);
                
                if ("FAIL".equals(result.status) && result.errorMessage != null) {
                    stepResult.put("error_message", result.errorMessage);
                }
                
                step.put("result", stepResult);
                steps.put(step);
            }
            
            scenario.put("steps", steps);
            elements.put(scenario);
            feature.put("elements", elements);
            features.put(feature);
        }
        
        // Write to file with pretty formatting
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            writer.write(features.toString(2)); // Pretty print with 2 spaces
            writer.flush();
        }
        
        logger.info("JSON report for QMetry written to: {}", jsonFilePath);
        
        // Verify file was written correctly
        File jsonFile = new File(jsonFilePath);
        if (jsonFile.exists() && jsonFile.length() > 0) {
            logger.info("JSON file verified - size: {} bytes", jsonFile.length());
        } else {
            logger.error("JSON file verification failed - file exists: {}, size: {}", 
                jsonFile.exists(), jsonFile.exists() ? jsonFile.length() : "N/A");
        }
        
        return jsonFilePath;
    }
    
    /**
     * Utility method to escape XML special characters
     */
    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
    
    /**
     * Gets the total number of failed tests
     */
    private int getFailureCount() {
        return (int) testResults.stream().filter(r -> "FAIL".equals(r.status)).count();
    }
    
    /**
     * Gets the total execution time in seconds
     */
    private double getTotalExecutionTime() {
        return testResults.stream().mapToLong(r -> r.executionTime).sum() / 1000.0;
    }
    
    /**
     * Gets the generated JSON report file for QMetry upload
     */
    public File getReportFile() {
        if (generatedJsonFilePath != null) {
            File jsonFile = new File(generatedJsonFilePath);
            if (jsonFile.exists()) {
                logger.info("Returning JSON file for QMetry upload: {}", generatedJsonFilePath);
                return jsonFile;
            }
        }
        
        logger.warn("No JSON file available for QMetry upload");
        return null;
    }
    
    /**
     * Inner class to hold test result data
     */
    private static class TestResult {
        final String testCaseId;
        final String featureFile;
        final String status;
        final long executionTime;
        final String errorMessage;
        final List<String> steps;
        
        TestResult(String testCaseId, String featureFile, String status, 
                  long executionTime, String errorMessage, List<String> steps) {
            this.testCaseId = testCaseId;
            this.featureFile = featureFile;
            this.status = status;
            this.executionTime = executionTime;
            this.errorMessage = errorMessage;
            this.steps = steps != null ? new ArrayList<>(steps) : new ArrayList<>();
        }
    }
}
