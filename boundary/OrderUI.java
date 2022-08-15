package boundary;

import control.*;
import entity.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import rrpss.RRPSS;

public class OrderUI {

    private static Scanner sc = RRPSS.sc;

    /**
     * Function to display the order submenu
     * @throws IOException to hanlder clear_console()
     */
    public static void order_ui() throws IOException {
        sc = new Scanner(System.in);

        int choice = -1;
        String input;

        while (choice != 0) {

            System.out.println("╒═══════════════════════════════════╕");
            System.out.println("│ RRPSS                             │");
            System.out.println("│   + Order Manager                 │");
            System.out.println("╞═══════════════════════════════════╡");
            System.out.println("│ 0: Back to main menu              │");
            System.out.println("│ 1: Create an order                │");
            System.out.println("│ 2: Add/remove item of an order    │");
            System.out.println("│ 3: View/Find order                │");
            System.out.println("│ 4: Print order invoice            │");
            System.out.println("╘═══════════════════════════════════╛");

            System.out.print("Choice: ");
            input = sc.nextLine();

            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: Wrong choice...");
                System.out.print("Choice: ");
            }

            switch (choice) {
            case 0:
                break;
            case 1:
                create_order();
                break;
            case 2:
                update_order();
                break;
            case 3:
                RRPSS.clear_console();
                find_order();
                break;
            case 4:
                print_order_invoice();
            default:
                System.out.println("Invalid choice");
            }
        }
    }

    /**
     * Function to gather user input and create an new order
     */
    public static void create_order() {
        System.out.println("======= CREATING ORDER =======");

        // Choose a table...
        System.out.println("Number of pax:");
        int orderNumOfPax = Integer.parseInt(sc.nextLine().trim());

        // Find a staff...
        System.out.println("Staff name:");
        String nameInput = sc.nextLine();
        Staff orderStaff = new Staff(nameInput);

        // gathering the customer's information ...
        System.out.println("Customer name:");
        nameInput = sc.nextLine();

        Table orderTable;
        orderTable = TableList.searchEmptyTable(orderNumOfPax, nameInput);
        if (orderTable == null) {
            System.out.println("No more empty table :(");
            return;
        }

        System.out.println("Discount:");
        int customerDiscount = Integer.parseInt(sc.nextLine().trim());
        Customer orderCustomer = new Customer(nameInput, customerDiscount);
        // System.out.println("Customer name: " + nameInput +
        //"\nDiscount: " + customerDiscount);

        // Get current datatime...
        LocalDateTime ldt = LocalDateTime.now();
        Instant instant = ldt.toInstant(ZoneOffset.ofHours(8));
        Date orderCreatedTime = Date.from(instant);
        System.out.println("Order Created at: " + orderCreatedTime);

        // Call OrderManager
        OrderManager.create_order(orderTable, orderStaff, orderCustomer,
                                  orderCreatedTime);

        System.out.println("------------------------------");
    }

    /**
     * Function to find a specific order.
     * Finding an order by prompting the user and get information.
     * @return returnId which is the id of the desired order to be found.
     *     Returns -1 when not found or not applicable.
     * @throws IOException to hanlder clear_console()
     */
    public static int find_order() throws IOException {
        String key;

        // RRPSS.clear_console();
        System.out.println("╒═══════════════════════════════════╕");
        System.out.println("│ RRPSS                             │");
        System.out.println("│   + Menu Manager                  │");
        System.out.println("│     + View/Find Order             │");
        System.out.println("╞═══════════════════════════════════╡");
        System.out.println("│ 0: EXIT                           │");
        System.out.println("│                                   │");
        System.out.println("│ Filter by:                        │");
        System.out.println("│ 1: View all                       │");
        System.out.println("│ 2: Order id                       │");
        System.out.println("│ 3: Customer name                  │");
        System.out.println("│ 4: table id                       │");
        System.out.println("╘═══════════════════════════════════╛");
        System.out.print("Choice: ");

        int choice = -1;
        int returnId = -1;

        String input = sc.nextLine();

        try {
            choice = Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            choice = -1;
        }

        switch (choice) {
        case 0:
            break;
        case 1:
            OrderManager.view_order();
            break;
        case 2:
        case 3:
        case 4:
            System.out.println("Key Value:");
            key = sc.nextLine().trim();
            Order targetOrder = OrderManager.find_order(choice - 1, key);
            if (targetOrder == null) {
                System.out.println("Cannot find such order.");
                return -1;
            } else {
                // targetOrder.print_info();
                System.out.println(targetOrder.toStringWithDetail());
                returnId = targetOrder.getId();
            }
            break;
        default:
            System.out.println("Filter by?");
            break;
        }

        return returnId;
    }

    /**
     * Function to update order.
     * Adding/removing item/set by prompting the user and get information.
     * @throws IOException to handle clear_console() function
     */
    public static void update_order() throws IOException {
        int order_id = 0;

        RRPSS.clear_console();
        System.out.println("╒═══════════════════════════════════╕");
        System.out.println("│ RRPSS                             │");
        System.out.println("│   + Menu Manager                  │");
        System.out.println("│     + Update Order                │");
        System.out.println("╘═══════════════════════════════════╛");

        OrderManager.view_order();

        System.out.println("=== Update order ===");

        String input = null;
        while (input == null) {
            System.out.println("Order id to update (\"q\" to quit):");

            input = sc.nextLine();
            try {
                if (input.trim().equals("q")) {
                    return;
                }

                // check whether order has been printed as invoice
                if (!OrderManager.checkIsOpenOrder(input.trim())) {
                    System.out.println("Order has been closed or not exists");
                    input = null;
                    continue;
                }

                order_id = Integer.parseInt(input.trim());

                break;
            } catch (Exception e) {
                System.out.println("Wrong id");
                input = null;
            }
        }

        RRPSS.clear_console();

        System.out.println("╒═══════════════════════════════════╕");
        System.out.println("│ RRPSS                             │");
        System.out.println("│   + Menu Manager                  │");
        System.out.println("│     + Update Order                │");
        System.out.println("╞═══════════════════════════════════╡");
        System.out.println("│ 0: EXIT                           │");
        System.out.println("│                                   │");
        System.out.println("│ Add/Remove?                       │");
        System.out.println("│ 1: Remove                         │");
        System.out.println("│ 2: Add                            │");
        System.out.println("╘═══════════════════════════════════╛");
        System.out.print("Choice: ");

        int choice = -1;
        boolean addFlag = true; // 1 - true - add; 0 - false - remove;
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
            addFlag = false; // remove
            break;
        case 2:
            addFlag = true; // add
            break;
        default:
            System.out.println("Incorrect input.");
            break;
        }

        input = "";
        while (!input.equals("q")) {

            RRPSS.clear_console();

            System.out.println("╒═══════════════════════════════════╕");
            System.out.println("│ RRPSS                             │");
            System.out.println("│   + Menu Manager                  │");
            System.out.println("│     + Update Order                │");
            System.out.println("╞═══════════════════════════════════╡");
            System.out.println("│ 0: EXIT                           │");
            System.out.println("│                                   │");
            System.out.println("│ A-la-carte/Set                    │");
            System.out.println("│ 1: Set                            │");
            System.out.println("│ 2: A la Carte                     │");
            System.out.println("╘═══════════════════════════════════╛");
            System.out.print("Choice: ");

            choice = -1;
            input = sc.nextLine();

            try {
                choice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                choice = -1;
            }
            String itemName = "";

            switch (choice) {
            case 0:
                input = "q";
                break;
            case 1:
                while (!itemName.trim().equals("q")) {
                    // set
                    MenuUI.print_menu_set();
                    System.out.println("Set name (\"q\" to quit):");
                    itemName = sc.nextLine();

                    if (Menu.getSet_list().containsKey(itemName)) {
                        OrderManager.update_order_set(
                            order_id, Menu.getSet_list().get(itemName),
                            addFlag);
                    } else {
                        System.out.println("SET NOT ON MENU");
                    }
                }
                break;
            case 2:
                // a la carte
                while (!itemName.equals("q")) {
                    MenuUI.print_menu_item();
                    System.out.println("Item name (\"q\" to quit):");
                    itemName = sc.nextLine();

                    if (Menu.getItem_list().containsKey(itemName)) {
                        OrderManager.update_order(
                            order_id, Menu.getItem_list().get(itemName),
                            addFlag);
                    } else {
                        System.out.println("ITEM NOT ON MENU");
                    }
                }
                break;
            default:
                System.out.println("Incorrect input.");
                break;
            }
        }
    }

    /**
     * Function to call OrderManager to prepare the data to print
     *  when recieving an valid orderID from find_order().
     * @throws IOException to handle clear_console() function
     */
    public static void print_order_invoice() throws IOException {

        int printId = -1;
        // get returned order ID from find_order()
        while (printId == -1) {
            printId = find_order();
        };
        // call manager when an order is selected
        OrderManager.print_invoice(OrderManager.getOrderList().get(printId));
        RRPSS.isToClear = false;
    }

    /**
     * A UI Function to print out the invoice details passed by
     * OrderManager.print_invoice(order) This print function involes UTF-8
     * charaters for border beautifying purpose. format code like "%-25s %12s
     * \n" is used for word alignment.
     *  @param key The invoice information name eg. ID, Table, Tax
     *  @param content The invoice information content for specific 'key'
     *                                      eg: key       content
     *                                          ID        1
     *                                          Table     5
     *                                          Total     15.02
     */
    public static void print_invoice_details(String key, String content) {

        switch (key) {
        // To print header with the order ID
        case "ID":
            System.out.println("\n");
            System.out.println("╒══════════════════════════════════════╕");
            System.out.println("│                 RRPSS                │");
            System.out.println("│############### Order " + content +
                               " ##############│");
            break;

        // To print seperation line
        case "seperation":
            System.out.println("╞══════════════════════════════════════╡");
            break;
        // To print date data
        case "Created Date":
        case "Print Time":
            System.out.printf("%-18s %12s │\n", "│" + key + ": ", content);
            break;
        // To print ending
        case "END":
            System.out.println("│############## THANK YOU #############│");
            System.out.println("╘══════════════════════════════════════╛");
            System.out.println("\n");
            break;
        // To print general info not included above such as order items, table
        // number, customer Name...
        default:
            System.out.printf("%-25s %12s │\n", "│" + key + ": ", content);
            break;
        }
    }
    /**
     * A UI Function to print out the sale revenue details passed by
     * OrderUI.print_sale_revenue_day(), or OrderUI.print_sale_revenue_month()
     * The print funciton utilises UTF-8 characters for beautification purposes
     * "key" to choose the options on the UItime range of the sale
     * revenue to be printed either by day,
     * month, year or to quit the
     * @throws IOException to handle clear_console() function
     * @throws ParseException when date cannot be formatted to the given format
     */
    public static void print_sale_revenue() throws IOException, ParseException {
        String key;
        // Terminal UI output
        RRPSS.clear_console();
        System.out.println("╒═══════════════════════════════════╕");
        System.out.println("│ RRPSS                             │");
        System.out.println("│   + Menu Manager                  │");
        System.out.println("│     + Print Sale Revenue          │");
        System.out.println("╞═══════════════════════════════════╡");
        System.out.println("│ 0: EXIT                           │");
        System.out.println("│                                   │");
        System.out.println("│ Filter by:                        │");
        System.out.println("│ 1: Day                            │");
        System.out.println("│ 2: Month                          │");
        System.out.println("│ 3: Year                           │");
        System.out.println("╘═══════════════════════════════════╛");

        int choice = -1;
        // Loop when not exiting
        while (choice != 0) {
            // Prompting the user to enter one of the options as displayed on
            // the UI
            System.out.println("Filter by (\"q\" to quit):");
            // Get user input
            key = sc.nextLine();

            // Exiting the terminal if user enters "q"
            if (key.trim().equals("q")) {
                break;
            }
            // Converting the user intput into an integer
            choice = Integer.parseInt(key);

            switch (choice) {
            case 0:
                break;
            case 1:
                print_sale_revenue_day();
                break;
            case 2:
                print_sale_revenue_month();
                break;
            case 3:
                print_sale_revenue_year();
                break;
            default:
                System.out.println("INVALID CHOICE");
                break;
            }
        }
    }

    /**
     * When the user chooses to print the daily sale revenue,
     * this UI method will be called
     * Prompts the user to enter necessary details to retrieve the sale revenue
     * from a specific day Details include the date in the format of
     * "dd/MM/yyyy"
     * @throws ParseException when user does not enter the date in the right
     *     format
     */
    public static void print_sale_revenue_day() throws ParseException {
        boolean hasOrder = false;

        double rev = 0.0;

        System.out.print("Enter a date (dd/MM/yyyy):");
        // Get date input
        String date_input = sc.nextLine().trim();

        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(date_input);

        ArrayList<Order> order_list = OrderManager.getOrderList();
        // sum the invoices for that specific day by looking through the
        // orderlist that contains orders that have been invoiced
        for (Order order : order_list) {
            // System.out.println(order.getDate());

            // Only Print when Order is printed out as invoice
            if (order.getIsOpen()) {
                continue;
            }
            if (date.getDay() == order.getDate().getDay() &&
                date.getMonth() == order.getDate().getMonth() &&
                date.getYear() == order.getDate().getYear()) {
                System.out.println(order);
                rev += order.getDiscountedPrice();
                hasOrder = true;
            }
        }
        if (hasOrder) {
            // prints the total revenue for that day
            System.out.println("Revenue of the day : " + rev);
        } else {
            System.out.println(
                "No Order has been preinted out as invoice in the day\n");
        }
    }
    /**
     * When the user chooses to print the monthly sale revenue,
     * this UI method will be called
     * Prompts the user to enter necessary details to retrieve the sale revenue
     * from a specific month Details include the date in the format of "MM/yyyy"
     * @throws ParseException when the user does not enter the date in the right
     *     format
     */
    public static void print_sale_revenue_month() throws ParseException {
        boolean hasOrder = false;
        double rev = 0.0;

        System.out.print("Enter a month (MM/yyyy):");
        // Get date input
        String date_input = sc.nextLine().trim();

        Date date = new SimpleDateFormat("MM/yyyy").parse(date_input);

        ArrayList<Order> order_list = OrderManager.getOrderList();
        // sum the invoices for that specific month by looking through the
        // orderlist that contains orders that have been invoiced
        for (Order order : order_list) {
            // System.out.println(order.getDate());

            // Only Print when Order is printed out as invoice
            if (order.getIsOpen()) {
                continue;
            }
            if (date.getMonth() == order.getDate().getMonth() &&
                date.getYear() == order.getDate().getYear()) {
                System.out.println(order);
                rev += order.getDiscountedPrice();
                hasOrder = true;
            }
        }
        if (hasOrder) {
            // prints the total revenue for that day
            System.out.println("Revenue of the month : " + rev);
        } else {
            System.out.println(
                "No Order has been preinted out as invoice in the month\n");
        }
    }
    /**
     * When the user chooses to print the yearly sale revenue,
     * this UI method will be called
     * Prompts the user to enter necessary details to retrieve the sale revenue
     * from a specific month Details include the date in the format of "MM/yyyy"
     * @throws ParseException when the user does not enter the date in the right
     *     format
     */
    public static void print_sale_revenue_year() throws ParseException {
        boolean hasOrder = false;
        double rev = 0.0;

        System.out.print("Enter a year (yyyy):");
        // Get date input
        String date_input = sc.nextLine().trim();

        Date date = new SimpleDateFormat("yyyy").parse(date_input);

        ArrayList<Order> order_list = OrderManager.getOrderList();
        /*sum the invoices for that specific year by looking through the
        orderlist that contains orders that have been invoiced
        */

        for (Order order : order_list) {
            // System.out.println(order.getDate());

            // Only Print when Order is printed out as invoice
            if (order.getIsOpen()) {
                continue;
            }
            if (date.getYear() == order.getDate().getYear()) {
                System.out.println(order);
                rev += order.getDiscountedPrice();
                hasOrder = true;
            }
        }
        if (hasOrder) {
            // prints the total revenue for that year
            System.out.println("Revenue of the year : " + rev);
        } else {
            System.out.println(
                "No Order has been preinted out as invoice in the year\n");
        }
    }

    public static <T> void print(T o) { System.out.println(o); }
}
