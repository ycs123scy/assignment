import java.util.List;
import java.util.Scanner;

public class TrackOrderMenu {
    private static StoreManager storeManager = new StoreManager();
    
    public static void TraceOrderMenu(String sellerID) {
        storeManager.loadStoresFromFile();
        InvoicesManager.loadAllInvoices();
        
        String storeCode = storeManager.getStoreCodeBySellerID(sellerID);
        if (storeCode == null) {
            System.out.println("No store found for this seller. Please register a store first.");
            return;
        }
        
        Scanner input = new Scanner(System.in);
        boolean choiceChecking = true;
        int trackChoice = 0;

        do {
            try {
                System.out.println("====================================================================================================================================================================================");
                System.out.println("                                                                  - - - T R A C K   O R D E R   M A N A G E M E N T - - -                                                                  ");
                System.out.println("====================================================================================================================================================================================");
                System.out.println(" [1] View Orders");
                System.out.println(" [2] Track Specific Order");
                System.out.println(" [3] Change Order Status");
                System.out.println(" [4] Return and Refund Requests");
                System.out.println(" [0] Back to Store Menu");
                System.out.print("Choose an option: ");
                trackChoice = input.nextInt();
                input.nextLine();

                switch (trackChoice) {
                    case 1:
                        viewOrders(storeCode);
                        break;
                    case 2:
                        trackSpecificOrder(storeCode, input);
                        break;
                    case 3:
                        changeOrderStatus(storeCode, input);
                        break;
                    case 4:
                        changeReturnAndRefundStatus(storeCode, input);
                        break;
                    case 0:
                        choiceChecking = false;
                        System.out.println("Returning to Store Menu...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                input.nextLine();
            }
        } while (choiceChecking);
    }
    
    // View Orders
    private static void viewOrders(String storeCode) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - V I E W   O R D E R S - - -                                                                  ");
        System.out.println("====================================================================================================================================================================================");
        
        List<Invoice> storeInvoices = InvoicesManager.getInvoicesByStore(storeCode);
        
        if (storeInvoices.isEmpty()) {
            System.out.println("No orders found for your store.");
        } else {
            System.out.println("=======================================================================================================================");
            System.out.printf("| %-15s | %-15s | %-20s | %-19s | %-16s | %-15s |\n", 
                           "Invoice ID", "Order ID", "Customer", "Date", "Total Amount", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------");
            
            for (Invoice invoice : storeInvoices) {
                System.out.printf("| %-15s | %-15s | %-20s | %-15s | RM%-14.2f | %-15s |\n",
                                invoice.getInvoiceID(),
                                invoice.getOrderID(),
                                invoice.getCustomerName(),
                                invoice.getInvoiceDate(),
                                invoice.getTotalAmount(),
                                invoice.getStatus());
            }
            System.out.println("=======================================================================================================================");
        }
        
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }
    
    // Track Specific Order
    private static void trackSpecificOrder(String storeCode, Scanner input) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - T R A C K   S P E C I F I C   O R D E R - - -                                                                  ");
        System.out.println("====================================================================================================================================================================================");
        
        System.out.print("Enter Invoice ID or Order ID (0 to back)\t\t: ");
        String searchID = input.nextLine().trim();
        
        if (searchID.equals("0")) {
            System.out.println("Order tracking cancelled.");
            return;
        }
        
        Invoice invoice = InvoicesManager.getInvoiceByID(searchID);
        
        if (invoice != null) {
            // Verify this invoice belongs to the seller's store
            if (!invoice.getStoreCode().equals(storeCode)) {
                System.out.println("This order doesn't belong to your store.");
                return;
            }
            
            displayOrderDetails(invoice);
            return;
        }
        
        // If not found by invoice ID, try by order ID
        invoice = InvoicesManager.getInvoiceByOrderID(searchID);
        if (invoice == null) {
            System.out.println("No order found with that ID.");
            return;
        }
        
        // Verify the order belongs to the seller's store
        if (!invoice.getStoreCode().equals(storeCode)) {
            System.out.println("This order doesn't belong to your store.");
            return;
        }
        
        displayOrderDetails(invoice);
    }
    
    private static void displayOrderDetails(Invoice invoice) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - O R D E R   D E T A I L S - - -                                                                  ");
        System.out.println("====================================================================================================================================================================================");
        System.out.printf("Invoice ID\t: %s\n", invoice.getInvoiceID());
        System.out.printf("Order ID\t: %s\n", invoice.getOrderID());
        System.out.printf("Customer\t: %s\n", invoice.getCustomerName());
        System.out.printf("Date\t\t: %s\n", invoice.getInvoiceDate());
        System.out.printf("Total Amount\t: RM%.2f\n", invoice.getTotalAmount());
        System.out.printf("Status\t\t: %s\n", invoice.getStatus());
        
        System.out.println("\nPress Enter to continue...");
        new Scanner(System.in).nextLine();
    }
    
