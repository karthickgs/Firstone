/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: DataManager.java
 * Description: Enhanced data manager supporting cross-test case access and improved caching
 */

package com.novac.naf.datamanager;

import com.novac.naf.config.ConfigLoader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enhanced data manager with cross-test case access and improved caching
 */
public class DataManager {
    private static final Logger logger = LoggerFactory.getLogger(DataManager.class);
    private final ConfigLoader configLoader;
    private Connection dbConnection;
    private static final Pattern DATA_REFERENCE_PATTERN = Pattern.compile("([^.]+)\\.(.+)");
    
    // Enhanced cache for optimizing performance - storing loaded test data with better structure
    private final Map<String, Map<String, String>> testDataCache = new HashMap<>();
    
    public DataManager(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        
        if ("MYSQL".equalsIgnoreCase(configLoader.getDataSource())) {
            initializeDbConnection();
        }
    }
    
    /**
     * Initialize database connection if DB data source is configured
     */
    private void initializeDbConnection() {
        try {
            String dbUrl = configLoader.getDbUrl();
            String dbUser = configLoader.getDbUser();
            String dbPassword = configLoader.getDbPassword();
            
            if (dbUrl == null || dbUrl.isEmpty()) {
                logger.warn("Database URL not configured, skipping DB connection");
                return;
            }
            
            logger.info("Initializing database connection to {}", dbUrl);
            dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            logger.info("Database connection established successfully");
            
        } catch (SQLException e) {
            logger.error("Failed to establish database connection: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Gets the path to the TestData.xlsx file for test data operations
     * 
     * @return Path to TestData.xlsx file
     */
    private String getTestDataExcelPath() {
        String excelPath = configLoader.getConfigValue("ExcelPath");
        if (excelPath == null || excelPath.isEmpty()) {
            throw new RuntimeException("ExcelPath not configured in framework configuration");
        }
        
        // Replace RunManager.xlsx with TestData.xlsx while keeping the same directory
        String testDataPath = excelPath.replace("RunManager.xlsx", "TestData.xlsx");
        logger.debug("Using TestData Excel path: {}", testDataPath);
        return testDataPath;
    }
    
    /**
     * Enhanced method to get test data value supporting cross-test case access
     * 
     * @param testCaseId The test case ID (can be different from current test case)
     * @param sheetName The sheet name
     * @param columnName The column name
     * @return The value
     */
    public String getTestDataValue(String testCaseId, String sheetName, String columnName) {
        logger.debug("Getting test data value for testCase: {}, sheet: {}, column: {}", testCaseId, sheetName, columnName);
        
        // Get all data for the specified test case from the specified sheet
        Map<String, String> testData = getTestDataFromSheet(testCaseId, sheetName);
        
        // Get the specific column value with case-insensitive matching
        String value = getCaseInsensitiveValue(testData, columnName);
        
        if (value == null) {
            logger.error("Column '{}' not found in sheet '{}' for test case {}. Available columns: {}", 
                    columnName, sheetName, testCaseId, testData.keySet());
            throw new RuntimeException("Column '" + columnName + "' not found in sheet '" + sheetName + "' for test case '" + testCaseId + "'");
        }
        
        logger.debug("Retrieved value for {}.{}.{}: '{}'", testCaseId, sheetName, columnName, value);
        return value;
    }
    
    /**
     * Gets test data from Excel or Database using new module-specific sheet logic
     * 
     * @param testCaseId Test case ID
     * @param sheetName Sheet name to get data from
     * @return Map of test data parameters
     */
    public Map<String, String> getTestData(String testCaseId, String sheetName) {
        String dataSource = configLoader.getDataSource();
        
        if ("EXCEL".equalsIgnoreCase(dataSource)) {
            return getTestDataFromSheet(testCaseId, sheetName);
        } else if ("MYSQL".equalsIgnoreCase(dataSource)) {
            return getTestDataFromDatabase(testCaseId);
        } else {
            logger.error("Unsupported data source: {}", dataSource);
            throw new RuntimeException("Unsupported data source: " + dataSource);
        }
    }
    
    /**
     * Process test data reference in format SheetName.ColumnName
     * 
     * @param reference Data reference in format "SheetName.ColumnName"
     * @param testCaseId Current test case ID
     * @return The value from the specified sheet and column
     */
    public String processDataReference(String reference, String testCaseId) {
        logger.info("Processing data reference: '{}' for test case: {}", reference, testCaseId);
        
        // Check if the reference follows SheetName.ColumnName pattern
        Matcher matcher = DATA_REFERENCE_PATTERN.matcher(reference);
        
        if (matcher.matches()) {
            // Extract sheet name and column name
            String sheetName = matcher.group(1);
            String columnName = matcher.group(2);
            
            logger.debug("Parsed reference: Sheet='{}', Column='{}'", sheetName, columnName);
            
            // Get the value from the specified sheet
            String value = getTestDataValue(testCaseId, sheetName, columnName);
            
            logger.info("Retrieved value for '{}': '{}'", reference, value);
            return value;
        } else {
            // If reference doesn't match SheetName.ColumnName format, return as-is
            logger.warn("Reference '{}' doesn't match SheetName.ColumnName format. Returning original value.", reference);
            return reference;
        }
    }
    
    /**
     * Gets value from map with case-insensitive key matching
     * 
     * @param dataMap The data map
     * @param key The key to search for
     * @return The value or null if not found
     */
    private String getCaseInsensitiveValue(Map<String, String> dataMap, String key) {
        // First try exact match
        String value = dataMap.get(key);
        if (value != null) {
            return value;
        }
        
        // Try case-insensitive match
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(key)) {
                logger.debug("Found case-insensitive match for '{}': '{}'", key, entry.getKey());
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * Gets test data from a specific Excel sheet
     * 
     * @param testCaseId Test case ID
     * @param sheetName Sheet name
     * @return Map of test data parameters
     */
    public Map<String, String> getTestDataFromSheet(String testCaseId, String sheetName) {
        // Check if data is already cached
        String cacheKey = testCaseId + ":" + sheetName;
        if (testDataCache.containsKey(cacheKey)) {
            logger.debug("Using cached test data for {} in sheet {}", testCaseId, sheetName);
            return testDataCache.get(cacheKey);
        }
        
        // Not in cache, load from TestData Excel
        String testDataExcelPath = getTestDataExcelPath();
        Map<String, String> testData = new HashMap<>();
        
        logger.info("Retrieving test data for {} from sheet {} in TestData Excel: {}", 
                testCaseId, sheetName, testDataExcelPath);
        
        try (FileInputStream fis = new FileInputStream(testDataExcelPath);
        		
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            // Get data from specified sheet
            Sheet dataSheet = workbook.getSheet(sheetName);
            if (dataSheet == null) {
                logger.error("Sheet '{}' not found in {}", sheetName, testDataExcelPath);
                throw new RuntimeException("Sheet '" + sheetName + "' not found in " + testDataExcelPath);
            }
            
            // Process the sheet to extract test data
            Map<String, String> extractedData = extractTestDataFromSheet(dataSheet, testCaseId);
            
            // Cache the data for future use
            testDataCache.put(cacheKey, extractedData);
            
            return extractedData;
            
        } catch (Exception e) {
            logger.error("Error retrieving test data from TestData Excel sheet '{}': {}", 
                    sheetName, e.getMessage(), e);
            throw new RuntimeException("Error retrieving test data from TestData Excel sheet '" + 
                    sheetName + "': " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract test data from a specific sheet
     * 
     * @param dataSheet The Excel sheet
     * @param testCaseId The test case ID
     * @return Map of column names to values
     */
    private Map<String, String> extractTestDataFromSheet(Sheet dataSheet, String testCaseId) {
        Map<String, String> testData = new HashMap<>();
        
        // Find header row
        Row headerRow = dataSheet.getRow(0);
        if (headerRow == null) {
            logger.error("Header row not found in sheet {}", dataSheet.getSheetName());
            throw new RuntimeException("Header row not found in sheet " + dataSheet.getSheetName());
        }
        
        // Log all column headers for debugging
        StringBuilder headerDebug = new StringBuilder("Sheet columns for " + dataSheet.getSheetName() + ": ");
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String headerValue = getCellValueAsString(cell);
                headerDebug.append("[").append(i).append(":").append(headerValue).append("] ");
            }
        }
        
        logger.debug(headerDebug.toString());
        
        // Find the column index for TestCaseID
        int testCaseIdColIndex = findTestCaseIdColumn(headerRow);
        
        if (testCaseIdColIndex == -1) {
            logger.error("TestCaseID column not found in sheet {}", dataSheet.getSheetName());
            throw new RuntimeException("TestCaseID column not found in sheet " + dataSheet.getSheetName());
        }
        
        // Find the row matching the test case ID
        int targetRowIndex = findTestCaseRow(dataSheet, testCaseIdColIndex, testCaseId);
        
        if (targetRowIndex == -1) {
            logger.error("TestCaseID '{}' not found in sheet '{}'", testCaseId, dataSheet.getSheetName());
            
            // Log all available test case IDs for debugging
            StringBuilder availableTCs = new StringBuilder("Available TestCaseIDs in sheet " + dataSheet.getSheetName() + ": ");
            for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
                Row row = dataSheet.getRow(i);
                if (row != null && row.getCell(testCaseIdColIndex) != null) {
                    String tcId = getCellValueAsString(row.getCell(testCaseIdColIndex));
                    availableTCs.append("[").append(tcId).append("] ");
                }
            }
            logger.debug(availableTCs.toString());
            
            throw new RuntimeException("TestCaseID '" + testCaseId + "' not found in sheet '" + dataSheet.getSheetName() + "'");
        }
        
        // Extract data from the target row
        Row dataRow = dataSheet.getRow(targetRowIndex);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell headerCell = headerRow.getCell(i);
            Cell dataCell = dataRow != null ? dataRow.getCell(i) : null;
            
            if (headerCell != null) {
                String key = getCellValueAsString(headerCell);
                String value = getCellValueAsString(dataCell);
                
                // Store with original key
                testData.put(key, value);
                
                logger.debug("Loaded test data from sheet {}: {} = {}", 
                        dataSheet.getSheetName(), key, value);
            }
        }
        
        // Log all loaded test data for debugging
        logger.info("Test data for case {} from sheet {}: {}", 
                testCaseId, dataSheet.getSheetName(), testData);
        
        return testData;
    }
    
    /**
     * Find the column index for TestCaseID with flexible matching
     */
    private int findTestCaseIdColumn(Row headerRow) {
        // Try exact matches first with common variations
        String[] testCaseIdVariations = {
            "TestCaseID", "testcaseid", "TestCase ID", "Test Case ID", 
            "TCID", "tcid", "TC_ID", "tc_id", "TestID", "testid", 
            "Test_ID", "test_id", "TC", "tc"
        };
        
        for (String variation : testCaseIdVariations) {
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    String cellValue = getCellValueAsString(cell);
                    if (variation.equalsIgnoreCase(cellValue)) {
                        logger.debug("Found TestCaseID column '{}' at index {}", cellValue, i);
                        return i;
                    }
                }
            }
        }
        
        // Try substring matching as fallback
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String cellValue = getCellValueAsString(cell).toLowerCase();
                if (cellValue.contains("testcase") || cellValue.contains("test case") || 
                    cellValue.contains("test id") || cellValue.startsWith("tc")) {
                    logger.info("Found TestCaseID column '{}' by substring matching at index {}", 
                            getCellValueAsString(cell), i);
                    return i;
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Find the row index for the specified test case ID with enhanced matching
     */
    private int findTestCaseRow(Sheet dataSheet, int testCaseIdColIndex, String testCaseId) {
        logger.debug("Looking for test case ID: {} in sheet: {}", testCaseId, dataSheet.getSheetName());
        
        // Prepare variations of the test case ID for flexible matching
        String[] testCaseVariations = generateTestCaseVariations(testCaseId);
        
        // First pass: exact matches
        for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
            Row row = dataSheet.getRow(i);
            if (row != null && row.getCell(testCaseIdColIndex) != null) {
                String cellValue = getCellValueAsString(row.getCell(testCaseIdColIndex));
                
                for (String variation : testCaseVariations) {
                    if (variation.equals(cellValue)) {
                        logger.debug("Found exact match for test case '{}' at row {}", cellValue, i);
                        return i;
                    }
                }
            }
        }
        
        // Second pass: case-insensitive matches
        for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
            Row row = dataSheet.getRow(i);
            if (row != null && row.getCell(testCaseIdColIndex) != null) {
                String cellValue = getCellValueAsString(row.getCell(testCaseIdColIndex));
                
                for (String variation : testCaseVariations) {
                    if (variation.equalsIgnoreCase(cellValue)) {
                        logger.debug("Found case-insensitive match for test case '{}' at row {}", cellValue, i);
                        return i;
                    }
                }
            }
        }
        
        // Third pass: substring matches
        for (int i = 1; i <= dataSheet.getLastRowNum(); i++) {
            Row row = dataSheet.getRow(i);
            if (row != null && row.getCell(testCaseIdColIndex) != null) {
                String cellValue = getCellValueAsString(row.getCell(testCaseIdColIndex)).toLowerCase();
                
                for (String variation : testCaseVariations) {
                    String lowerVariation = variation.toLowerCase();
                    if (cellValue.contains(lowerVariation) || lowerVariation.contains(cellValue)) {
                        logger.info("Found substring match for test case '{}' with '{}' at row {}", 
                                testCaseId, getCellValueAsString(row.getCell(testCaseIdColIndex)), i);
                        return i;
                    }
                }
            }
        }
        
        return -1;
    }
    
    /**
     * Generate variations of test case ID for flexible matching
     */
    private String[] generateTestCaseVariations(String testCaseId) {
        String[] variations = new String[6];
        variations[0] = testCaseId; // Original
        
        // Extract number if in format like STATIMCM-TC-459
        if (testCaseId.contains("-TC-")) {
            String number = testCaseId.substring(testCaseId.lastIndexOf("-") + 1);
            variations[1] = "TC" + number;
            variations[2] = "TC-" + number;
            variations[3] = number;
        } else {
            variations[1] = testCaseId;
            variations[2] = testCaseId;
            variations[3] = testCaseId;
        }
        
        // Additional common formats
        variations[4] = testCaseId.replaceAll("[^a-zA-Z0-9]", ""); // Remove special chars
        variations[5] = testCaseId.replaceAll("-", "_"); // Replace dashes with underscores
        
        return variations;
    }
    
    /**
     * Gets test data from database
     */
    private Map<String, String> getTestDataFromDatabase(String testCaseId) {
        Map<String, String> testData = new HashMap<>();
        
        logger.info("Retrieving test data for {} from database", testCaseId);
        
        if (dbConnection == null) {
            logger.error("Database connection not initialized");
            throw new RuntimeException("Database connection not initialized");
        }
        
        try {
            String query = "SELECT * FROM test_data WHERE test_case_id = ?";
            try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
                stmt.setString(1, testCaseId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnName(i);
                            String columnValue = rs.getString(i);
                            testData.put(columnName, columnValue);
                        }
                    } else {
                        logger.error("No test data found for test case {}", testCaseId);
                        throw new RuntimeException("No test data found for test case " + testCaseId);
                    }
                }
            }
            
