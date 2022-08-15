package boundary;

import control.MenuManager;
import entity.Menu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import rrpss.RRPSS;

public class MenuUI {

    // use Scanner from RRPSS class
    private static Scanner sc = new Scanner(System.in);

    // public static void main(String[] args) throws IOException { menu_ui(); }

    /**
     * Menu Manager Interative UI, can do following options
     *
     * Choice:
     *  - 0: EXIT
     *  - 1: Create/Update/Remove Menu Item
     *  - 2: Create/Update/Remove Menu Set
     *  - 3: Show Menu A-la-carte
     *  - 4: Show Menu Promotions
     *  @throws IOException inherited from `readConfigFile` function
     */
    public static void menu_ui() throws IOException {
        // have not initialized menu
        if (!MenuManager.is_initialized) {
            MenuManager.readConfigFile();
            MenuManager.is_initialized = true;
        }

        int choice = -1;
        boolean clear = true;
        String input;
        String result = null;

        while (choice != 0) {
            if (clear) {
                RRPSS.clear_console();
            }
            clear = true;

            if (result != null) {
                System.out.println(result);
            }
            result = null;

            System.out.println("╒═══════════════════════════════════╕");
            System.out.println("│ RRPSS                             │");
            System.out.println("│   + Menu Manager                  │");
            System.out.println("╞═══════════════════════════════════╡");
            System.out.println("│ 0: EXIT                           │");
            System.out.println("│ 1: Create/Update/Remove Menu Item │");
            System.out.println("│ 2: Create/Update/Remove Menu Set  │");
            System.out.println("│ 3: Show Menu A-la-carte           │");
            System.out.println("│ 4: Show Menu Promotions           │");
            System.out.println("╘═══════════════════════════════════╛");
            System.out.print("Choice: ");
            input = sc.nextLine();

            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Wrong choice...");
                System.out.print("Choice: ");
            }

            switch (choice) {
            case 0:
                break;
            case 1:
                menu_item_operation();
                break;
            case 2:
                menu_set_operation();
                break;
            case 3:
                RRPSS.clear_console();
                print_menu_item();
                clear = false;
                break;
            case 4:
                RRPSS.clear_console();
                print_menu_set();
                clear = false;
                break;
            default:
                result = "Invalid choice";
            }
        }

