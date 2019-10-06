package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class DataReader {
    /*
    This functionality is not checked.
     */

    public DataReader(String path, String fileName) {
        readData(path, fileName);
    }

    private Properties internalData = new Properties();

//    TODO refactor code for resource variable
    protected void readData(String path, String fileName) {

        String relativePath = String.format("/%s/%s.properties", path, fileName);
        try (InputStream resource = this.getClass().getResourceAsStream(relativePath)) {
            if (null == resource) {
                throw new AssertionError("Resource file not found: " + relativePath);
            }
            internalData.load(new InputStreamReader(resource, StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Error reading data " + relativePath + "from file: "+ e);
        }
        internalData.forEach((key, value) -> internalData.setProperty(key.toString(), value.toString()));
    }

    public String getValue(String key) {
        return Optional.ofNullable(internalData.getProperty(key)).orElseThrow(() -> new AssertionError(key + " property not found!"));
    }

    public Map<String, String> getInternalDataAsMap() {
        Map<String, String> map = new HashMap<>();
        for (final String propName : internalData.stringPropertyNames()) {
            map.put(propName, String.valueOf(internalData.getProperty(propName)));
        }
        return map;
    }
}
