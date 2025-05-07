import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ProductSpecificationManager {
    private static final String PRODUCT_SPECIFICATION_FILE = "product_specifications.txt";
    private static Map<String, ProductSpecification> productSpecificationsMap = new HashMap<>();

    // Register Product Specification
    public void registerProductSpecification(ProductSpecification spec) {
        productSpecificationsMap.put(spec.getProductCode(), spec);
        saveProductSpecificationToFile();
    }

    // Save product specification to file
    public void saveProductSpecificationToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_SPECIFICATION_FILE))) {
            for (ProductSpecification spec : productSpecificationsMap.values()) {
                writer.write(spec.getProductCode() + ":" + spec.getWeight() + ":" + 
                           spec.getHeight() + ":" + spec.getWidth() + ":" + 
                           spec.getDepth() + ":" + spec.getColor() + ":" + 
                           spec.getMaterial() + ":" + spec.getStoreCode());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving product specifications: " + e.getMessage());
        }
    }

    // Load product specification from file
    public void loadProductSpecificationFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_SPECIFICATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(":");
                if (data.length == 8) {
                    ProductSpecification spec = new ProductSpecification(
                        Double.parseDouble(data[1]), // weight
                        Double.parseDouble(data[2]), // height
                        Double.parseDouble(data[3]), // width
                        Double.parseDouble(data[4]), // depth
                        data[5], // color
                        data[6], // material
                        data[0], // productCode
                        data[7]  // storeCode
                    );
                    productSpecificationsMap.put(data[0], spec);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading product specifications: " + e.getMessage());
        }
    }

    public Map<String, ProductSpecification> getAllProductSpecifications() {
        return productSpecificationsMap;
    }

    public ProductSpecification getSpecificationByProductCode(String productCode) {
        return productSpecificationsMap.get(productCode);
    }
}