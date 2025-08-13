package com.saucedemo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader loads values from both application.properties and credentials.properties
 */
public class ConfigReader {

    private static final Properties config = new Properties();
    private static final Properties credentials = new Properties();

    static {
        try {
            FileInputStream configStream = new FileInputStream("src/test/resources/application.properties");
            config.load(configStream);
            configStream.close();

            FileInputStream credStream = new FileInputStream("src/test/resources/credentials.properties");
            credentials.load(credStream);
            credStream.close();

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config or credentials files.", e);
        }
    }

    /**
     * Retrieves a value from application.properties
     * @param key property key
     * @return String value
     */
    public static String getConfig(String key) {
        return config.getProperty(key);
    }

    /**
     * Retrieves a sensitive credential value
     * @param key credential key
     * @return String value
     */
    public static String getCredential(String key) {
        return credentials.getProperty(key);
    }
}
