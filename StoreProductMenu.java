import java.util.Map;
import java.util.Scanner;

public class StoreProductMenu {
    private static String currentStoreCode = "";

    public static void ProductMenu(String sellerID) {
        ProductManager productManager = new ProductManager();
        productManager.loadProductsFromFile();

        StoreManager storeManager = new StoreManager();
        storeManager.loadStoresFromFile();

        currentStoreCode = storeManager.getStoreCodeBySellerID(sellerID);
        if (currentStoreCode == null) {
            System.out.println("No store found for this seller. Please register a store first.");
            return;
        }

        Scanner input = new Scanner(System.in);
        int productChoice = 0;
        boolean exitMenu = false;

        do {
            try {
                System.out.println("====================================================================================================================================================================================");
                System.out.println("                                                                   - - - P R O D U C T   M A N A G E M E N T - - -                                                                  ");
                System.out.println("====================================================================================================================================================================================");
                System.out.println(" [1] Add Product");
                System.out.println(" [2] Add Stock");
                System.out.println(" [3] View Products");
                System.out.println(" [4] Update Product");
                System.out.println(" [5] Delete Product");
                System.out.println(" [0] Back to Store Menu");
                System.out.print("Choose an option: ");
                productChoice = input.nextInt();
                input.nextLine();

                switch (productChoice) {
                    case 1:
                        addProduct(input, productManager, sellerID);
                        break;
                    case 2:
                        addStock(input, productManager, sellerID);
                        break;
                    case 3:
                        viewProducts(productManager, sellerID);
                        break;
                    case 4:
                        updateProduct(input, productManager, sellerID);
                        break;
                    case 5:
                        deleteProduct(input, productManager, sellerID);
                        break;
                    case 0:
                        exitMenu = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 0-5.");
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid input. Please try again.");
                input.nextLine();
            }
        } while (!exitMenu);
    }

    // Add Product
    private static void addProduct(Scanner input, ProductManager productManager, String sellerID) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - A D D   N E W   P R O D U C T - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        // Product Name
        String productName = "";
        while (productName.isEmpty()) {
            System.out.print("Enter product name (0 to back)\t\t\t\t: ");
            productName = input.nextLine().trim();
            if (productName.equals("0")) {
                System.out.println("Product addition cancelled.");
                return;
            }
            if (productName.isEmpty()) {
                System.out.println("Product name cannot be empty. Please try again.");
            }
        }

        // Unit Price
        double unitPrice = 0;
        while (unitPrice <= 0) {
            System.out.print("Enter unit price (RM) (0 to back)\t\t\t: ");
            try {
                String priceInput = input.nextLine();
                if (priceInput.equals("0")) {
                    System.out.println("Product addition cancelled.");
                    return;
                }
                unitPrice = Double.parseDouble(priceInput);
                if (unitPrice <= 0) {
                    System.out.println("Price must be greater than 0. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid price. Please enter a number.");
            }
        }

        // Product Stock
        int productStock = -1;
        while (productStock < 0) {
            System.out.print("Enter initial stock (0 to back)\t\t\t\t: ");
            try {
                String stockInput = input.nextLine();
                if (stockInput.equals("0")) {
                    System.out.println("Product addition cancelled.");
                    return;
                }
                productStock = Integer.parseInt(stockInput);
                if (productStock < 0) {
                    System.out.println("Stock cannot be negative. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid stock quantity. Please enter a whole number.");
            }
        }

        // Category
        String category = "";
        String[] validCategories = {"Living Room", "Dining Room", "Kitchen", "Bathroom", "Bedroom", "Others"};
        while (category.isEmpty()) {
            System.out.print("Enter category (Living Room, Dining Room, Kitchen, Bathroom, Bedroom, Others) (0 to back): ");
            category = input.nextLine().trim();
            
            if (category.equals("0")) {
                System.out.println("Product addition cancelled.");
                return;
            }
            
            boolean valid = false;
            for (String validCat : validCategories) {
                if (category.equalsIgnoreCase(validCat)) {
                    valid = true;
                    category = validCat;
                    break;
                }
            }
            
            if (!valid) {
                System.out.println("Invalid category. Please choose from the listed options.");
                category = "";
            }
        }

        productManager.registerProduct(productName, unitPrice, productStock, category, currentStoreCode);
    }

    // Add Stock
    private static void addStock(Scanner input, ProductManager productManager, String sellerID) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - A D D   S T O C K - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        // Product Code
        String productCode = "";
        while (productCode.isEmpty()) {
            System.out.print("Enter product code (0 to back)\t\t\t\t: ");
            productCode = input.nextLine().trim();
            if (productCode.equals("0")) {
                System.out.println("Stock addition cancelled.");
                return;
            }
        }

        Product product = productManager.getProductByCode(productCode);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        if (!product.getStoreCode().equals(currentStoreCode)) {
            System.out.println("You can only add stock to products in your own store.");
            return;
        }

        // Additional Stock
        int additionalStock = -1;
        while (additionalStock < 0) {
            System.out.printf("Current stock: %d. Enter additional stock (0 to back)\t: ", product.getProductStock());
            try {
                String stockInput = input.nextLine();
                if (stockInput.equals("0")) {
                    System.out.println("Stock addition cancelled.");
                    return;
                }
                additionalStock = Integer.parseInt(stockInput);
                if (additionalStock < 0) {
                    System.out.println("Additional stock cannot be negative. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        product.setProductStock(product.getProductStock() + additionalStock);
        productManager.saveProductsToFile();
        System.out.printf("Stock updated successfully. New stock: %d\n", product.getProductStock());
    }

    // View Products
    private static void viewProducts(ProductManager productManager, String sellerID) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - V I E W   P R O D U C T S - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        Map<String, Product> products = productManager.getAllProducts();
        
        System.out.println("====================================================================================================================================================================================");
        System.out.printf("| %-30s | %-60s | %-15s | %-30s | %-30s |%n", 
                        "Product Code", "Product Name", "Unit Price", "Stock", "Category");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        boolean hasProducts = false;
        for (Product product : products.values()) {
            if (product.getStoreCode().equals(currentStoreCode)) {
                hasProducts = true;
                System.out.printf("| %-30s | %-60s | RM%-13.2f | %-30d | %-30s |%n",
                                product.getProductCode(),
                                product.getProductName(),
                                product.getUnitPrice(),
                                product.getProductStock(),
                                product.getCategory());
            }
        }

        if (!hasProducts) {
            System.out.println("| No products found in your store.                                                                                                                                                  |");
        }
        System.out.println("====================================================================================================================================================================================");
        
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    // Update Product
    private static void updateProduct(Scanner input, ProductManager productManager, String sellerID) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - U P D A T E   P R O D U C T - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        // Product Code
        String productCode = "";
        while (productCode.isEmpty()) {
            System.out.print("Enter product code to update (0 to back)\t\t: ");
            productCode = input.nextLine().trim();
            if (productCode.equals("0")) {
                System.out.println("Product update cancelled.");
                return;
            }
        }

        Product product = productManager.getProductByCode(productCode);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        if (!product.getStoreCode().equals(currentStoreCode)) {
            System.out.println("You can only update products in your own store.");
            return;
        }

        // Product Name
        System.out.printf("Current name: %s. Enter new name (or press Enter to keep current, 0 to cancel): ", product.getProductName());
        String newName = input.nextLine().trim();
        if (newName.equals("0")) {
            System.out.println("Product update cancelled.");
            return;
        }
        if (newName.isEmpty()) {
            newName = product.getProductName();
        }

        // Unit Price
        double newPrice = product.getUnitPrice();
        while (true) {
            System.out.printf("Current price: RM%.2f. Enter new price (or 0 to cancel, -1 to keep current): ", product.getUnitPrice());
            try {
                String priceInput = input.nextLine();
                if (priceInput.equals("0")) {
                    System.out.println("Product update cancelled.");
                    return;
                }
                double inputPrice = Double.parseDouble(priceInput);
                if (inputPrice > 0) {
                    newPrice = inputPrice;
                    break;
                } else if (inputPrice == -1) {
                    break;
                } else {
                    System.out.println("Price must be positive. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // Stock
        int newStock = product.getProductStock();
        while (true) {
            System.out.printf("Current stock: %d. Enter new stock (or 0 to cancel, -1 to keep current): ", product.getProductStock());
            try {
                String stockInput = input.nextLine();
                if (stockInput.equals("0")) {
                    System.out.println("Product update cancelled.");
                    return;
                }
                int inputStock = Integer.parseInt(stockInput);
                if (inputStock >= 0) {
                    newStock = inputStock;
                    break;
                } else if (inputStock == -1) {
                    break;
                } else {
                    System.out.println("Stock cannot be negative. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }

        // Category
        String newCategory = product.getCategory();
        String[] validCategories = {"Living Room", "Dining Room", "Kitchen", "Bathroom", "Bedroom", "Others"};
        while (true) {
            System.out.printf("Current category: %s. Enter new category (or press Enter to keep current, 0 to cancel): ", product.getCategory());
            String inputCategory = input.nextLine().trim();
            
            if (inputCategory.equals("0")) {
                System.out.println("Product update cancelled.");
                return;
            }
            
            if (inputCategory.isEmpty()) {
                break;
            }
            
            boolean valid = false;
            for (String validCat : validCategories) {
                if (inputCategory.equalsIgnoreCase(validCat)) {
                    valid = true;
                    newCategory = validCat;
                    break;
                }
            }
            
            if (valid) {
                break;
            } else {
                System.out.println("Invalid category. Please choose from: Living Room, Dining Room, Kitchen, Bathroom, Bedroom, Others");
            }
        }

        productManager.updateProduct(productCode, newName, newPrice, newStock, newCategory, currentStoreCode);
    }

    // Delete Product
    private static void deleteProduct(Scanner input, ProductManager productManager, String sellerID) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - D E L E T E   P R O D U C T - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");
        
        // Product Code
        String productCode = "";
        while (productCode.isEmpty()) {
            System.out.print("Enter product code to delete (0 to back)\t\t: ");
            productCode = input.nextLine().trim();
            if (productCode.equals("0")) {
                System.out.println("Product deletion cancelled.");
                return;
            }
        }

        Product product = productManager.getProductByCode(productCode);
        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        if (!product.getStoreCode().equals(currentStoreCode)) {
            System.out.println("You can only delete products from your own store.");
            return;
        }

        // Confirm Deletion
        System.out.printf("Are you sure you want to delete product %s (%s)? (Yes/No): ", productCode, product.getProductName());
        String confirmation = input.nextLine().trim().toLowerCase();
        if (confirmation.equals("0")) {
            System.out.println("Product deletion cancelled.");
            return;
        }
        if (confirmation.equals("yes")) {
            productManager.deleteProduct(productCode);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}