        // sc.close();
    }

    /**
     * This function should either add/update/remove item to/from item_list
     * (menu) choice:
     * - 0: exit
     * - 1: add item
     * - 2: update item
     * - 3: remove item
     * @throws IOException to handle `clear_console()` function
     */
    public static void menu_item_operation() throws IOException {
        int choice = -1;

        String input;
        String result = null;

        while (choice != 0) {
            // clear console
            RRPSS.clear_console();

            // track the result of last round
            if (result != null) {
                System.out.println("RESULT: " + result);
            }

            System.out.println("╒════════════════════════════╕");
            System.out.println("│ RRPSS                      │");
            System.out.println("│   + Menu Manager           │");
            System.out.println("│     + Menu Item Operation  │");
            System.out.println("╞════════════════════════════╡");
            System.out.println("│ 0: EXIT                    │");
            System.out.println("│ 1: Create Item to Menu     │");
            System.out.println("│ 2: Update Item to Menu     │");
            System.out.println("│ 3: Remove Item to Menu     │");
            System.out.println("╘════════════════════════════╛");
            System.out.print("Choice: ");

            input = sc.nextLine();
            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
            case 0:
                break;
            case 1:
            case 2:
                result = add_update_menu_item();
                break;
            case 3:
                result = remove_menu_item();
                break;
            default:
                result = ("Please enter an valid choice!");
            }
        }
    }

    /**
     * Add or update an item to the item_list (menu)
     *
     * This function will ask for following input
     * - item name
     * - item price
     * - item type
     * - item description
     *
     * @return String carries task completion status
     */
    private static String add_update_menu_item() {
        String name;
        String type;
        String description;
        double price;

        String input;

        System.out.println("Enter the item name: ");
        name = sc.nextLine().trim();

        System.out.println("Enter the item price: ");

        input = sc.nextLine();
        try {
            price = Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return "Wrong Price...";
        }

        System.out.println("Enter the item type: ");
        type = sc.nextLine().trim();

        System.out.println("Enter the item description: ");
        description = sc.nextLine().trim();

        // update or add
        MenuManager.add_update_menu_item(name, price, type, description);
        return String.format("RESULT: Item %s is updated to menu.", name);
    }

    /**
     * remove an item form item_list (menu)
     * This function will ask user to input item name to be removed
     * @return String carries task completion status
     */
    private static String remove_menu_item() {
        System.out.println("Enter the item name to be remove: ");
        String name = sc.nextLine().trim();

        if (MenuManager.remove_menu_item(name)) {
            return String.format("RESULT: Item %s is removed from menu.", name);
        } else {
            return String.format("RESULT: Item %s is not found in menu.", name);
        }
    }

    /**
     * This function should either add/update/remove promotion to/from set_list
     * (menu) choice:
     * - 0: exit
     * - 1: add promotion
     * - 2: update promotion
     * - 3: remove promotion
     * @throws IOException to handle `clear_console()` function
     */
    public static void menu_set_operation() throws IOException {
        int choice = -1;

        String input;
        String result = "";

        while (choice != 0) {
            // clear console
            RRPSS.clear_console();

            // track the result of last round
            if (result != null) {
                System.out.println(result);
            }

            System.out.println("╒══════════════════════════╕");
            System.out.println("│ RRPSS                    │");
            System.out.println("│   + Menu Manager         │");
            System.out.println("│     + Menu Set Operation │");
            System.out.println("╞══════════════════════════╡");
            System.out.println("│ 0: EXIT                  │");
            System.out.println("│ 1: Create Set to Menu    │");
            System.out.println("│ 2: Update Set to Menu    │");
            System.out.println("│ 3: Remove Set to Menu    │");
            System.out.println("╘══════════════════════════╛");
            System.out.print("Choice: ");

            input = sc.nextLine();

            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
            case 0:
                break;
            case 1:
            case 2:
                result = add_update_menu_itemSet();
                break;
            case 3:
                result = remove_menu_item_itemSet();
                break;
            default:
                result = "Please enter an valid choice!";
            }
        }
    }

    /**
     * Add or update an promotion to the set_list (menu)
     *
     * This function will ask for following input
     * - set name
     * - set items
     *  - set items should be input with specified delimiter
     *  ( Item:Quantity;Item: Quantity)
     * - set price
     * - set description
     * @return String carries task completion status
     */
    private static String add_update_menu_itemSet() {
        String name;
        String items_string;
        double price;
        String description;

        String input;

        print_menu_item();

        System.out.println("Enter the promotion name: ");
        name = sc.nextLine().trim();

        System.out.println(
            "Enter the items that promotion contains (use ; to seperate items, : to seperate quantity)");
        System.out.println("Example: chicken chop : 2; lemon tea : 1");
        items_string = sc.nextLine();

        System.out.println("Enter the promotion price: ");

        input = sc.nextLine();
        try {
            price = Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return "Wrong Price...";
        }

        System.out.println("Enter the promotion description: ");
        description = sc.nextLine().trim();

        // update or add
        if (MenuManager.add_update_menu_itemSet(
                name, MenuManager.add_item_list_of_set(items_string), price,
                description)) {

            return String.format("RESULT: Promotion %s is updated to menu.",
                                 name);
        } else {
            return String.format("Error: Cannot add item into set.");
        }
    }

    /**
     * remove an promotion form set_list (menu)
     * @return String carries task completion status
     */
    private static String remove_menu_item_itemSet() {
        System.out.println("Enter the promotion name to be remove: ");
        String name = sc.nextLine().trim();

        if (MenuManager.remove_menu_item_itemSet(name)) {
            return String.format("RESULT: Promotion %s is removed from menu.",
                                 name);
        } else {
            return String.format("RESULT: Promotion %s is not found in menu.",
                                 name);
        }
    }

    /**
     * This function will print items in the hash map
     * by using Item's own toString() function
     */
    private static void print_item_list() {
        for (String name : Menu.getItem_list().keySet()) {
            System.out.println(Menu.getItem_list().get(name));
        }
    }

    /**
     * This function will print sets in the hash map
     * by using Set's own toString() function
     */
    private static void print_set_list() {
        for (String name : Menu.getSet_list().keySet()) {
            System.out.println(Menu.getSet_list().get(name));
        }
    }

    /**
     * This function is ought to be used in MenuManager Class
     * Print out the error message with correct item name
     * @param item_name is the name of the item that is not found on menu
     */
    public static void print_item_not_found(String item_name) {
        System.out.printf("Error: Menu does not have item \"%s\"\n", item_name);
    }

    /**
     * Print out the menu with items that are
     * grouped by Category with pretty borders
     */
    public static void print_menu_item() {
        ArrayList<String> types = MenuManager.getItemTypes();
        System.out.println("╒══════════════════════════════╕");
        System.out.println("│           Item Menu          │");
        System.out.println("╞══════════════════════════════╡");
        System.out.println("│                              │");
        for (String type : types) {
            System.out.printf("│ %-28s │\n", type);
            for (String name : MenuManager.getItemList().keySet()) {
                if (MenuManager.getItemList().get(name).getType().trim().equals(
                        type)) {
                    System.out.println(MenuManager.getItemList().get(name));
                }
            }
            System.out.println("│                              │");
        }
        System.out.println("╘══════════════════════════════╛");
    }

    /**
     * Print out the menu with sets with sets' own toString methods
     */
    public static void print_menu_set() {
        System.out.println("╒══════════════════════════════╕");
        System.out.println("│           Set Menu           │");
        System.out.println("╞══════════════════════════════╡");
        System.out.println("│                              │");
        for (String name : MenuManager.getSetList().keySet()) {
            System.out.println(MenuManager.getSetList().get(name));
            System.out.println("│                              │");
        }
        System.out.println("╘══════════════════════════════╛");
    }
}
