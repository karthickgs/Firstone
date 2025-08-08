/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: QMetryClient.java
 * Description: Handles integration with QMetry test management system for uploading results
 */

package com.novac.naf.qmetry;

import com.novac.naf.config.ConfigLoader;
import com.novac.naf.reporting.ReportManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles integration with QMetry API using 3-step upload process
 */
public class QMetryClient {
    private static final Logger logger = LoggerFactory.getLogger(QMetryClient.class);
    private final ConfigLoader configLoader;
    private String apiBaseUrl;
    private String projectId;
    private String apiKey;
    private String username;
    private String password;
    private String authHeaderValue;
    private boolean qmetryEnabled = false;
    
    // QMetry API endpoints
    private static final String INITIATE_UPLOAD_ENDPOINT = "https://karya-pmt.novactech.net/rest/qtm4j/automation/latest/importresult";
    private static final String TRACK_STATUS_ENDPOINT = "https://karya-pmt.novactech.net/rest/qtm4j/automation/latest/importresult/track";
    private static final String FALLBACK_UPLOAD_ENDPOINT = "https://karya-pmt.novactech.net/rest/qtm4j/automation/latest/importresult/submitFile";
    
    // Polling configuration
    private static final int POLLING_INTERVAL_MS = 5000; // 5 seconds
    private static final int MAX_POLLING_ATTEMPTS = 24; // 2 minutes total
    
    /**
     * Constructor initializes QMetry client with configuration
     */
    public QMetryClient(ConfigLoader configLoader) {
        this.configLoader = configLoader;
        
        // Check if QMetry integration is required first
        if (!configLoader.isQMetryIntegrationRequired()) {
            logger.info("QMetry integration is disabled (QMetryIntegration=Not-Required). Skipping QMetry client initialization.");
            qmetryEnabled = false;
            return;
        }
        
        // Core QMetry configuration
        this.apiBaseUrl = getConfigValueOrDefault("API_URL", INITIATE_UPLOAD_ENDPOINT);
        this.projectId = getConfigValueOrDefault("QMetry_ProjectID", "12718");
        this.apiKey = configLoader.getConfigValue("QMetry_API_Key");
        
        // Read username and password for Basic Authentication
        this.username = configLoader.getConfigValue("QMetry_Username");
        this.password = configLoader.getConfigValue("QMetry_Password");
        
        // Generate Basic Authentication header value if username and password are provided
        if (username != null && !username.isEmpty() && !"null".equalsIgnoreCase(username) &&
            password != null && !password.isEmpty() && !"null".equalsIgnoreCase(password)) {
            String credentials = username + ":" + password;
            authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
            logger.debug("Generated Basic Authentication header");
        } else {
            logger.warn("Username or password not provided. Basic Authentication will not be used.");
        }
        
        // Log QMetry configuration
        logger.info("QMetry configuration: API URL={}, Project ID={}", apiBaseUrl, projectId);
        
        // Enable QMetry if configuration is valid
        if (isQMetryConfigValid()) {
            qmetryEnabled = true;
            logger.info("QMetry integration enabled successfully");
        } else {
            logger.warn("QMetry integration is disabled due to missing required configuration.");
        }
    }
    
