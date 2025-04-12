package utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestDataProvider {

    // Path to the test data file (JSON format)
    private static final String TEST_DATA_FILE = "src/test/resources/testdata/testdata.json";

    // Map to store test data with key-value pairs
    private static Map<String, Object> testDataMap = new HashMap<>();

    // Flag to check if the test data has already been initialized
    private static boolean isInitialized = false;

    // Static block to initialize test data when the class is loaded
    static {
        initializeTestData();
    }

    // Method to initialize the test data by reading from the JSON file
    private static void initializeTestData() {
        if (!isInitialized) {  // Check if already initialized
            ObjectMapper mapper = new ObjectMapper();  // Create an ObjectMapper instance
            try {
                // Read the JSON file and convert it into a JsonNode
                JsonNode rootNode = mapper.readTree(new File(TEST_DATA_FILE));

                // Parse the JSON node and populate the test data map
                parseJsonNode(rootNode, "");

                // Mark the initialization as complete
                isInitialized = true;
            } catch (IOException e) {
                // Handle any I/O exceptions that may occur during file reading
                throw new RuntimeException("Failed to load test data file: " + TEST_DATA_FILE, e);
            }
        }
    }

    // Recursive method to parse a JSON node and store data in the map
    private static void parseJsonNode(JsonNode node, String currentPath) {
        // If the node is a JSON object, iterate over its fields
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String newPath = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
                parseJsonNode(entry.getValue(), newPath);  // Recurse with the new path
            }
        }
        // If the node is a value node, add it to the map with the current path as the key
        else if (node.isValueNode()) {
            testDataMap.put(currentPath, convertJsonValue(node));  // Convert and store the value
        }
    }

    // Method to convert a JsonNode value to an appropriate Java type (String, Integer, Boolean, etc.)
    private static Object convertJsonValue(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();  // Return text as String
        } else if (node.isInt()) {
            return node.asInt();  // Return integer
        } else if (node.isDouble()) {
            return node.asDouble();  // Return double
        } else if (node.isBoolean()) {
            return node.asBoolean();  // Return boolean
        }
        return node.toString();  // Default case for other types (e.g., arrays, objects)
    }

    // Method to retrieve test data based on the key
    public static String getTestData(String key) {
        // Check if the key exists in the map
        if (!testDataMap.containsKey(key)) {
            throw new IllegalArgumentException("Test data key not found: " + key);  // Throw exception if key not found
        }
        // Return the test data as a String
        return testDataMap.get(key).toString();
    }
}
