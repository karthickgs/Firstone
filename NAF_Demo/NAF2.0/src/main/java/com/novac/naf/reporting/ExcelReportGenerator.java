/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: ExcelReportGenerator.java
 * Description: Generates Excel reports for test execution results
 */

package com.novac.naf.reporting;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Generates Excel reports for test execution results
 */
public class ExcelReportGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ExcelReportGenerator.class);
    private String reportFilePath;
    
    /**
     * Default constructor
     */
    public ExcelReportGenerator() {
        this.reportFilePath = null;
    }
    
    /**
     * Constructor that accepts a report file path
     * 
     * @param reportFilePath Path where the Excel report will be saved
     */
    public ExcelReportGenerator(String reportFilePath) {
        this.reportFilePath = reportFilePath;
        logger.info("Excel report generator initialized with path: {}", reportFilePath);
    }
    
    /**
     * Generates a report with the given test results
     * This method is called from ReportManager for backward compatibility
     * 
     * @param testResults Map containing test execution results
     */
    public void generateReport(Map<String, Object> testResults) {
        try {
            logger.info("Generating simple Excel report at: {}", reportFilePath);
            
            if (reportFilePath == null || reportFilePath.isEmpty()) {
                logger.error("Report file path not specified");
                return;
            }
            
            // Create parent directory if it doesn't exist
            File reportFile = new File(reportFilePath);
            File parentDir = reportFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
                logger.info("Created directory: {}", parentDir.getAbsolutePath());
            }
            
            // Create a simple Excel report with test results
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Test Results");
                
                // Create header row
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Test ID");
                headerRow.createCell(1).setCellValue("Result");
                
                // Add data rows
                int rowNum = 1;
                for (Map.Entry<String, Object> entry : testResults.entrySet()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(entry.getKey());
                    row.createCell(1).setCellValue(entry.getValue().toString());
                }
                
                // Auto-size columns for better readability
                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                
                // Write the workbook to the file
                try (FileOutputStream fileOut = new FileOutputStream(reportFilePath)) {
                    workbook.write(fileOut);
                    logger.info("Excel report generated successfully at: {}", reportFilePath);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error generating Excel report: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Generates an Excel report asynchronously
     * @param reportPath Path to save the Excel report
     * @param executionData Map containing test execution data
     */
    public static CompletableFuture<Void> generateExcelReportAsync(String reportPath, Map<String, Object> executionData) {
        return CompletableFuture.runAsync(() -> {
            try {
                generateExcelReport(reportPath, executionData);
            } catch (Exception e) {
                // Log error but don't throw exception to prevent failing test run
                logger.error("Error generating Excel report: {}", e.getMessage(), e);
            }
        });
    }
    
    /**
     * Generates an Excel report with test execution data
     * @param reportPath Path to save the Excel report
     * @param executionData Map containing test execution data
     */
    public static void generateExcelReport(String reportPath, Map<String, Object> executionData) {
        logger.info("Generating Excel report at: {}", reportPath);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create Summary sheet
            Sheet summarySheet = workbook.createSheet("Summary");
            createSummarySheet(workbook, summarySheet, executionData);
            
            // Create Details sheet
            Sheet detailsSheet = workbook.createSheet("Details");
            createDetailsSheet(workbook, detailsSheet, executionData);
            
            // Auto-size columns for better readability
            autoSizeColumns(summarySheet);
            autoSizeColumns(detailsSheet);
            
            // Get project name and timestamp for standardized file naming
            String projectName = (String) executionData.get("projectName");
            String executionDate = (String) executionData.get("executionDate");
            
            // Create standardized file name with project name and timestamp
            String excelFileName = projectName.replaceAll("\\s+", "_") + "_Report_" + executionDate + ".xlsx";
            
            // Create the full file path
            String excelFilePath = Paths.get(reportPath, excelFileName).toString();
            
            // Ensure the directory exists
            File reportDir = new File(reportPath);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
                logger.info("Created report directory: {}", reportPath);
            }
            
            // Write workbook to file
            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
                workbook.write(fileOut);
                logger.info("Excel report generated successfully: {}", excelFilePath);
            }
        } catch (IOException e) {
            logger.error("Error creating Excel report: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Creates the Summary sheet with test execution summary data
     */
    @SuppressWarnings("unchecked")
    private static void createSummarySheet(Workbook workbook, Sheet sheet, Map<String, Object> executionData) {
        // Create header styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle passStyle = createPassStyle(workbook);
        CellStyle failStyle = createFailStyle(workbook);
        CellStyle defaultStyle = createDefaultStyle(workbook);
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Test ID", "Scenario Name", "Start Time", "End Time", "Status", "Total Steps", "Passed Steps", "Failed Steps", "Duration (sec)"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Add data rows
        List<Map<String, Object>> testCases = (List<Map<String, Object>>) executionData.get("testCases");
        int rowNum = 1;
        
        if (testCases != null && !testCases.isEmpty()) {
            logger.info("Creating summary sheet with {} test cases", testCases.size());
            
            for (Map<String, Object> testCase : testCases) {
                Row row = sheet.createRow(rowNum++);
                
                // Test ID
                Cell idCell = row.createCell(0);
                idCell.setCellValue((String) testCase.get("testId"));
                idCell.setCellStyle(defaultStyle);
                
                // Scenario Name
                Cell nameCell = row.createCell(1);
                nameCell.setCellValue((String) testCase.get("description"));
                nameCell.setCellStyle(defaultStyle);
                
                // Start Time
                Cell startCell = row.createCell(2);
                String startTime = (String) testCase.get("startTime");
                startCell.setCellValue(startTime != null ? startTime : "N/A");
                startCell.setCellStyle(defaultStyle);
                
                // End Time
                Cell endCell = row.createCell(3);
                String endTime = (String) testCase.get("endTime");
                endCell.setCellValue(endTime != null ? endTime : "N/A");
                endCell.setCellStyle(defaultStyle);
                
                // Status
                Cell statusCell = row.createCell(4);
                String status = (String) testCase.get("status");
                statusCell.setCellValue(status != null ? status : "PENDING");
                statusCell.setCellStyle("PASS".equals(status) ? passStyle : failStyle);
                
                // Total Steps
                Cell totalStepsCell = row.createCell(5);
                Integer totalSteps = (Integer) testCase.get("totalSteps");
                totalStepsCell.setCellValue(totalSteps != null ? totalSteps : 0);
                totalStepsCell.setCellStyle(defaultStyle);
                
                // Passed Steps
                Cell passedStepsCell = row.createCell(6);
                Integer passedSteps = (Integer) testCase.get("passedSteps");
                passedStepsCell.setCellValue(passedSteps != null ? passedSteps : 0);
                passedStepsCell.setCellStyle(defaultStyle);
                
                // Failed Steps
                Cell failedStepsCell = row.createCell(7);
                Integer failedSteps = (Integer) testCase.get("failedSteps");
                failedStepsCell.setCellValue(failedSteps != null ? failedSteps : 0);
                failedStepsCell.setCellStyle(defaultStyle);
                
                // Duration
                Cell durationCell = row.createCell(8);
                Double durationSec = (Double) testCase.get("durationSeconds");
                durationCell.setCellValue(durationSec != null ? durationSec : 0.0);
                durationCell.setCellStyle(defaultStyle);
            }
        } else {
            logger.warn("No test cases found for summary sheet");
        }
        
        // Add project summary at the bottom
        rowNum += 2;
        Row summaryLabelRow = sheet.createRow(rowNum++);
        Cell summaryLabelCell = summaryLabelRow.createCell(0);
        summaryLabelCell.setCellValue("Project Summary");
        summaryLabelCell.setCellStyle(headerStyle);
        
        // Project name
        Row projectRow = sheet.createRow(rowNum++);
        projectRow.createCell(0).setCellValue("Project Name");
        projectRow.getCell(0).setCellStyle(headerStyle);
        Cell projectNameCell = projectRow.createCell(1);
        projectNameCell.setCellValue((String) executionData.get("projectName"));
        projectNameCell.setCellStyle(defaultStyle);
        
        // Execution date
        Row dateRow = sheet.createRow(rowNum++);
        dateRow.createCell(0).setCellValue("Execution Date");
        dateRow.getCell(0).setCellStyle(headerStyle);
        Cell dateCell = dateRow.createCell(1);
        dateCell.setCellValue((String) executionData.get("executionDate"));
        dateCell.setCellStyle(defaultStyle);
        
        // Overall summary
        Row summaryRow = sheet.createRow(rowNum++);
        summaryRow.createCell(0).setCellValue("Tests Executed");
        summaryRow.getCell(0).setCellStyle(headerStyle);
        summaryRow.createCell(1).setCellValue((Integer) executionData.get("totalTests"));
        summaryRow.getCell(1).setCellStyle(defaultStyle);
        
        Row passedRow = sheet.createRow(rowNum++);
        passedRow.createCell(0).setCellValue("Tests Passed");
        passedRow.getCell(0).setCellStyle(headerStyle);
        passedRow.createCell(1).setCellValue((Integer) executionData.get("passedTests"));
        passedRow.getCell(1).setCellStyle(defaultStyle);
        
        Row failedRow = sheet.createRow(rowNum++);
        failedRow.createCell(0).setCellValue("Tests Failed");
        failedRow.getCell(0).setCellStyle(headerStyle);
        failedRow.createCell(1).setCellValue((Integer) executionData.get("failedTests"));
        failedRow.getCell(1).setCellStyle(defaultStyle);
    }

    /**
     * Creates the Details sheet with test execution step data
     */
    @SuppressWarnings("unchecked")
    private static void createDetailsSheet(Workbook workbook, Sheet sheet, Map<String, Object> executionData) {
        // Create header styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle passStyle = createPassStyle(workbook);
        CellStyle failStyle = createFailStyle(workbook);
        CellStyle infoStyle = createInfoStyle(workbook);
        CellStyle defaultStyle = createDefaultStyle(workbook);
        CellStyle hyperlinkStyle = createHyperlinkStyle(workbook);
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Test ID", "Step #", "Step Description", "Status", "Timestamp", "Details/Error", "Screenshot Path"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Create hyperlink helper
        CreationHelper createHelper = workbook.getCreationHelper();
        
        // Add data rows
        List<Map<String, Object>> testCases = (List<Map<String, Object>>) executionData.get("testCases");
        int rowNum = 1;
        
        if (testCases != null && !testCases.isEmpty()) {
            logger.info("Creating details sheet with {} test cases", testCases.size());
            
            for (Map<String, Object> testCase : testCases) {
                String testId = (String) testCase.get("testId");
                List<Map<String, Object>> steps = (List<Map<String, Object>>) testCase.get("steps");
                
                if (steps != null && !steps.isEmpty()) {
                    logger.debug("Processing {} steps for test case {}", steps.size(), testId);
                    
                    for (Map<String, Object> step : steps) {
                        Row row = sheet.createRow(rowNum++);
                        
                        // Test ID
                        Cell idCell = row.createCell(0);
                        idCell.setCellValue(testId);
                        idCell.setCellStyle(defaultStyle);
                        
                        // Step #
                        Cell stepNumCell = row.createCell(1);
                        Integer stepNumber = (Integer) step.get("stepNumber");
                        stepNumCell.setCellValue(stepNumber != null ? stepNumber : 0);
                        stepNumCell.setCellStyle(defaultStyle);
                        
                        // Step Description
                        Cell descCell = row.createCell(2);
                        String description = (String) step.get("description");
                        descCell.setCellValue(description != null ? description : "N/A");
                        descCell.setCellStyle(defaultStyle);
                        
                        // Status
                        Cell statusCell = row.createCell(3);
                        String status = (String) step.get("status");
                        statusCell.setCellValue(status != null ? status : "UNKNOWN");
                        
                        // Apply style based on status
                        if ("PASS".equals(status)) {
                            statusCell.setCellStyle(passStyle);
                        } else if ("FAIL".equals(status)) {
                            statusCell.setCellStyle(failStyle);
                        } else if ("INFO".equals(status)) {
                            statusCell.setCellStyle(infoStyle);
                        } else {
                            statusCell.setCellStyle(defaultStyle);
                        }
                        
                        // Timestamp
                        Cell timeCell = row.createCell(4);
                        String timestamp = (String) step.get("timestamp");
                        timeCell.setCellValue(timestamp != null ? timestamp : "N/A");
                        timeCell.setCellStyle(defaultStyle);
                        
                        // Details/Error
                        Cell detailsCell = row.createCell(5);
                        String details = (String) step.get("details");
                        detailsCell.setCellValue(details != null ? details : "N/A");
                        detailsCell.setCellStyle(defaultStyle);
                        
                        // Screenshot Path
                        Cell screenshotCell = row.createCell(6);
                        String screenshotPath = (String) step.get("screenshotPath");
                        if (screenshotPath != null && !screenshotPath.trim().isEmpty()) {
                            screenshotCell.setCellValue(screenshotPath);
                            
                            // Create a file hyperlink to the screenshot
                            try {
                                Hyperlink link = createHelper.createHyperlink(HyperlinkType.FILE);
                                link.setAddress(screenshotPath.replace('\\', '/'));
                                screenshotCell.setHyperlink(link);
                                screenshotCell.setCellStyle(hyperlinkStyle);
                                
                                logger.debug("Created hyperlink for screenshot: {}", screenshotPath);
                            } catch (Exception e) {
                                logger.error("Error creating hyperlink for screenshot {}: {}", screenshotPath, e.getMessage());
                                screenshotCell.setCellValue(screenshotPath + " (Link error)");
                                screenshotCell.setCellStyle(defaultStyle);
                            }
                        } else {
                            screenshotCell.setCellValue("N/A");
                            screenshotCell.setCellStyle(defaultStyle);
                        }
                    }
                    
                    // Add a blank row between test cases for better readability
                    if (rowNum < 65535) { // Excel row limit check
                        rowNum++;
                    }
                } else {
                    logger.warn("No steps found for test case: {}", testId);
                    
                    // Add a row indicating no steps
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(testId);
                    row.createCell(1).setCellValue(0);
                    row.createCell(2).setCellValue("No steps executed");
                    row.createCell(3).setCellValue("INFO");
                    row.createCell(4).setCellValue("N/A");
                    row.createCell(5).setCellValue("Test case had no steps");
                    row.createCell(6).setCellValue("N/A");
                    
                    for (int i = 0; i < 7; i++) {
                        row.getCell(i).setCellStyle(defaultStyle);
                    }
                }
            }
        } else {
            logger.warn("No test cases found for details sheet");
            
            // Add a row indicating no data
            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue("N/A");
            row.createCell(1).setCellValue(0);
            row.createCell(2).setCellValue("No test cases executed");
            row.createCell(3).setCellValue("INFO");
            row.createCell(4).setCellValue("N/A");
            row.createCell(5).setCellValue("No test execution data available");
            row.createCell(6).setCellValue("N/A");
            
            for (int i = 0; i < 7; i++) {
                row.getCell(i).setCellStyle(defaultStyle);
            }
        }
        
        logger.info("Details sheet created with {} data rows", rowNum - 1);
    }
    
    /**
     * Auto-sizes all columns in the sheet for better readability
     */
    private static void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 15; i++) { // Assuming max 15 columns
            try {
                sheet.autoSizeColumn(i);
            } catch (Exception e) {
                // Ignore exceptions during auto-sizing
                logger.debug("Error auto-sizing column: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Creates a header cell style
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    /**
     * Creates a pass cell style
     */
    private static CellStyle createPassStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    /**
     * Creates a fail cell style
     */
    private static CellStyle createFailStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    /**
     * Creates an info cell style
     */
    private static CellStyle createInfoStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    /**
     * Creates a default cell style
     */
    private static CellStyle createDefaultStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(true);
        return style;
    }
    
    /**
     * Creates a hyperlink cell style
     */
    private static CellStyle createHyperlinkStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }
}
