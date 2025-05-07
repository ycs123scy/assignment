import java.util.Scanner;

public class StoreProfileMenu {
    public static void StoreMenu(String sellerID) {
        StoreManager storeManager = new StoreManager();
        storeManager.loadStoresFromFile();
        Scanner input = new Scanner(System.in);
        int storeChoice = 0;
        boolean choiceChecking = true;

        do { 
            try {
                System.out.println("====================================================================================================================================================================================");
                System.out.println("                                                                   - - - S T O R E   P R O F I L E   M E N U - - -                                                                  ");
                System.out.println("====================================================================================================================================================================================");
                System.out.println(" [1] Register Store");
                System.out.println(" [2] Login Stores");
                System.out.println(" [0] Back to Seller Menu");
                System.out.print("Choose an option: ");
                storeChoice = input.nextInt();
                input.nextLine();

                switch (storeChoice) {
                    case 1:
                        if (storeManager.getStoreCodeBySellerID(sellerID) != null) {
                            System.out.println("You already have a registered store. Each seller can only have one store.");
                            break;
                        }
                        registerStore(input, storeManager, sellerID);
                        break;
                    case 2:
                        boolean hasStores = false;
                        for (Store store : storeManager.getAllStores().values()) {
                            if (store.getSellerID().equals(sellerID)) {
                                hasStores = true;
                                break;
                            }
                        }
                        
                        if (hasStores) {
                            StoreConfigMenu.ConfigStoreMenu(sellerID);
                        } else {
                            System.out.println("You have not registered a store. Please register a store first.");
                        }
                        break;
                    case 0:
                        SellerProfileMenu.SellerMenu();
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                input.nextLine();
            }
        } while (choiceChecking);

        input.close();
    }

    // Register Store
    private static void registerStore(Scanner input, StoreManager storeManager, String sellerID) {
        String storeName="", storeEmail="", storeAddress="", storeContactNumber="";

        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - S T O R E   R E G I S T R A T I O N  - - -                                                                  ");
        System.out.println("====================================================================================================================================================================================");

        // Get Store Name
        while (storeName.isEmpty()) {
            System.out.print("Enter store name (0 to back)\t\t\t\t: ");
            storeName = input.nextLine().trim();
            if (storeName.equals("0")) {
                System.out.println("Store registration cancelled.");
                return;
            }
            if (storeName.isEmpty()) {
                System.out.println("Store name cannot be empty. Please try again.");
            }
        }

        // Get Store Email
        while (storeEmail.isEmpty() || !isValidEmail(storeEmail)) {
            System.out.print("Enter store email (0 to back)\t\t\t\t: ");
            storeEmail = input.nextLine().trim();
            if (storeEmail.equals("0")) {
                System.out.println("Store registration cancelled.");
                return;
            }
            if (storeEmail.isEmpty()) {
                System.out.println("Store email cannot be empty. Please try again.");
            } else if (!isValidEmail(storeEmail)) {
                System.out.println("Invalid email format. Please try again.");
            }
        }

        // Get Store Contact Number
        while (storeContactNumber.isEmpty() || storeContactNumber.length() < 10 || !isAllDigits(storeContactNumber)) {
            System.out.print("Enter store contact number (without -) (0 to back)\t: ");
            storeContactNumber = input.nextLine().trim();
            if (storeContactNumber.equals("0")) {
                System.out.println("Store registration cancelled.");
                return;
            }
            if (storeContactNumber.isEmpty()) {
                System.out.println("Store contact number cannot be empty. Please try again.");
            } else if (storeContactNumber.length() < 10 || !isAllDigits(storeContactNumber)) {
                System.out.println("Invalid contact number. Please enter at least 10 digits.");
            }
        }

        // Get Store Address
        while (storeAddress.isEmpty()) {
            System.out.print("Enter store address (0 to back)\t\t\t\t: ");
            storeAddress = input.nextLine().trim();
            if (storeAddress.equals("0")) {
                System.out.println("Store registration cancelled.");
                return;
            }
            if (storeAddress.isEmpty()) {
                System.out.println("Store address cannot be empty. Please try again.");
            }
        }

        // Register the store
        boolean success = storeManager.registerStore(storeName, storeEmail, storeAddress, storeContactNumber, sellerID);
        if (!success) {
            System.out.println("Store registration failed.");
        }
    }

    // Check isAllDigits
    private static boolean isAllDigits(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Check if email has @ and .
    private static boolean isValidEmail(String storeEmail) {
        return storeEmail.contains("@") && storeEmail.contains(".");
    }
}