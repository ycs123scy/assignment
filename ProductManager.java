import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ProductManager {
    private static final String PRODUCT_FILE = "products.txt";
    private static Map<String, Product> productsMap = new HashMap<>();
    private static int productCounter = 1; // Counter to generate product codes

    // Register Product
    public void registerProduct(String productName, double unitPrice, int productStock, String category, String storeCode) {
        String productCode = generateProductCode();
        Product product = new Product(productCode, productName, unitPrice, productStock, category, storeCode);
        productsMap.put(productCode, product);
        saveProductsToFile();
        System.out.println("Product registered successfully! Your Product Code is: " + productCode);
    }

    // Get all products
    public Map<String, Product> getAllProducts() {
        return productsMap;
    }

    // Get product by code
    public Product getProductByCode(String productCode) {
        return productsMap.get(productCode);
    }

    // Update Product
    public void updateProduct(String productCode, String productName, double unitPrice, int productStock, String category, String storeCode) {
        if (productsMap.containsKey(productCode)) {
            Product product = productsMap.get(productCode);
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setProductStock(productStock);
            product.setCategory(category);
            product.setStoreCode(storeCode);
            saveProductsToFile();
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }

    // Delete Product
    public void deleteProduct(String productCode) {
        if (productsMap.containsKey(productCode)) {
            productsMap.remove(productCode);
            saveProductsToFile();
            System.out.println("Product deleted successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }

    // Save products to file
    public void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            // Write counter as first line
            writer.write("COUNTER:" + productCounter);
            writer.newLine();

            // Write product data
            for (Product product : productsMap.values()) {
                writer.write(product.getProductCode() + ":" + product.getProductName() + ":" + 
                product.getUnitPrice() + ":" + product.getProductStock() + ":" + product.getCategory() + ":" +
                product.getStoreCode());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving products to file: " + e.getMessage());
        }
    }

    // Load products from file
    public void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine && line.startsWith("COUNTER:")) {
                    // Parse counter from first line
                    String counterStr = line.substring("COUNTER:".length()).trim();
                    try {
                        productCounter = Integer.parseInt(counterStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid counter value in file. Starting from default counter value.");
                    }
                    isFirstLine = false;
                    continue;
                }

                // Parse product data
                String[] data = line.split(":");
                if (data.length == 6) {
                    String productCode = data[0];
                    String productName = data[1];
                    double unitPrice = Double.parseDouble(data[2]);
                    int productStock = Integer.parseInt(data[3]);
                    String category = data[4];
                    String storeCode = data[5];

                    Product product = new Product(productCode, productName, unitPrice, productStock, category, storeCode);
                    productsMap.put(productCode, product);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading products from file: " + e.getMessage());
        }
    }

    // Generate a unique product code
    private String generateProductCode() {
        String productCode = "P" + String.format("%05d", productCounter); // Format as P00001, P00002, etc.
        productCounter++; // Increment counter for the next product
        return productCode;
    }
}