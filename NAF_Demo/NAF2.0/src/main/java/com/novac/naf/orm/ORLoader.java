
/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: ORLoader.java
 * Description: Object Repository Loader that manages page object locators from JSON files, now supporting dual-locator strategy (ID + XPath)
 */

package com.novac.naf.orm;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Object Repository Loader for managing page object locators using dual-locator strategy (ID + XPath)
 */
public class ORLoader {
    private static final Logger logger = LoggerFactory.getLogger(ORLoader.class);
    private static final Map<String, Map<String, ElementLocator>> objectRepositories = new HashMap<>();
    private static boolean initialized = false;
    
    /**
     * ElementLocator class to encapsulate both ID and XPath locator values
     */
    public static class ElementLocator {
        private String id;
        private String xpath;
        
        public ElementLocator(String id, String xpath) {
            this.id = id;
            this.xpath = xpath;
        }
        
        public String getId() {
            return id;
        }
        
        public String getXpath() {
            return xpath;
        }
        
        public boolean hasId() {
            return id != null && !id.trim().isEmpty();
        }
        
        public boolean hasXpath() {
            return xpath != null && !xpath.trim().isEmpty();
        }
    }
    
    /**
     * Initialize all object repositories from the ObjectRepo directory
     */
    public static void initialize() {
        if (initialized) {
            logger.debug("ORLoader already initialized, skipping...");
            return;
        }
        
        try {
            String orDirectory = "./src/main/resources/ObjectRepo";
            File orDir = new File(orDirectory);
            
            if (!orDir.exists() || !orDir.isDirectory()) {
                logger.error("ObjectRepo directory not found at: {}", orDirectory);
                throw new RuntimeException("ObjectRepo directory not found at: " + orDirectory);
            }
            
            File[] jsonFiles = orDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
            
            if (jsonFiles == null || jsonFiles.length == 0) {
                logger.warn("No JSON files found in ObjectRepo directory");
                return;
            }
            
            for (File jsonFile : jsonFiles) {
                String fileName = jsonFile.getName();
                String pageName = fileName.substring(0, fileName.lastIndexOf('.'));
                loadObjectRepository(pageName, jsonFile.getAbsolutePath());
            }
            
            initialized = true;
            logger.info("ORLoader initialization completed. Loaded {} pages", objectRepositories.size());
            
        } catch (Exception e) {
            logger.error("Error initializing ORLoader: {}", e.getMessage(), e);
            throw new RuntimeException("Error initializing ORLoader: " + e.getMessage(), e);
        }
    }
    