    /**
     * Validates that all required QMetry configuration parameters are present
     */
    private boolean isQMetryConfigValid() {
        if (apiKey == null || apiKey.isEmpty() || "null".equalsIgnoreCase(apiKey)) {
            logger.warn("QMetry API Key is not configured. This is required for authentication.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Uploads the generated report file to QMetry using 3-step process
     * Step 1: Initiate upload and get tracking ID & upload URL
     * Step 2: Upload file to the received URL
     * Step 3: Poll import status using tracking ID
     */
    public boolean uploadResultsFile(File reportFile) {
        // First check if QMetry integration is required
        if (!configLoader.isQMetryIntegrationRequired()) {
            logger.debug("QMetry integration is disabled. Skipping results upload.");
            return true; // Return true to continue execution flow smoothly
        }
        
        if (!qmetryEnabled) {
            logger.info("QMetry integration not enabled. Skipping results upload");
            return true;
        }
        
        if (reportFile == null || !reportFile.exists()) {
            logger.warn("No report file provided or file does not exist");
            return false;
        }
        
        // Validate that we only accept JSON files for QMetry upload
        if (!reportFile.getName().toLowerCase().endsWith(".json")) {
            logger.error("QMetry upload only accepts JSON files. Provided file: {}", reportFile.getName());
            logger.error("Please ensure the JSON report is generated properly for QMetry upload");
            return false;
        }
        
        try {
            logger.info("Starting QMetry 3-step upload process for file: {}", reportFile.getName());
            
            // Validate file content
            byte[] fileContent = Files.readAllBytes(reportFile.toPath());
            if (fileContent.length == 0) {
                logger.error("Report file is empty: {}", reportFile.getName());
                return false;
            }
            
            logger.info("JSON file content size: {} bytes", fileContent.length);
            logger.info("=== QMetry 3-Step Upload Process ===");
            
            // Step 1: Initiate upload and get tracking ID & upload URL
            String[] step1Result = executeStep1InitiateUpload();
            if (step1Result == null) {
                logger.error("Step 1 failed - could not initiate upload");
                return false;
            }
            
            String uploadUrl = step1Result[0];
            String trackingId = step1Result[1];
            
            logger.info("Step 1 successful - Upload initiated");
            logger.info("Upload URL: {}", uploadUrl);
            logger.info("Tracking ID: {}", trackingId);
            
            // Step 2: Upload file to the received URL
            boolean step2Result = executeStep2UploadFile(reportFile, uploadUrl, trackingId);
            if (!step2Result) {
                logger.error("Step 2 failed - file upload unsuccessful");
                return false;
            }
            
            logger.info("Step 2 successful - File uploaded");
            
            // Step 3: Poll import status using tracking ID
            boolean step3Result = executeStep3PollStatus(trackingId);
            if (!step3Result) {
                logger.warn("Step 3 completed with warnings - check import status manually");
                // Don't return false here as upload might still be processing
            }
            
            logger.info("QMetry 3-step upload process completed");
            logger.info("Final Tracking ID for monitoring: {}", trackingId);
            return true;
            
        } catch (Exception e) {
            logger.error("Exception occurred during QMetry 3-step upload: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Step 1: Initiate upload and get tracking ID & upload URL
     * Returns array: [uploadUrl, trackingId] or null if failed
     */
    private String[] executeStep1InitiateUpload() {
        try {
            logger.info("=== Step 1: Initiating Upload ===");
            logger.info("Endpoint: {}", INITIATE_UPLOAD_ENDPOINT);
            
            // Create HttpClient
            HttpClient httpClient = HttpClientBuilder.create().build();
            
            // Create POST request
            HttpPost httpPost = new HttpPost(INITIATE_UPLOAD_ENDPOINT);
            
            // Add headers
            if (apiKey != null && !apiKey.isEmpty()) {
                httpPost.setHeader("apiKey", apiKey);
                logger.debug("Added API key header");
            }
            
            if (authHeaderValue != null) {
                httpPost.setHeader("Authorization", authHeaderValue);
                logger.debug("Added Authorization header");
            }
            
            httpPost.setHeader("Content-Type", "application/json");
            
            // Create JSON body
            String jsonBody = "{\n" +
                "  \"format\": \"cucumber\",\n" +
                "  \"attachFile\": true,\n" +
                "  \"isZip\": false\n" +
                "}";
            
            logger.info("Step 1 request body: {}", jsonBody);
            
            // Set JSON entity
            StringEntity entity = new StringEntity(jsonBody, "UTF-8");
            httpPost.setEntity(entity);
            
            // Execute the request
            logger.info("Making Step 1 API call to initiate upload...");
            HttpResponse response = httpClient.execute(httpPost);
            
            logger.info("=== Step 1 Response ===");
            int statusCode = response.getStatusLine().getStatusCode();
            String statusLine = response.getStatusLine().toString();
            logger.info("Status: {} {}", statusCode, statusLine);
            
            // Get response body
            String responseBody = "";
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                responseBody = EntityUtils.toString(responseEntity);
                EntityUtils.consume(responseEntity);
            }
            
            logger.info("Step 1 response body: {}", responseBody);
            
            // Check for successful response
            if (statusCode >= 200 && statusCode < 300) {
                logger.info("Step 1 successful - Status: {}", statusCode);
                
                // Extract upload URL and tracking ID from response - Fix: look for "url" not "uploadUrl"
                String uploadUrl = extractFieldFromResponse(responseBody, "url");
                String trackingId = extractFieldFromResponse(responseBody, "trackingId");
                
                if (uploadUrl != null && !uploadUrl.trim().isEmpty() && 
                    trackingId != null && !trackingId.trim().isEmpty()) {
                    logger.info("Extracted upload URL: {}", uploadUrl);
                    logger.info("Extracted tracking ID: {}", trackingId);
                    return new String[]{uploadUrl, trackingId};
                } else {
                    logger.error("Could not extract upload URL or tracking ID from Step 1 response");
                    logger.error("Upload URL extracted: {}", uploadUrl);
                    logger.error("Tracking ID extracted: {}", trackingId);
                    return null;
                }
            } else {
                logger.error("Step 1 failed");
                logger.error("Status: {} - {}", statusCode, statusLine);
                logger.error("Response body: {}", responseBody);
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Exception in Step 1: {}", e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Step 2: Upload file to the URL received from Step 1
     */
    private boolean executeStep2UploadFile(File reportFile, String uploadUrl, String trackingId) {
        try {
            logger.info("=== Step 2: Uploading File ===");
            
            // Use provided uploadUrl or fallback to default with trackingId
            String finalUploadUrl = uploadUrl;
            if (finalUploadUrl == null || finalUploadUrl.trim().isEmpty()) {
                finalUploadUrl = FALLBACK_UPLOAD_ENDPOINT + "?trackingId=" + trackingId;
                logger.info("Using fallback upload URL: {}", finalUploadUrl);
            } else {
                logger.info("Using dynamic upload URL: {}", finalUploadUrl);
            }
            
            logger.info("File: {} ({} bytes)", reportFile.getName(), reportFile.length());
            
            // Create HttpClient
            HttpClient httpClient = HttpClientBuilder.create().build();
            
            // Create POST request
            HttpPost httpPost = new HttpPost(finalUploadUrl);
            
            // Add headers
            if (apiKey != null && !apiKey.isEmpty()) {
                httpPost.setHeader("apiKey", apiKey);
                logger.debug("Added API key header");
            }
            
            if (authHeaderValue != null) {
                httpPost.setHeader("Authorization", authHeaderValue);
                logger.debug("Added Authorization header");
            }
            
            // Build multipart entity for file upload (Content-Type will be set automatically)
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            
            // Add file part with field name "file"
            FileBody fileBody = new FileBody(reportFile);
            entityBuilder.addPart("file", fileBody);
            logger.info("Added file part: {} (auto-detected content type)", reportFile.getName());
            
            // Set the multipart entity
            HttpEntity multipartEntity = entityBuilder.build();
            httpPost.setEntity(multipartEntity);
            
            // Execute the request
            logger.info("Making Step 2 API call to upload file...");
            HttpResponse response = httpClient.execute(httpPost);
            
            logger.info("=== Step 2 Response ===");
            int statusCode = response.getStatusLine().getStatusCode();
            String statusLine = response.getStatusLine().toString();
            logger.info("Status: {} {}", statusCode, statusLine);
            
            // Get response body
            String responseBody = "";
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                responseBody = EntityUtils.toString(responseEntity);
                EntityUtils.consume(responseEntity);
            }
            
            logger.info("Step 2 response body: {}", responseBody);
            
            // Check for successful upload
            if (statusCode >= 200 && statusCode < 300) {
                logger.info("Step 2 successful - File uploaded successfully - Status: {}", statusCode);
                return true;
            } else {
                logger.error("Step 2 failed - File upload unsuccessful");
                logger.error("Status: {} - {}", statusCode, statusLine);
                logger.error("Response body: {}", responseBody);
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Exception in Step 2: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Step 3: Poll import status using tracking ID
     */
    private boolean executeStep3PollStatus(String trackingId) {
        try {
            logger.info("=== Step 3: Polling Import Status ===");
            logger.info("Tracking ID: {}", trackingId);
            logger.info("Polling interval: {} ms", POLLING_INTERVAL_MS);
            logger.info("Max attempts: {}", MAX_POLLING_ATTEMPTS);
            
            String trackUrl = TRACK_STATUS_ENDPOINT + "?trackingId=" + trackingId;
            logger.info("Track URL: {}", trackUrl);
            
            for (int attempt = 1; attempt <= MAX_POLLING_ATTEMPTS; attempt++) {
                logger.info("Polling attempt {}/{}", attempt, MAX_POLLING_ATTEMPTS);
                
                // Create HttpClient
                HttpClient httpClient = HttpClientBuilder.create().build();
                
                // Create GET request
                HttpGet httpGet = new HttpGet(trackUrl);
                
                // Add headers
                if (apiKey != null && !apiKey.isEmpty()) {
                    httpGet.setHeader("apiKey", apiKey);
                }
                
                if (authHeaderValue != null) {
                    httpGet.setHeader("Authorization", authHeaderValue);
                }
                
                // Execute the request
                HttpResponse response = httpClient.execute(httpGet);
                
                int statusCode = response.getStatusLine().getStatusCode();
                String statusLine = response.getStatusLine().toString();
                
                // Get response body
                String responseBody = "";
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    responseBody = EntityUtils.toString(responseEntity);
                    EntityUtils.consume(responseEntity);
                }
                
                logger.info("Polling response - Status: {}, Body: {}", statusCode, responseBody);
                
                if (statusCode >= 200 && statusCode < 300) {
                    // Check if processing is complete
                    String processStatus = extractFieldFromResponse(responseBody, "processStatus");
                    String importStatus = extractFieldFromResponse(responseBody, "importStatus");

                    if (processStatus != null && importStatus != null) {
                        if ("SUCCESS".equalsIgnoreCase(processStatus) && "SUCCESS".equalsIgnoreCase(importStatus)) {
                            logger.info("Step 3 successful - Import completed successfully");
                            logger.info("Final status: processStatus={}, importStatus={}", processStatus, importStatus);
                            return true;
                        } else if ("FAILED".equalsIgnoreCase(processStatus) || "FAILED".equalsIgnoreCase(importStatus)
                                || "ERROR".equalsIgnoreCase(processStatus) || "ERROR".equalsIgnoreCase(importStatus)) {
                            logger.error("Step 3 failed - Import failed");
                            logger.error("processStatus={}, importStatus={}", processStatus, importStatus);
                            return false;
                        } else {
                            logger.info("Import still in progress... (processStatus={}, importStatus={})", processStatus, importStatus);
                        }
                    } else {
                        logger.warn("Missing processStatus or importStatus in response, assuming still processing...");
                        logger.debug("Response body searched: {}", responseBody);
                    }
                } else {
                    logger.warn("Polling failed with status: {} - {}", statusCode, statusLine);
                    logger.warn("Response: {}", responseBody);
                }
                
                // Wait before next attempt (except for last attempt)
                if (attempt < MAX_POLLING_ATTEMPTS) {
                    try {
                        Thread.sleep(POLLING_INTERVAL_MS);
                    } catch (InterruptedException e) {
                        logger.warn("Polling interrupted: {}", e.getMessage());
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
            
            logger.warn("Step 3 timeout - Maximum polling attempts reached");
            logger.warn("Import may still be processing. Check manually with tracking ID: {}", trackingId);
            return false;
            
        } catch (Exception e) {
            logger.error("Exception in Step 3: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Extract a specific field value from JSON response using regex
     */
    private String extractFieldFromResponse(String responseBody, String fieldName) {
        if (responseBody == null || responseBody.trim().isEmpty()) {
            logger.warn("Response body is empty, cannot extract field: {}", fieldName);
            return null;
        }
        
        try {
            // Look for the field in JSON response using regex
            Pattern fieldPattern = Pattern.compile("\"" + fieldName + "\"\\s*:\\s*\"([^\"]+)\"");
            Matcher fieldMatcher = fieldPattern.matcher(responseBody);
            
            if (fieldMatcher.find()) {
                String fieldValue = fieldMatcher.group(1);
                logger.debug("Extracted {}: {}", fieldName, fieldValue);
                return fieldValue;
            } else {
                logger.warn("Could not find '{}' field in response body", fieldName);
                logger.debug("Response body searched: {}", responseBody);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error extracting field '{}': {}", fieldName, e.getMessage());
            return null;
        }
    }
    
    /**
     * Gets a configuration value with a default fallback
     */
    private String getConfigValueOrDefault(String key, String defaultValue) {
        String value = configLoader.getConfigValue(key);
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            logger.debug("{} not found in configuration, using default: {}", key, defaultValue);
            return defaultValue;
        }
        return value;
    }
    
    /**
     * Checks if QMetry integration is enabled
     */
    public boolean isQMetryEnabled() {
        return configLoader.isQMetryIntegrationRequired() && qmetryEnabled;
    }
}
