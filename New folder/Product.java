public class Product {
    private String productCode;
    private String productName;
    private double unitPrice;
    private int productStock;
    private String category;
    private String storeCode;
    private ProductSpecification productSpecification;

    private static final String[] ALLOWED_CATEGORIES = {"Living Room", "Dining Room", "Kitchen", "Bathroom", "Bedroom", "Others"};

    public Product(String productCode, String productName, double unitPrice, int productStock, String category, String storeCode) {
        this.productCode = productCode;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.productStock = productStock;
        this.category = category;
        this.storeCode = storeCode;
        this.productSpecification = new ProductSpecification(0, 0, 0, 0, "", "", productCode, storeCode);
    }

    // Getters and Setters
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    // Get Product Name By Code
    public static String getProductNameByCode(String productCode) {
        ProductManager productManager = new ProductManager();
        Product product = productManager.getProductByCode(productCode);
        return (product != null) ? product.getProductName() : null;
    }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public int getProductStock() { return productStock; }
    public void setProductStock(int productStock) { this.productStock = productStock; }

    public static String[] getAllowedCategories() { return ALLOWED_CATEGORIES; }

    public static String getCategoryByChoice(int choice) {
        if (choice < 1 || choice > ALLOWED_CATEGORIES.length) {
            throw new IllegalArgumentException("Invalid category choice");
        }
        return ALLOWED_CATEGORIES[choice - 1];
    }

    public String getCategory() { return category; }
    public void setCategory(String category) {
        for (String allowedCategory : ALLOWED_CATEGORIES) {
            if (allowedCategory.equalsIgnoreCase(category)) {
                this.category = category;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid category. Allowed categories are: Living Room, Dining Room, Kitchen, Bathroom, Bedroom, Others.");
    }

    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }

    public ProductSpecification getProductSpecification() { return productSpecification; }
    public void setProductSpecification(ProductSpecification productSpecification) { this.productSpecification = productSpecification; }

}