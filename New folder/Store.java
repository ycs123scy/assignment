public class Store  {
    private String storeCode;
    private String storeName;
    private String storeEmail;
    private String storeAddress;
    private String storeContactNumber;
    private String sellerID;

    public Store(String storeCode, String storeName, String storeEmail, String storeAddress, String storeContactNumber, String sellerID) {
        this.storeCode = storeCode;
        this.storeName = storeName;
        this.storeEmail = storeEmail;
        this.storeAddress = storeAddress;
        this.storeContactNumber = storeContactNumber;
        this.sellerID = sellerID;
    }

    // Getters and Setters
    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getStoreEmail() { return storeEmail; }
    public void setStoreEmail(String storeEmail) { this.storeEmail = storeEmail; }
    
    public String getStoreAddress() { return storeAddress; }
    public void setStoreAddress(String storeAddress) { this.storeAddress = storeAddress; }

    public String getStoreContactNumber() { return storeContactNumber; }
    public void setStoreContactNumber(String storeContactNumber) { this.storeContactNumber = storeContactNumber; }

    public String getSellerID() { return sellerID; }
    public void setSellerID(String sellerID) { this.sellerID = sellerID; }
    
}