public class ProductSpecification {
    private double weight;
    private double height;
    private double width;
    private double depth;
    private String color;
    private String material;
    private String productCode;
    private String storeCode;

    public ProductSpecification(double weight, double height, double width, double depth, String color, 
    String material, String productCode, String storeCode) {
        this.weight = weight;
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.color = color;
        this.material = material;
        this.productCode = productCode;
        this.storeCode = storeCode;
    }

    // Getters and Setters
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getDepth() { return depth; }
    public void setDepth(double depth) { this.depth = depth; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }

    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }

}
