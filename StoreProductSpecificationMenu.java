import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StoreProductSpecificationMenu {
    public static void ProductSpecificationMenu(String sellerID) {
        ProductManager productManager = new ProductManager();
        ProductSpecificationManager productSpecificationManager = new ProductSpecificationManager();
        StoreManager storeManager = new StoreManager();
        storeManager.loadStoresFromFile();
        
        try {
            productManager.loadProductsFromFile();
            productSpecificationManager.loadProductSpecificationFromFile();
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
            return;
        }

        Scanner input = new Scanner(System.in);
        int productSpecificationChoice = 0;
        boolean choiceChecking = true;

        do {
            try {
                System.out.println("====================================================================================================================================================================================");
                System.out.println("                                                                  - - - P R O D U C T   S P E C I F I C A T I O N   M A N A G E M E N T - - -                                                                  ");
                System.out.println("====================================================================================================================================================================================");
                System.out.println(" [1] Add Product Specification");
                System.out.println(" [2] View Product Specifications");
                System.out.println(" [3] Update Product Specification");
                System.out.println(" [4] Delete Product Specification");
                System.out.println(" [0] Back to Store Menu");
                System.out.print("Choose an option: ");
                
                if (input.hasNextInt()) {
                    productSpecificationChoice = input.nextInt();
                    input.nextLine(); 
                    
                    switch (productSpecificationChoice) {
                        case 1:
                            addProductSpecification(input, productManager, productSpecificationManager, sellerID, storeManager);
                            break;
                        case 2:
                            viewProductSpecifications(productSpecificationManager, sellerID, storeManager, productManager);
                            break;
                        case 3:
                            updateProductSpecification(input, productManager, productSpecificationManager, sellerID, storeManager);
                            break;
                        case 4:
                            deleteProductSpecification(input, productSpecificationManager, sellerID, storeManager);
                            break;
                        case 0:
                            System.out.println("Returning to Store Menu...");
                            choiceChecking = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 0-4.");
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    input.nextLine(); 
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                input.nextLine();
            }
        } while (choiceChecking);
    }

    // Add Product Specification
    private static void addProductSpecification(Scanner input, ProductManager productManager, 
            ProductSpecificationManager productSpecificationManager, String sellerID, StoreManager storeManager) {
        
        String storeCode = storeManager.getStoreCodeBySellerID(sellerID);
        if (storeCode == null) {
            System.out.println("No store found for this seller. Please register a store first.");
            return;
        }

        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - A D D   P R O D U C T   S P E C I F I C A T I O N - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        // Product Code
        String productCode;
        while (true) {
            System.out.print("Enter product code (Pxxxxx format) (0 to back)\t\t: ");
            productCode = input.nextLine().trim();
            
            if (productCode.equals("0")) {
                System.out.println("Product specification addition cancelled.");
                return;
            }
            
            if (productCode.isEmpty()) {
                System.out.println("Product code cannot be empty. Please try again.");
                continue;
            }
            
            if (!productCode.matches("P\\d{5}")) {
                System.out.println("Invalid format. Product code should be in Pxxxxx format (e.g., P00001).");
                continue;
            }
            
            if (productSpecificationManager.getAllProductSpecifications().containsKey(productCode)) {
                System.out.println("Product specification already exists for this code.");
                continue;
            }
            
            if (!productManager.getAllProducts().containsKey(productCode)) {
                System.out.println("No product found with this code. Please register the product first.");
                continue;
            }
            
            // Verify product belongs to seller's store
            Product product = productManager.getProductByCode(productCode);
            if (product == null || !product.getStoreCode().equals(storeCode)) {
                System.out.println("Product doesn't belong to your store or doesn't exist.");
                continue;
            }
            
            break;
        }

        // Weight
        double weight = getPositiveDoubleInput(input, "Enter product weight (kg) (0 to back)\t\t: ");
        if (weight == 0) {
            System.out.println("Product specification addition cancelled.");
            return;
        }
        
        // Height
        double height = getPositiveDoubleInput(input, "Enter product height (cm) (0 to back)\t\t: ");
        if (height == 0) {
            System.out.println("Product specification addition cancelled.");
            return;
        }
        
        // Width
        double width = getPositiveDoubleInput(input, "Enter product width (cm) (0 to back)\t\t: ");
        if (width == 0) {
            System.out.println("Product specification addition cancelled.");
            return;
        }
        
        // Depth
        double depth = getPositiveDoubleInput(input, "Enter product depth (cm) (0 to back)\t\t: ");
        if (depth == 0) {
            System.out.println("Product specification addition cancelled.");
            return;
        }

        // Color
        String color = getValidatedStringInput(input, "Enter product color (0 to back)\t\t\t: ", "[a-zA-Z ]+", 
                "Color should contain only letters and spaces.");
        if (color.equals("0")) {
            System.out.println("Product specification addition cancelled.");
            return;
        }

        // Material
        String material = getValidatedStringInput(input, "Enter product material (0 to back)\t\t: ", "[a-zA-Z ]+", 
                "Material should contain only letters and spaces.");
        if (material.equals("0")) {
            System.out.println("Product specification addition cancelled.");
            return;
        }

        // Register the Product Specification
        ProductSpecification spec = new ProductSpecification(weight, height, width, depth, color, material, productCode, storeCode);
        productSpecificationManager.registerProductSpecification(spec);
        System.out.println("Product specification registered successfully!");
    }

    // View Product Specifications
    private static void viewProductSpecifications(ProductSpecificationManager productSpecificationManager, String sellerID, StoreManager storeManager, ProductManager productManager) {
        String storeCode = storeManager.getStoreCodeBySellerID(sellerID);
        if (storeCode == null) {
            System.out.println("No store found for this seller.");
            return;
        }

        Map<String, Product> storeProducts = new HashMap<>();
        for (Product product : productManager.getAllProducts().values()) {
            if (product.getStoreCode().equals(storeCode)) {
                storeProducts.put(product.getProductCode(), product);
            }
        }

        if (storeProducts.isEmpty()) {
            System.out.println("No products found for your store.");
            return;
        }

        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - V I E W   P R O D U C T   S P E C I F I C A T I O N S - - -                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        System.out.println("====================================================================================================================================================================================");
        System.out.printf("| %-20s | %-30s | %-13s | %-13s | %-13s | %-13s | %-18s | %-18s | %-15s |\n", 
                "Product Code", "Product Name", "Weight", "Height", "Width", "Depth", "Color", "Material", "Store Code");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        for (Product product : storeProducts.values()) {
            ProductSpecification spec = productSpecificationManager.getSpecificationByProductCode(product.getProductCode());
            
            System.out.printf("| %-20s | %-30s |", 
                    product.getProductCode(), 
                    product.getProductName());
            
            if (spec != null) {
                System.out.printf(" %-13.2f | %-13.2f | %-13.2f | %-13.2f | %-18s | %-18s | %-15s |\n",
                        spec.getWeight(),
                        spec.getHeight(),
                        spec.getWidth(),
                        spec.getDepth(),
                        spec.getColor(),
                        spec.getMaterial(),
                        spec.getStoreCode());
            } else {
                System.out.printf(" %-13s | %-13s | %-13s | %-13s | %-18s | %-18s | %-15s |\n",
                        "-", "-", "-", "-", "-", "-", storeCode);
            }
        }
        
        System.out.println("====================================================================================================================================================================================");
        
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    // Update Product Specification
    private static void updateProductSpecification(Scanner input, ProductManager productManager,
            ProductSpecificationManager productSpecificationManager, String sellerID, StoreManager storeManager) {
        
        String storeCode = storeManager.getStoreCodeBySellerID(sellerID);
        if (storeCode == null) {
            System.out.println("No store found for this seller.");
            return;
        }

        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - U P D A T E   P R O D U C T   S P E C I F I C A T I O N - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        // Product Code
        String productCode;
        while (true) {
            System.out.print("Enter product code to update (0 to back)\t\t: ");
            productCode = input.nextLine().trim();
            
            if (productCode.equals("0")) {
                System.out.println("Product specification update cancelled.");
                return;
            }
            
            if (productCode.isEmpty()) {
                System.out.println("Product code cannot be empty. Please try again.");
                continue;
            }
            
            ProductSpecification spec = productSpecificationManager.getSpecificationByProductCode(productCode);
            if (spec == null) {
                System.out.println("Product specification not found. Please try again.");
                continue;
            }
            
            if (!spec.getStoreCode().equals(storeCode)) {
                System.out.println("You can only update specifications for products from your own store. Please try again.");
                continue;
            }
            
            break;
        }

        ProductSpecification currentSpec = productSpecificationManager.getSpecificationByProductCode(productCode);
        
        // New Weight
        System.out.println("\nCurrent values (enter 0 to cancel or keep current value):");
        double newWeight = getUpdatedDoubleInput(input, "Weight (kg)", currentSpec.getWeight());
        if (newWeight == 0) {
            System.out.println("Product specification update cancelled.");
            return;
        }
        
        // New Height
        double newHeight = getUpdatedDoubleInput(input, "Height (cm)", currentSpec.getHeight());
        if (newHeight == 0) {
            System.out.println("Product specification update cancelled.");
            return;
        }
        
        // New Width
        double newWidth = getUpdatedDoubleInput(input, "Width (cm)", currentSpec.getWidth());
        if (newWidth == 0) {
            System.out.println("Product specification update cancelled.");
            return;
        }
        
        // New Depth
        double newDepth = getUpdatedDoubleInput(input, "Depth (cm)", currentSpec.getDepth());
        if (newDepth == 0) {
            System.out.println("Product specification update cancelled.");
            return;
        }
        
        // New Color
        System.out.println("\nCurrent values (enter 0 to cancel or leave blank to keep current value):");
        String newColor = getUpdatedStringInput(input, "Color", currentSpec.getColor());
        if (newColor.equals("0")) {
            System.out.println("Product specification update cancelled.");
            return;
        }
        
        // New Material
        String newMaterial = getUpdatedStringInput(input, "Material", currentSpec.getMaterial());
        if (newMaterial.equals("0")) {
            System.out.println("Product specification update cancelled.");
            return;
        }

        // Update the Product Specification
        ProductSpecification updatedSpec = new ProductSpecification(
                newWeight, newHeight, newWidth, newDepth, 
                newColor, newMaterial, productCode, storeCode);
        
        productSpecificationManager.registerProductSpecification(updatedSpec);
        System.out.println("Product specification updated successfully!");
    }

    // Delete Product Specification
    private static void deleteProductSpecification(Scanner input, 
            ProductSpecificationManager productSpecificationManager, String sellerID, StoreManager storeManager) {
        
        String storeCode = storeManager.getStoreCodeBySellerID(sellerID);
        if (storeCode == null) {
            System.out.println("No store found for this seller.");
            return;
        }

        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - D E L E T E   P R O D U C T   S P E C I F I C A T I O N - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        String productCode;
        while (true) {
            System.out.print("Enter product code to delete (0 to back)\t\t: ");
            productCode = input.nextLine().trim();
            
            if (productCode.equals("0")) {
                System.out.println("Product specification deletion cancelled.");
                return;
            }
            
            if (productCode.isEmpty()) {
                System.out.println("Product code cannot be empty. Please try again.");
                continue;
            }
            
            ProductSpecification spec = productSpecificationManager.getSpecificationByProductCode(productCode);
            if (spec == null) {
                System.out.println("Product specification not found. Please try again.");
                continue;
            }
            
            if (!spec.getStoreCode().equals(storeCode)) {
                System.out.println("You can only delete specifications for products from your own store. Please try again.");
                continue;
            }
            
            break;
        }

        // Confirm deletion
        System.out.printf("Are you sure you want to delete this specification? (Yes/No): ");
        String confirmation = input.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("0")) {
            System.out.println("Deletion cancelled.");
            return;
        } else if (confirmation.equals("yes")) {
            productSpecificationManager.getAllProductSpecifications().remove(productCode);
            productSpecificationManager.saveProductSpecificationToFile();
            System.out.println("Product specification deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Make sure input is can only be a positive double
    private static double getPositiveDoubleInput(Scanner input, String prompt) {
        double value = 0;
        while (true) {
            System.out.print(prompt);
            try {
                String inputStr = input.nextLine().trim();
                if (inputStr.equals("0")) {
                    return 0;
                }
                
                value = Double.parseDouble(inputStr);
                if (value <= 0) {
                    System.out.println("Value must be greater than zero. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // Make sure input string cannot be empty and check if want to cancel
    private static String getValidatedStringInput(Scanner input, String prompt, String regex, String errorMsg) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = input.nextLine().trim();
            
            if (value.equals("0")) {
                return "0";
            }
            
            if (value.isEmpty()) {
                System.out.println("Value cannot be empty. Please try again.");
                continue;
            }
            
            if (!value.matches(regex)) {
                System.out.println(errorMsg);
                continue;
            }
            
            break;
        }
        return value;
    }

    // Make sure input is can only be a positive double and check if want to cancel or keep current value
    private static double getUpdatedDoubleInput(Scanner input, String fieldName, double currentValue) {
        double newValue = 0;
        while (true) {
            System.out.printf("Enter new %s (current: %.2f) (0 to cancel)\t: ", fieldName, currentValue);
            try {
                String inputStr = input.nextLine().trim();
                if (inputStr.isEmpty()) {
                    return currentValue;
                }
                
                if (inputStr.equals("0")) {
                    return 0;
                }
                
                newValue = Double.parseDouble(inputStr);
                if (newValue <= 0) {
                    System.out.println("Value must be greater than zero. Please try again.");
                    continue;
                }
                return newValue;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    // Make sure input string cannot be empty and check if want to cancel or keep current value
    private static String getUpdatedStringInput(Scanner input, String fieldName, String currentValue) {
        System.out.printf("Enter new %s (current: %s) (0 to cancel)\t\t: ", fieldName, currentValue);
        String newValue = input.nextLine().trim();
        if (newValue.equals("0")) {
            return "0";
        }
        return newValue.isEmpty() ? currentValue : newValue;
    }
}