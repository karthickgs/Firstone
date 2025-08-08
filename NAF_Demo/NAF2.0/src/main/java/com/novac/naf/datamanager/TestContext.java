
/**
 * Framework Name: Novac Automation Framework
 * Author: SriramR-NOVAC
 * File Name: TestContext.java
 * Description: Thread-safe utility for storing and retrieving dynamic test data at runtime
 */

package com.novac.naf.datamanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe context storage for dynamic test data that can be shared across test steps and test cases
 */
public class TestContext {
    private static final Logger logger = LoggerFactory.getLogger(TestContext.class);
    
    // Thread-safe storage for dynamic values
    private static final ConcurrentMap<String, Object> contextData = new ConcurrentHashMap<>();
    
    // Prefix for cross-test case data sharing
    private static final String CROSS_TESTCASE_PREFIX = "GLOBAL.";
    
    /**
     * Stores a value in the test context with the current test case scope
     * 
     * @param key The key to store the value under
     * @param value The value to store
     */
    public static void store(String key, Object value) {
        if (key == null || key.trim().isEmpty()) {
            logger.warn("Attempted to store value with null or empty key");
            return;
        }
        
        contextData.put(key, value);
        logger.debug("Stored value in context: {} = {}", key, value);
    }
    
    /**
     * Stores a value that can be accessed across test cases
     * 
     * @param key The key to store the value under
     * @param value The value to store
     */
    public static void storeGlobal(String key, Object value) {
        if (key == null || key.trim().isEmpty()) {
            logger.warn("Attempted to store global value with null or empty key");
            return;
        }
        
        String globalKey = CROSS_TESTCASE_PREFIX + key;
        contextData.put(globalKey, value);
        logger.debug("Stored global value in context: {} = {}", globalKey, value);
    }
    
    /**
     * Retrieves a value from the test context
     * 
     * @param key The key to retrieve
     * @return The stored value or null if not found
     */
    public static Object retrieve(String key) {
        if (key == null || key.trim().isEmpty()) {
            logger.warn("Attempted to retrieve value with null or empty key");
            return null;
        }
        
        Object value = contextData.get(key);
        logger.debug("Retrieved value from context: {} = {}", key, value);
        return value;
    }
    
    /**
     * Retrieves a value as String from the test context
     * 
     * @param key The key to retrieve
     * @return The stored value as String or null if not found
     */
    public static String retrieveAsString(String key) {
        Object value = retrieve(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * Retrieves a global value that was stored for cross-test case access
     * 
     * @param key The key to retrieve (without GLOBAL prefix)
     * @return The stored value or null if not found
     */
    public static Object retrieveGlobal(String key) {
        if (key == null || key.trim().isEmpty()) {
            logger.warn("Attempted to retrieve global value with null or empty key");
            return null;
        }
        
        String globalKey = CROSS_TESTCASE_PREFIX + key;
        Object value = contextData.get(globalKey);
        logger.debug("Retrieved global value from context: {} = {}", globalKey, value);
        return value;
    }
    
    /**
     * Retrieves a global value as String
     * 
     * @param key The key to retrieve (without GLOBAL prefix)
     * @return The stored value as String or null if not found
     */
    public static String retrieveGlobalAsString(String key) {
        Object value = retrieveGlobal(key);
        return value != null ? value.toString() : null;
    }
    
    /**
     * Checks if a key exists in the context
     * 
     * @param key The key to check
     * @return true if the key exists, false otherwise
     */
    public static boolean contains(String key) {
        return key != null && contextData.containsKey(key);
    }
    
    /**
     * Checks if a global key exists in the context
     * 
     * @param key The key to check (without GLOBAL prefix)
     * @return true if the global key exists, false otherwise
     */
    public static boolean containsGlobal(String key) {
        return key != null && contextData.containsKey(CROSS_TESTCASE_PREFIX + key);
    }
    
    /**
     * Removes a specific key from the context
     * 
     * @param key The key to remove
     */
    public static void remove(String key) {
        if (key != null) {
            Object removedValue = contextData.remove(key);
            logger.debug("Removed key from context: {} (value was: {})", key, removedValue);
        }
    }
    
    /**
     * Clears all non-global context data (used between test cases)
     */
    public static void clear() {
        // Remove only non-global entries
        contextData.entrySet().removeIf(entry -> !entry.getKey().startsWith(CROSS_TESTCASE_PREFIX));
        logger.info("Cleared test-level context data (keeping global data)");
    }
    
    /**
     * Clears all context data including global data (used at test suite end)
     */
    public static void clearAll() {
        int size = contextData.size();
        contextData.clear();
        logger.info("Cleared all context data ({} entries)", size);
    }
    
    /**
     * Gets the current size of the context
     * 
     * @return Number of entries in context
     */
    public static int size() {
        return contextData.size();
    }
    
    /**
     * Gets all keys currently stored in context (for debugging)
     * 
     * @return Array of all keys
     */
    public static String[] getAllKeys() {
        return contextData.keySet().toArray(new String[0]);
    }
}