            logger.info("Retrieved {} data items for test case {}", testData.size(), testCaseId);
            return testData;
            
        } catch (SQLException e) {
            logger.error("Error retrieving test data from database: {}", e.getMessage(), e);
            throw new RuntimeException("Error retrieving test data from database: " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets test data for a test case by searching across all available test data sheets
     * Excludes framework configuration and run manager sheets
     * 
     * @param testCaseId Test case ID to search for
     * @return Map of test data parameters from all sheets containing the test case
     */
    public Map<String, String> getTestDataFromAnySheet(String testCaseId) {
        String testDataExcelPath = getTestDataExcelPath();
        Map<String, String> testData = new HashMap<>();
        
        logger.info("Searching for test data for {} across all available sheets in {}", testCaseId, testDataExcelPath);
        
        try (FileInputStream fis = new FileInputStream(testDataExcelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            // Get all sheet names and filter out non-data sheets
            String[] excludedSheets = {"Framework Configuration", "RunManager", "Run Manager", "framework configuration"};
            
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                
                // Skip excluded sheets
                boolean shouldSkip = false;
                for (String excluded : excludedSheets) {
                    if (excluded.equalsIgnoreCase(sheetName)) {
                        shouldSkip = true;
                        break;
                    }
                }
                
                if (shouldSkip) {
                    logger.debug("Skipping configuration sheet: {}", sheetName);
                    continue;
                }
                
                logger.debug("Searching for test case {} in sheet: {}", testCaseId, sheetName);
                
                try {
                    // Try to extract test data from this sheet
                    Map<String, String> sheetData = extractTestDataFromSheet(sheet, testCaseId);
                    if (!sheetData.isEmpty()) {
                        logger.info("Found test data for {} in sheet {}", testCaseId, sheetName);
                        
                        // Merge data from this sheet, prefixing keys with sheet name for reference
                        for (Map.Entry<String, String> entry : sheetData.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue();
                            
                            // Add both prefixed and non-prefixed versions
                            testData.put(key, value);
                            testData.put(sheetName + "." + key, value);
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Could not extract test data from sheet {}: {}", sheetName, e.getMessage());
                    // Continue to next sheet
                }
            }
            
            if (testData.isEmpty()) {
                logger.warn("Test case {} not found in any available test data sheets", testCaseId);
            } else {
                logger.info("Successfully loaded test data for {} from multiple sheets: {}", testCaseId, testData);
            }
            
            return testData;
            
        } catch (Exception e) {
            logger.error("Error searching for test data across sheets: {}", e.getMessage(), e);
            throw new RuntimeException("Error searching for test data across sheets: " + e.getMessage(), e);
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
                	java.util.Date date = cell.getDateCellValue();
                	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                	return formatter.format(date);
                } else {
                    // Convert numeric to string without trailing zeros
                    double numValue = cell.getNumericCellValue();
                    if (numValue == Math.floor(numValue)) {
                        return String.valueOf((int)numValue);
                    } else {
                        return String.valueOf(numValue).replaceAll("\\.0+$", "");
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        double numValue = cell.getNumericCellValue();
                        if (numValue == Math.floor(numValue)) {
                            return String.valueOf((int)numValue);
                        } else {
                            return String.valueOf(numValue).replaceAll("\\.0+$", "");
                        }
                    } catch (Exception ex) {
                        return cell.getCellFormula();
                    }
                }
            default:
                return "";
        }
    }
    
    /**
     * Clears the test data cache
     */
    public void clearCache() {
        logger.info("Clearing test data cache");
        testDataCache.clear();
    }
    
    /**
     * Closes database connection
     */
    public void closeConnection() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing database connection: {}", e.getMessage(), e);
            }
        }
    }
}
