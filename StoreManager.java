import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StoreManager {
    private static final String STORE_FILE = "stores.txt";
    private static Map<String, Store> storesMap = new HashMap<>();
    private static int storeCounter = 1; // Counter to generate storeCode

    // Register Store
    public boolean registerStore(String storeName, String storeEmail, String storeAddress, String storeContactNumber, String sellerID) {
        // Check if seller already has a store
        if (getStoreCodeBySellerID(sellerID) != null) {
            System.out.println("Error: You can only register one store per seller.");
            return false;
        }
        
        String storeCode = generateStoreCode();
        Store newStore = new Store(storeCode, storeName, storeEmail, storeAddress, storeContactNumber, sellerID);
        storesMap.put(storeCode, newStore);
        saveStoresToFile();
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("\nStore registered successfully! Your Store Code is: [ %s ]\n\n", storeCode);
        System.out.println("----------------------------------------------------------------------");
        return true;
    }

    // Save store to file
    public void saveStoresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STORE_FILE))) {
            // Write counter as first line
            writer.write("COUNTER:" + storeCounter);
            writer.newLine();

            // Write store data
            for (Store store : storesMap.values()) {
                writer.write(store.getStoreCode() + ":" + store.getStoreName() + ":" + 
                           store.getStoreEmail() + ":" + store.getStoreAddress() + ":" + 
                           store.getStoreContactNumber() + ":" + store.getSellerID());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving stores to file: " + e.getMessage());
        }
    }

    // Load stores from file
    public void loadStoresFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STORE_FILE))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine && line.startsWith("COUNTER:")) {
                    // Parse counter from first line
                    String counterStr = line.substring("COUNTER:".length()).trim();
                    try {
                        storeCounter = Integer.parseInt(counterStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid counter value in file. Starting from default counter value.");
                    }
                    isFirstLine = false;
                    continue;
                }

                // Parse store data
                String[] data = line.split(":");
                if (data.length == 6) {
                    String storeCode = data[0];
                    String storeName = data[1];
                    String storeEmail = data[2];
                    String storeAddress = data[3];
                    String storeContactNumber = data[4];
                    String sellerID = data[5];

                    Store store = new Store(storeCode, storeName, storeEmail, storeAddress, storeContactNumber, sellerID);
                    storesMap.put(storeCode, store);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while loading stores from file: " + e.getMessage());
        }
    }

    // Get store by code
    public Store getStoreByCode(String storeCode) {
        return storesMap.get(storeCode);
    }

    // Get store code by name
    public String getStoreCodeByName(String storeName) {
        for (Store store : storesMap.values()) {
            if (store.getStoreName().equalsIgnoreCase(storeName)) {
                return store.getStoreCode();
            }
        }
        return null; // Store not found
    }

    // Get all stores
    public Map<String, Store> getAllStores() {
        return storesMap;
    }

    // Get store code by seller ID
    public String getStoreCodeBySellerID(String sellerID) {
        for (Store store : storesMap.values()) {
            if (store.getSellerID().equals(sellerID)) {
                return store.getStoreCode();
            }
        }
        return null; // Return null if no store found for this seller
    }

    // Generate unique store code
    private String generateStoreCode() {
        String storeCode = "ST" + String.format("%05d", storeCounter); // Format as ST00001
        storeCounter++; // Increment counter for the next store
        return storeCode;
    }
}
