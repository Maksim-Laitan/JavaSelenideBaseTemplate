package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class PropertyReader {
    public static final String DEFAULT_PROPERTY_FILE = "config.properties";
    private static final String DOLLAR_PROPERTY_PATTERN = "\\$\\{.*}";
    private static final String DOLLAR_PROPERTY_MARKERS = "^\\$\\{|}";

    public static String getGlobalProperty(String object) {
        String result = readPropertyFromSystem(object);
        if ("N/A".equals(result)) {
            result = readPropertyFromConfigFile(object);
        }
        return result;
    }

    /**
     * Reads a property from a default config file.
     *
     * @param property
     * @return
     * @throws IOException
     */
    public static String readPropertyFromConfigFile(String property) {
        return readPropertyFromFile(DEFAULT_PROPERTY_FILE, property);
    }

    /**
     * Reads a property from a file.
     *
     * @param relativePath the path to the file
     * @param property     property name
     * @return property value or "N/A" if not found
     */
    public static String readPropertyFromFile(String relativePath, String property) {
        Properties data = new Properties();
        String result = "";
        try {
            try (InputStream resource = new FileInputStream(relativePath)) {
                data.load(resource);
                result = data.getProperty(property);
                if (result == null) {
                    result = "N/A";
                }
            } catch (IOException e) {
                throw new AssertionError("Resource file not found: " + relativePath);
            }
        } catch (Exception e) {
            System.out.println("Error reading data " + relativePath + "from file: "+ e);
        }
        return replaceGlobalPropertyValue(result);
    }

    /**
     * Reads a property from environment or system properties.
     *
     * @param propertyName the name of the property.
     * @param defaultValue the value returned in case property is not found
     * @return the value of the property or defaultValue if property is not found.
     */
    public static String readPropertyFromSystem(String propertyName, String defaultValue) {
        String result = System.getenv(propertyName);
        if (result == null) {
            result = System.getProperty(propertyName, defaultValue);
        }
        return result;
    }

    /**
     * Reads a property from environment or system properties.
     *
     * @param propertyName the name of the property.
     * @return the value of the property or null if property is not found.
     */
    public static String readPropertyFromSystem(String propertyName) {
        return readPropertyFromSystem(propertyName, "N/A");
    }

    public static String replaceGlobalPropertyValue(String propertyValue) {
        if (propertyValue.matches(DOLLAR_PROPERTY_PATTERN)) {
            propertyValue = PropertyReader.readPropertyFromSystem(propertyValue.replaceAll(DOLLAR_PROPERTY_MARKERS, ""), propertyValue);
        }
        return propertyValue;
    }

    /**
     * Reads a properties file and return all values as Map
     *
     * @param path the properties file location.
     * @param fileName the name of the properties file (without .properties).
     * @return all values from properties file as Map<String, String>
     */
    public Map<String, String> getPropertiesAsMap(String path, String fileName) {
        DataReader dataReader = new DataReader(path, fileName);
        return dataReader.getInternalDataAsMap();
    }
}
