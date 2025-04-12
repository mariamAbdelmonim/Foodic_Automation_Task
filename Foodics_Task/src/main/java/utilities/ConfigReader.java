package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    // Declare a static Properties object to hold the configuration properties
    private static Properties properties;

    // Static block to load properties from the config file during class initialization
    static {
        try {
            // Define the path to the config file relative to the project directory
            String path = System.getProperty("user.dir") + "/src/test/resources/config/config.properties";

            // Open the config file for reading
            FileInputStream input = new FileInputStream(path);

            // Initialize the Properties object and load the content from the file
            properties = new Properties();
            properties.load(input);

            // Close the input stream after loading the properties
            input.close();
        } catch (IOException e) {
            // If an error occurs while reading the config file, throw a runtime exception
            throw new RuntimeException("Failed to load config.properties from: " +
                    System.getProperty("user.dir") + "/src/test/resources/config/", e);
        }
    }

    // Method to get the base URL with the language parameter appended
    public static String getUrl() {
        // Fetch the base URL from the properties file
        String baseUrl = properties.getProperty("base.url");

        // If the base URL is not found, throw an exception
        if (baseUrl == null) {
            throw new RuntimeException("base.url not found in config.properties");
        }

        // Append the language query parameter to the URL and return the full URL
        return baseUrl.contains("?") ? baseUrl + "&language=en" : baseUrl + "?language=en";
    }

    // Method to get the value of any given property key
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Method to get the implicit wait time as a long value
    public static long getImplicitWait() {
        // Get the value for "implicit.wait" from the properties file and parse it as a long
        return Long.parseLong(getProperty("implicit.wait"));
    }
}