    /**
     * Loads an Object Repository file supporting both old and new JSON formats
     * 
     * @param pageName Name of the page
     * @param orFilePath Path to the OR JSON file
     */
    public static void loadObjectRepository(String pageName, String orFilePath) {
        try {
            logger.info("Loading Object Repository for page: {} from {}", pageName, orFilePath);
            
            // Read JSON file
            String content = new String(Files.readAllBytes(Paths.get(orFilePath)));
            JSONObject jsonObject = new JSONObject(content);
            
            // Create a new map for this page
            Map<String, ElementLocator> pageObjects = new HashMap<>();
            
            // Process all elements in the JSON
            JSONArray elementsArray = jsonObject.getJSONArray("elements");
            for (int i = 0; i < elementsArray.length(); i++) {
                JSONObject elementObject = elementsArray.getJSONObject(i);
                String elementName = elementObject.getString("name");
                
                String id = null;
                String xpath = null;
                
                // Check for ID field (new format)
                if (elementObject.has("id")) {
                    id = elementObject.getString("id");
                }
                
                // Check for XPath field (new format)
                if (elementObject.has("xpath")) {
                    xpath = elementObject.getString("xpath");
                }
                // Check for legacy XPath field (old format)
                else if (elementObject.has("xpathValue")) {
                    xpath = elementObject.getString("xpathValue");
                }
                
                // Create ElementLocator with both ID and XPath
                ElementLocator locator = new ElementLocator(id, xpath);
                pageObjects.put(elementName, locator);
                
                logger.debug("Loaded element: {} with id: {} and xpath: {}", elementName, id, xpath);
            }
            
            // Add the page objects to the repositories map
            objectRepositories.put(pageName, pageObjects);
            logger.info("Object Repository loaded successfully for page: {} with {} elements", pageName, pageObjects.size());
            
        } catch (Exception e) {
            logger.error("Error loading Object Repository for page {}: {}", pageName, e.getMessage(), e);
            throw new RuntimeException("Error loading Object Repository for page " + pageName + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Gets a Selenium locator for an element using dual-locator strategy (ID first, then XPath)
     * 
     * @param pageName Name of the page
     * @param elementName Name of the element
     * @return Selenium By locator using dual-locator strategy
     */
    public static By getLocator(String pageName, String elementName) {
        // Ensure ORLoader is initialized
        if (!initialized) {
            logger.info("ORLoader not initialized, initializing now...");
            initialize();
        }
        
        logger.debug("Getting locator for element: {}.{}", pageName, elementName);
        
        Map<String, ElementLocator> pageObjects = objectRepositories.get(pageName);
        if (pageObjects == null || pageObjects.isEmpty()) {
            logger.error("Page not found in Object Repository or has no elements: {}", pageName);
            logger.debug("Available pages: {}", objectRepositories.keySet());
            throw new RuntimeException("Page not found in Object Repository: " + pageName);
        }
        
        ElementLocator elementLocator = pageObjects.get(elementName);
        if (elementLocator == null) {
            logger.error("Element not found in Object Repository: {}.{}", pageName, elementName);
            logger.debug("Available elements for page {}: {}", pageName, pageObjects.keySet());
            throw new RuntimeException("Element not found: " + pageName + "." + elementName);
        }
        
        // Dual-locator strategy: Try ID first, then XPath
        if (elementLocator.hasId()) {
            logger.debug("Using ID locator for element {}.{}: {}", pageName, elementName, elementLocator.getId());
            return By.id(elementLocator.getId());
        } else if (elementLocator.hasXpath()) {
            logger.debug("Using XPath locator for element {}.{}: {}", pageName, elementName, elementLocator.getXpath());
            return By.xpath(elementLocator.getXpath());
        } else {
            String errorMessage = String.format("Unable to locate element using either ID or XPath for element '%s' on page '%s'.", elementName, pageName);
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
    
    /**
     * Gets the XPath string for an element (for debugging purposes and backward compatibility)
     * 
     * @param pageName Name of the page
     * @param elementName Name of the element
     * @return XPath string
     */
    public static String getXPath(String pageName, String elementName) {
        Map<String, ElementLocator> pageObjects = objectRepositories.get(pageName);
        if (pageObjects == null) {
            return null;
        }
        ElementLocator locator = pageObjects.get(elementName);
        return locator != null ? locator.getXpath() : null;
    }
    
    /**
     * Gets the ID string for an element (for debugging purposes)
     * 
     * @param pageName Name of the page
     * @param elementName Name of the element
     * @return ID string
     */
    public static String getId(String pageName, String elementName) {
        Map<String, ElementLocator> pageObjects = objectRepositories.get(pageName);
        if (pageObjects == null) {
            return null;
        }
        ElementLocator locator = pageObjects.get(elementName);
        return locator != null ? locator.getId() : null;
    }
    
    /**
     * Checks if a page is loaded in the Object Repository
     * 
     * @param pageName Name of the page
     * @return True if page exists, false otherwise
     */
    public static boolean isPageLoaded(String pageName) {
        return objectRepositories.containsKey(pageName) && !objectRepositories.get(pageName).isEmpty();
    }
    
    /**
     * Checks if an element exists for a given page
     * 
     * @param pageName Name of the page
     * @param elementName Name of the element
     * @return True if element exists, false otherwise
     */
    public static boolean isElementExists(String pageName, String elementName) {
        Map<String, ElementLocator> pageObjects = objectRepositories.get(pageName);
        return pageObjects != null && pageObjects.containsKey(elementName);
    }
    
    /**
     * Lists all loaded pages
     * 
     * @return Set of loaded page names
     */
    public static java.util.Set<String> getLoadedPages() {
        return objectRepositories.keySet();
    }
    
    /**
     * Lists all elements for a given page
     * 
     * @param pageName Name of the page
     * @return Set of element names for the page
     */
    public static java.util.Set<String> getElementsForPage(String pageName) {
        Map<String, ElementLocator> pageObjects = objectRepositories.get(pageName);
        return pageObjects != null ? pageObjects.keySet() : java.util.Collections.emptySet();
    }
}
