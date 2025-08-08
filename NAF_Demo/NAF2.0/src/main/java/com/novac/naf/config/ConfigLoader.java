/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: ConfigLoader.java
 * Description: Loads and manages framework configuration from the RunManager Excel file
 */

package com.novac.naf.config;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads configuration from RunManager.xlsx
 */
public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    private final String excelPath;
    private Map<String, String> configMap = new HashMap<>();
    private String projectName;
    private String dataSource;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String qmetryCycleId;
    private String qmetryProjectId;
    private String qmetryApiUrl;
    private String browser;
    private String environment;
    private Boolean headlessMode;
    private String screenshotMode;
    private String resultUploadFormat; // New field for XML/JSON report format
    private String qmetryIntegration; // New field for QMetry integration toggle
    private Map<String, String> applicationUrls = new HashMap<>(); // To store environment-specific URLs
    
    public ConfigLoader(String excelPath) {
        this.excelPath = excelPath;
        validateExcelPath();
        loadConfiguration();
        
        // Re-add the excel path to the config map to ensure it's available
        // This is important as the RunManager will look for it specifically
        configMap.put("ExcelPath", excelPath);
    }
    
    private void validateExcelPath() {
        if (excelPath == null || excelPath.trim().isEmpty()) {
            logger.error("Excel path is null or empty. Please provide a valid Excel file path.");
            throw new RuntimeException("Excel path is null or empty. Please provide a valid Excel file path.");
        }
        
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) {
            logger.error("Excel file does not exist: {}", excelPath);
            throw new RuntimeException("Excel file does not exist: " + excelPath);
        }
        
        if (!excelFile.canRead()) {
            logger.error("Excel file cannot be read: {}", excelPath);
            throw new RuntimeException("Excel file cannot be read: " + excelPath);
        }
        
        logger.info("Excel file validated successfully: {}", excelFile.getAbsolutePath());
    }
    
    private void loadConfiguration() {
        logger.info("Loading configuration from: {}", excelPath);
        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {
             
            // Load framework configuration sheet - checking both capitalization variants
            Sheet configSheet = workbook.getSheet("framework configuration");
            if (configSheet == null) {
                configSheet = workbook.getSheet("Framework Configuration");
            }
            
            if (configSheet == null) {
                logger.error("Framework Configuration sheet not found in {}. Available sheets: {}", excelPath, getAvailableSheets(workbook));
                throw new RuntimeException("Framework Configuration sheet not found in " + excelPath);
            }
            
            // Debug - print the number of rows in the sheet
            logger.debug("Number of rows in config sheet: {}", configSheet.getPhysicalNumberOfRows());
            
            // Process all rows in the configuration sheet
            for (int i = 0; i <= configSheet.getLastRowNum(); i++) {
                Row row = configSheet.getRow(i);
                if (row != null) {
                    Cell keyCell = row.getCell(0);
                    Cell valueCell = row.getCell(1);
                    
                    if (keyCell != null) {
                        String key = getCellValueAsString(keyCell).trim();
                        String value = valueCell != null ? getCellValueAsString(valueCell).trim() : "";
                        
                        if (!key.isEmpty()) {
                            configMap.put(key, value);
                            
                            // Check if this is a URL configuration entry (keys starting with "URL_" or "ENV_")
                            if (key.startsWith("URL_") || key.startsWith("ENV_")) {
                                String envKey = key.startsWith("URL_") ? key.substring(4) : key.substring(4);
                                applicationUrls.put(envKey, value);
                                logger.debug("Registered application URL for {}: {}", envKey, value);
                            }
                            
                            logger.debug("Loaded config: {} = {}", key, value);
                        }
                    }
                }
            }
            
            // Log all loaded configuration for debugging
            logger.debug("Loaded configuration: {}", configMap);
            
            // Extract specific configuration values with default fallbacks
            projectName = getConfigValueOrDefault("ProjectName", "NAF Project");
            dataSource = getConfigValueOrDefault("DataSource", "EXCEL");
            dbUrl = getConfigValue("DB_URL");
            dbUser = getConfigValue("DB_User");
            dbPassword = getConfigValue("DB_Password");
            qmetryCycleId = getConfigValue("QMetry_CycleID");
            qmetryProjectId = getConfigValue("QMetry_ProjectID");
            qmetryApiUrl = getConfigValue("API_URL");
            browser = getConfigValueOrDefault("Browser", "Chrome");
            environment = getConfigValueOrDefault("Environment", "QA");
            
            // Read QMetry integration flag with default to "Not-Required" for backward compatibility
            qmetryIntegration = getConfigValueOrDefault("QMetryIntegration", "Not-Required");
            if (!qmetryIntegration.equals("Required") && !qmetryIntegration.equals("Not-Required")) {
                logger.warn("Invalid QMetryIntegration value: {}. Valid values are 'Required' or 'Not-Required'. Defaulting to 'Not-Required'", qmetryIntegration);
                qmetryIntegration = "Not-Required";
            }
            
            // Get the new ResultUploadFormat configuration with default to "None"
            resultUploadFormat = getConfigValueOrDefault("ResultUploadFormat", "None");
            if (!resultUploadFormat.equals("XML") && !resultUploadFormat.equals("JSON") && !resultUploadFormat.equals("None")) {
                logger.warn("Invalid ResultUploadFormat value: {}. Valid values are 'XML', 'JSON', or 'None'. Defaulting to 'None'", resultUploadFormat);
                resultUploadFormat = "None";
            }
            
            // Get the base application URL for the current environment
            String baseUrlKey = "URL_" + environment;
            if (configMap.containsKey(baseUrlKey)) {
                String baseUrl = configMap.get(baseUrlKey);
                applicationUrls.put(environment, baseUrl);
                logger.info("Set base application URL for environment {}: {}", environment, baseUrl);
            } else {
                logger.warn("No base URL configured for environment: {}. Please add '{}' in the configuration sheet.", environment, baseUrlKey);
            }
            
            String headless = getConfigValue("HeadlessMode");
            headlessMode = (headless != null && !headless.isEmpty()) ? 
                "true".equalsIgnoreCase(headless) : false;
            
            // Read the new ScreenshotMode configuration with default to "pass_fail"
            screenshotMode = getConfigValueOrDefault("ScreenshotMode", "pass_fail");
            if (!screenshotMode.equals("all") && !screenshotMode.equals("pass_fail")) {
                logger.warn("Invalid ScreenshotMode value: {}. Valid values are 'all' or 'pass_fail'. Defaulting to 'pass_fail'", screenshotMode);
                screenshotMode = "pass_fail";
            }
            
            // Make sure ExcelPath is explicitly set in the configMap
            if (!configMap.containsKey("ExcelPath") || configMap.get("ExcelPath") == null || configMap.get("ExcelPath").isEmpty()) {
                configMap.put("ExcelPath", excelPath);
            }
            
            // Explicitly log key configuration values
            logger.info("Configuration loaded for project: {}", projectName);
            logger.info("Using data source: {}", dataSource);
            logger.info("Excel path set to: {}", configMap.get("ExcelPath"));
            logger.info("Browser: {}", browser);
            logger.info("Environment: {}", environment);
            logger.info("Screenshot mode: {}", screenshotMode);
            logger.info("Result upload format: {}", resultUploadFormat);
            logger.info("QMetry integration: {}", qmetryIntegration);
            logger.info("QMetry API URL: {}", qmetryApiUrl != null ? qmetryApiUrl : "Not configured");
            logger.info("Application URLs configured: {}", applicationUrls);
            
        } catch (FileNotFoundException e) {
            logger.error("Excel file not found: {} - {}", excelPath, e.getMessage());
            throw new RuntimeException("Excel file not found: " + excelPath, e);
        } catch (Exception e) {
            logger.error("Error loading configuration: " + e.getMessage(), e);
            throw new RuntimeException("Error loading configuration: " + e.getMessage(), e);
        }
    }
    
    /**
     * Helper method to get all available sheet names for debugging
     */
    private String getAvailableSheets(Workbook workbook) {
        StringBuilder sheetNames = new StringBuilder();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            if (i > 0) sheetNames.append(", ");
            sheetNames.append("'").append(workbook.getSheetName(i)).append("'");
        }
        return sheetNames.toString();
    }
    
    /**
     * Gets a configuration value with a default fallback if not found
     */
    private String getConfigValueOrDefault(String key, String defaultValue) {
        String value = configMap.get(key);
        if (value == null || value.trim().isEmpty()) {
            logger.warn("{} is not configured in the framework configuration, using default: {}", key, defaultValue);
            configMap.put(key, defaultValue);
            return defaultValue;
        }
        return value;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        try {
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
                        return String.valueOf(cell.getStringCellValue());
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
        } catch (Exception e) {
            logger.error("Error getting cell value: {}", e.getMessage());
            return "";
        }
    }
    
    // Getters for configuration properties
    public String getProjectName() {
        return projectName;
    }
    
    public String getDataSource() {
        return dataSource;
    }
    
    public String getDbUrl() {
        return dbUrl;
    }
    
    public String getDbUser() {
        return dbUser;
    }
    
    public String getDbPassword() {
        return dbPassword;
    }
    
    public String getQMetryCycleId() {
        return qmetryCycleId;
    }
    
    public String getQMetryProjectId() {
        return qmetryProjectId;
    }
    
    public String getQMetryApiUrl() {
        return qmetryApiUrl;
    }
    
    public String getBrowser() {
        return browser;
    }
    
    public String getEnvironment() {
        return environment;
    }
    
    public Boolean isHeadlessMode() {
        return headlessMode;
    }
    
    public String getConfigValue(String key) {
        return configMap.get(key);
    }
    
    /**
     * Gets the configured screenshot mode
     * @return "all" to capture screenshots for all steps, or "pass_fail" to capture only for passed and failed steps
     */
    public String getScreenshotMode() {
        return screenshotMode;
    }
    
    /**
     * Gets the configured result upload format for QMetry integration
     * @return "XML", "JSON", or "None"
     */
    public String getResultUploadFormat() {
        return resultUploadFormat;
    }
    
    /**
     * Gets the configured QMetry integration flag
     * @return "Required" to enable QMetry integration, or "Not-Required" to disable it
     */
    public String getQMetryIntegration() {
        return qmetryIntegration;
    }
    
    /**
     * Checks if QMetry integration is required
     * @return true if QMetryIntegration is set to "Required", false otherwise
     */
    public boolean isQMetryIntegrationRequired() {
        return "Required".equalsIgnoreCase(qmetryIntegration);
    }
    
    /**
     * Get application URL for a specific environment key
     * This method can be used to retrieve URLs referenced with ENV_ prefix in feature files
     * 
     * @param envKey The environment key (e.g., "saucedemo.com", "STATIM")
     * @return The configured URL for the environment, or null if not found
     */
    public String getApplicationUrl(String envKey) {
        if (envKey == null || envKey.isEmpty()) {
            return null;
        }
        
        // First check if there's a direct URL mapping
        if (applicationUrls.containsKey(envKey)) {
            logger.debug("Found direct URL mapping for key {}: {}", envKey, applicationUrls.get(envKey));
            return applicationUrls.get(envKey);
        }
        
        // Then check if there's an environment-specific URL
        String envUrlKey = "URL_" + envKey;
        String envUrl = configMap.get(envUrlKey);
        if (envUrl != null && !envUrl.isEmpty()) {
            logger.debug("Found URL for environment key {}: {}", envKey, envUrl);
            return envUrl;
        }
        
        // If still not found, use the base URL for the current environment
        String baseUrl = applicationUrls.get(environment);
        if (baseUrl != null) {
            logger.warn("No specific URL found for key '{}', falling back to base URL for environment '{}': {}", 
                    envKey, environment, baseUrl);
            return baseUrl;
        }
        
        logger.error("No URL configuration found for key: {}", envKey);
        return null;
    }
    
    /**
     * Get the base application URL for the current environment
     * 
     * @return The base URL for the current environment
     */
    public String getBaseApplicationUrl() {
        String baseUrl = applicationUrls.get(environment);
        if (baseUrl == null || baseUrl.isEmpty()) {
            logger.warn("No base URL configured for environment: {}. Using default placeholder.", environment);
            return "https://statim20.novactech.net/CoreApp_QA/masters";
        }
        return baseUrl;
    }
}
