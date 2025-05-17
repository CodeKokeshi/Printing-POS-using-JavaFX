package com.pp.s.printingpos;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "/com/pp/s/printingpos/config.properties";
    private static Properties properties;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        properties = new Properties();
        try (InputStream input = ConfigManager.class.getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Unable to find config file: " + CONFIG_FILE);
                return;
            }
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }

    // New method to reload configuration
    public static void reloadConfig() {
        loadConfig();
    }

    public static double getPriceForService(String serviceType, String colorOption) {
        String key = serviceType.toLowerCase() + "." + colorOption.toLowerCase();
        String value = properties.getProperty(key);
        return value != null ? Double.parseDouble(value) : 0.0;
    }

    public static double getImagesSurcharge() {
        String value = properties.getProperty("images.additional");
        return value != null ? Double.parseDouble(value) : 2.0;
    }
}