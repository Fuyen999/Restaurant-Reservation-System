package rrpss;

import boundary.*;
import control.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class RRPSS {
    public static Scanner sc = new Scanner(System.in);

    public static boolean isToClear = true;

    private Restaurant restaurant;

    // private final String DISTRO = "linux";

    /**
     * Hide Console output history
     * @throws IOException
     */
    public static void clear_console() throws IOException {
        // Runtime.getRuntime().exec("clear");
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public RRPSS() { restaurant = new Restaurant(); }

    /**
     * RRPSS Interative UI, can do following options
     *
     * Choice:
     * - 0: EXIT
     * - 1: Manage Menu                 : go to MenuUI
     * - 2: Manage Order                : go to OrderUI
     * - 3: Manage Reservation          : go to ReservationUI
     * - 4: Print Order Invoice
     * - 5: Print Sale Revenue Report
     * @param command line arguments
     * @throws IOException, InterruptedException, ParseException
     */
    public static void main(String[] args)
        throws IOException, InterruptedException, ParseException {

        if (isToClear) {
            clear_console();
        }
        isToClear = true;

        // initialize all needed resources
        RRPSS rrpss = new RRPSS();

        rrpss.restaurant.initialize_restaurant();

        Scanner sc;
        sc = new Scanner(System.in);

        System.out.println("\nWelcome To RRPSS");

        int choice = -1;
        String input;

        while (choice != 0) {
            clear_console();

            System.out.println("╒══════════════════════════════╕");
            System.out.println("│             RRPSS            │");
            System.out.println("╞══════════════════════════════╡");
            System.out.println("│ 0: EXIT                      │");
            System.out.println("│ 1: Manage Menu               │");
            System.out.println("│ 2: Manage Order              │");
            System.out.println("│ 3: Manage Reservation        │");
            System.out.println("│ 4: Print Order Invoice       │");
            System.out.println("│ 5: Print Sale Revenue Report │");
            System.out.println("╘══════════════════════════════╛");
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
                MenuUI.menu_ui();
                break;
            case 2:
                OrderUI.order_ui();
                break;
            case 3:
                RRPSS.clear_console();
                ReservationUI.reservation_ui();
                break;
            case 4:
                // print order invoice
                OrderUI.print_order_invoice();
                break;
            case 5:
                // Print Sale Revenue Report by Period
                OrderUI.print_sale_revenue();
                break;
            default:
                System.out.println("Invalid choice");
            }
        }

        // close scanner
        sc.close();
    }
}
