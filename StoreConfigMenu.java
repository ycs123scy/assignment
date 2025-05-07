import java.util.Scanner;

public class StoreConfigMenu {
    public static void ConfigStoreMenu(String sellerID) {
        Scanner input = new Scanner(System.in);
        int storeConfigChoice = 0;
        boolean choiceChecking = true;

        do {
            try {
                System.out.println("====================================================================================================================================================================================");
                System.out.println("                                                                  - - - S T O R E   C O N F I G U R A T I O N - - -                                                                 ");
                System.out.println("====================================================================================================================================================================================");
                System.out.println(" [1] Configure Products");
                System.out.println(" [2] Configure Product Specifications");
                System.out.println(" [3] Configure Order Status");
                System.out.println(" [0] Exit Store Profile Menu");
                System.out.print("Choose an option: ");
                storeConfigChoice = input.nextInt();
                input.nextLine();

                switch (storeConfigChoice) {
                    case 1:
                        StoreProductMenu.ProductMenu(sellerID);
                        break;
                    case 2:
                        StoreProductSpecificationMenu.ProductSpecificationMenu(sellerID);
                        break;
                    case 3:
                        TrackOrderMenu.TraceOrderMenu(sellerID);
                        break;
                    case 0:
                        StoreProfileMenu.StoreMenu(sellerID);
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
}
