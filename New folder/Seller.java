public class Seller extends User {
    private String sellerID;
    private String bankAccount;

    public Seller(String name, String email, String password, String contactNumber, String address, String sellerID, String bankAccount) {
        super(name, email, password, contactNumber, address);
        this.sellerID = sellerID;
        this.bankAccount = bankAccount;
    }

    // Getters and Setters
    public String getSellerID() { return sellerID; }
    public void setSellerID(String sellerID) { this.sellerID = sellerID; }

    public String getBankAccount() { return bankAccount; }
    public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }

}