    // Change Order Status
    private static void changeOrderStatus(String storeCode, Scanner input) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - C H A N G E   O R D E R   S T A T U S - - -                                                                  ");
        System.out.println("====================================================================================================================================================================================");
        
        System.out.print("Enter Invoice ID (0 to back)\t\t\t: ");
        String invoiceID = input.nextLine().trim();
        
        if (invoiceID.equals("0")) {
            System.out.println("Status change cancelled.");
            return;
        }
        
        Invoice invoice = InvoicesManager.getInvoiceByID(invoiceID);
        
        if (invoice == null) {
            System.out.println("Invoice not found.");
            return;
        }
        
        // Check if the invoice belongs to the seller's store
        if (!invoice.getStoreCode().equals(storeCode)) {
            System.out.println("This order doesn't belong to your store.");
            return;
        }
        
        System.out.printf("\nCurrent Order Status\t: %s\n", invoice.getStatus());
        System.out.println("Select new status:");
        System.out.println(" [1] Processing");
        System.out.println(" [2] Shipped");
        System.out.println(" [3] Delivered");
        System.out.println(" [4] Cancelled");
        System.out.println(" [0] Cancel");
        System.out.print("Choose status\t\t: ");
        
        try {
            String statusInput = input.nextLine();
            if (statusInput.equals("0")) {
                System.out.println("Status change cancelled.");
                return;
            }
            
            int statusChoice = Integer.parseInt(statusInput);
            String newStatus;
            switch (statusChoice) {
                case 1:
                    newStatus = "Processing";
                    break;
                case 2:
                    newStatus = "Shipped";
                    break;
                case 3:
                    newStatus = "Delivered";
                    break;
                case 4:
                    newStatus = "Cancelled";
                    break;
                default:
                    System.out.println("Invalid choice. Status not changed.");
                    return;
            }
            
            invoice.setStatus(newStatus);
            InvoicesManager.saveAllInvoices();
            System.out.println("Order status updated successfully!");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    // Change Return and Refund Status
    private static void changeReturnAndRefundStatus(String storeCode, Scanner input) {
        System.out.println("====================================================================================================================================================================================");
        System.out.println("                                                                  - - - C H A N G E   R E T U R N   A N D   R E F U N D   S T A T U S - - -                                                                  ");
        System.out.println("====================================================================================================================================================================================");
        
        System.out.print("Enter Receipt ID (0 to back)\t\t\t: ");
        String receiptID = input.nextLine().trim();
        
        if (receiptID.equals("0")) {
            System.out.println("Status change cancelled.");
            return;
        }
        
        Receipt receipt = ReceiptManager.getReceiptByID(receiptID);
        
        if (receipt == null) {
            System.out.println("Receipt not found.");
            return;
        }
        
        
        Invoice invoice = InvoicesManager.getInvoiceByID(receipt.getInvoiceID());
        if (invoice == null || !invoice.getStoreCode().equals(storeCode)) {
            System.out.println("This receipt doesn't belong to your store.");
            return;
        }
        
        String currentStatus = receipt.getReturnRefundStatus() != null ? receipt.getReturnRefundStatus() : "Pending";
        System.out.printf("\nCurrent Return/Refund Status\t: %s\n", currentStatus);
        System.out.println("Select new status:");
        System.out.println(" [1] Approved");
        System.out.println(" [2] Rejected");
        System.out.println(" [3] Pending");
        System.out.println(" [0] Cancel");
        System.out.print("Choose status\t\t\t: ");
        
        try {
            String statusInput = input.nextLine();
            if (statusInput.equals("0")) {
                System.out.println("Status change cancelled.");
                return;
            }
            
            int statusChoice = Integer.parseInt(statusInput);
            String newStatus;
            switch (statusChoice) {
                case 1:
                    newStatus = "Approved";
                    break;
                case 2:
                    newStatus = "Rejected";
                    break;
                case 3:
                    newStatus = "Pending";
                    break;
                default:
                    System.out.println("Invalid choice. Status not changed.");
                    return;
            }
            
            receipt.setReturnRefundStatus(newStatus);
            ReceiptManager.saveAllReceipts();
            System.out.println("Return/Refund status updated successfully!");
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
        }
    }
}