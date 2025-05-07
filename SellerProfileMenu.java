import java.util.Scanner;

public class SellerProfileMenu {
    public static void SellerMenu() {
        SellerManager sellerManager = new SellerManager();
        sellerManager.loadSellersFromFile();

        Scanner input = new Scanner(System.in);
        int sellerChoice = 0;
        boolean choiceChecking = true;

        do { 
            try {
                System.out.println("====================================================================================================================================================================================");
                System.out.println("                                                                - - - S E L L E R   P R O F I L E   S Y S T E M - - -                                                               ");
                System.out.println("====================================================================================================================================================================================");
                System.out.println(" [1] Register");
                System.out.println(" [2] Login");
                System.out.println(" [0] Back to Main Menu");
                System.out.print("Choose an option: ");
                sellerChoice = input.nextInt();
                input.nextLine();
    
                switch (sellerChoice) {
                    case 1:
                        registerSeller(input, sellerManager);
                        break;
                    case 2:
                        loginSeller(input, sellerManager);
                        break;
                    case 0:
                        System.out.println("Returning to Main Menu...");
                        UserMenu.mainMenu();
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

    // Register Seller
    private static void registerSeller(Scanner input, SellerManager sellerManager) {
        String name="", email="", password="", confirmPassword="", contactNumber="", address="", bankAccount="";

        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - S E L L E R   R E G I S T R A T I O N - - -                                                                 "); 
        System.out.println("====================================================================================================================================================================================");
        
        // Get Seller Name
        while (name.isEmpty()) {
            System.out.print("Enter your name (0 to back)\t\t\t\t: ");
            name = input.nextLine().trim();
            if (name.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again.");
            }
        }

        // Get Seller Email
        while (email.isEmpty() || !isValidEmail(email)) {
            System.out.print("Enter your email (0 to back)\t\t\t\t: ");
            email = input.nextLine().trim();
            if (email.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (email.isEmpty()) {
                System.out.println("Email cannot be empty. Please try again.");
            } else if (!isValidEmail(email)) {
                System.out.println("Invalid email format. Please try again.");
            }
        }

        // Get Seller Contact Number
        while (contactNumber.isEmpty() || contactNumber.length() < 10 || !isAllDigits(contactNumber)) {
            System.out.print("Enter your contact number (without -) (0 to back)\t: ");
            contactNumber = input.nextLine().trim();
            if (contactNumber.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (contactNumber.isEmpty()) {
                System.out.println("Contact number cannot be empty. Please try again.");
            } else if (contactNumber.length() < 10 || !isAllDigits(contactNumber)) {
                System.out.println("Invalid contact number. Please enter at least 10 digits.");
            }
        }

        // Get Seller Address
        while (address.isEmpty()) {
            System.out.print("Enter your address (0 to back)\t\t\t\t: ");
            address = input.nextLine().trim();
            if (address.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (address.isEmpty()) {
                System.out.println("Address cannot be empty. Please try again.");
            }
        }

        // Get Seller Bank Account
        while (bankAccount.isEmpty() || bankAccount.length() != 12 || !isAllDigits(bankAccount)) {
            System.out.print("Enter your bank account (12 digits) (0 to back)\t\t: ");
            bankAccount = input.nextLine().trim();
            if (bankAccount.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (bankAccount.isEmpty()) {
                System.out.println("Bank account cannot be empty. Please try again.");
            } else if (bankAccount.length() != 12 || !isAllDigits(bankAccount)) {
                System.out.println("Invalid bank account. Please enter a 12-digit number.");
            }
        }

        // Get Seller Password
        while (password.isEmpty() || password.length() < 8) {
            System.out.print("Enter your password (at least 8 characters) (0 to back)\t: ");
            password = input.nextLine().trim();
            if (password.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again.");
            } else if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters long. Please try again.");
            }
        }

        // Confirm Password
        while (!confirmPassword.equals(password)) {
            System.out.print("Confirm your password (0 to back)\t\t\t: ");
            confirmPassword = input.nextLine().trim();
            if (confirmPassword.equals("0")) {
                System.out.println("Registration cancelled.");
                return;
            }
            if (!confirmPassword.equals(password)) {
                System.out.println("Passwords do not match. Please try again.");
            }
        }

        // Register the seller
        sellerManager.registerSeller(name, email, password, contactNumber, address, bankAccount);
    }

    // Login Seller
    private static void loginSeller(Scanner input, SellerManager sellerManager) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - S E L L E R   L O G I N - - -                                                                 ");
        System.out.println("====================================================================================================================================================================================");

        System.out.print("Enter your seller ID (0 to back)\t\t\t: ");
        String sellerID = input.nextLine();
        
        if (sellerID.equals("0")) {
            System.out.println("Login cancelled.");
            return;
        }
    
        System.out.print("Enter your password (0 to back)\t\t\t\t: ");
        String password = input.nextLine();
        
        if (password.equals("0")) {
            System.out.println("Login cancelled.");
            return;
        }
    
        // Authenticate the seller
        if (sellerManager.loginSeller(sellerID, password)) {
            authenticateSeller(sellerID, input);
        }
    }

    // Authenticate Seller
    private static void authenticateSeller(String sellerID, Scanner input) {
        StoreManager storeManager = new StoreManager();
        storeManager.loadStoresFromFile();
        int authCode = (int) (Math.random() * 9000 + 1000);
    
        System.out.println("----------------------------------------------------------------------");
        System.out.printf("\nAuthentication code: [ %d ]\n\n", authCode);
        System.out.println("----------------------------------------------------------------------");
        
        int maxAttempts = 3;
        int attempts = 0;
        boolean authenticated = false;
    
        while (attempts < maxAttempts && !authenticated) {
            System.out.print("Enter the authentication code (Attempt " + (attempts + 1) + "/" + maxAttempts + ") or 0 to cancel: ");
            
            String userInput = input.nextLine().trim();
            
            if (userInput.equals("0")) {
                System.out.println("Authentication cancelled.");
                return;
            }
            
            if (userInput.isEmpty()) {
                System.out.println("Error: No input detected. Please enter the 4-digit code.");
                attempts++;
                continue;
            }
    
            try {
                int userNum = Integer.parseInt(userInput);
    
                if (userNum == authCode) {
                    authenticated = true;
                    System.out.println("Authentication successful! Access granted.");
                    StoreProfileMenu.StoreMenu(sellerID);
                } else {
                    System.out.println("Incorrect code. Please try again.");
                    attempts++;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a 4-digit number.");
                attempts++;
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                attempts++;
            }
        }
    
        if (!authenticated) {
            System.out.println("Maximum authentication attempts reached. Access denied.");
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
    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}