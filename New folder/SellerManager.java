import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SellerManager {
    private static final String SELLER_FILE = "sellers.txt";
    private static Map<String, Seller> sellersMap = new HashMap<>();
    private static int sellerCounter = 1;

    // Register Seller
    public void registerSeller(String name, String email, String password, String contactNumber, String address, String bankAccount) {
        String sellerID = generateSellerID();
        Seller newSeller = new Seller(name, email, password, contactNumber, address, sellerID, bankAccount);
        sellersMap.put(sellerID, newSeller);
        saveSellersToFile(); // Save to file after registration
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("\nSeller registered successfully! Your Seller ID is: [ %s ]\n\n", sellerID);
        System.out.println("----------------------------------------------------------------------");

    }

    // Login Seller
    public boolean loginSeller(String sellerID, String password) {
        Seller seller = sellersMap.get(sellerID);
        if (seller != null && seller.getPassword().equals(password)) {
            System.out.println("\nLogin successful! Welcome, " + seller.getName() + "!");
            return true;
        } else {
            System.out.println("Invalid Seller ID or Password.");
            return false;
        }
    }

    // Generate unique Seller ID
    private String generateSellerID() {
        String sellerID = "S" + String.format("%05d", sellerCounter); // Format as S00001, S00002, etc.
        sellerCounter++; // Increment counter for the next seller
        return sellerID;
    }

    // Save sellers to file
    private void saveSellersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SELLER_FILE))) {
            // Write the counter as the first line
            writer.write("COUNTER:" + sellerCounter);
            writer.newLine();

            // Write seller data
            for (Seller seller : sellersMap.values()) {
                writer.write(seller.getSellerID() + ":" + seller.getName() + ":" + seller.getPassword() + ":" + 
                seller.getEmail() + ":" + seller.getContactNumber() + ":" + seller.getAddress() + ":" + seller.getBankAccount());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving sellers to file: " + e.getMessage());
        }
    }

    // Load sellers from file
    public void loadSellersFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SELLER_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine && line.startsWith("COUNTER:")) {
                    // Parse the counter from the first line
                    String counterStr = line.substring("COUNTER:".length()).trim();
                    try {
                        sellerCounter = Integer.parseInt(counterStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid counter value in file. Starting from default counter value.");
                    }
                    isFirstLine = false;
                    continue;
                }

                // Parse seller data
                String[] parts = line.split(":");
                if (parts.length == 7) {
                    String sellerID = parts[0];
                    String name = parts[1];
                    String password = parts[2];
                    String email = parts[3];
                    String contactNumber = parts[4];
                    String address = parts[5];
                    String bankAccount = parts[6];

                    Seller seller = new Seller(name, email, password, contactNumber, address, sellerID, bankAccount);
                    sellersMap.put(sellerID, seller);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading sellers from file: " + e.getMessage());
        }
    }
}