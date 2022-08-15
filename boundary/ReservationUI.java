package boundary;

import control.ReservationManager;
import control.TableList;
import entity.Reservation;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import rrpss.RRPSS;

/**
  Boundary class to output UI and get user input when managing reservation
  operations ReservationUI shall only talk to a control class and shall not
  access an entity class object directly
  @author  Yew Fu Yen
  @version 1.0
  @since   2021-11-14
 */
public class ReservationUI {
    /**
     * A common scanner object for use of all methods in the class
     */
    private static Scanner sc;

    /**
     * This will be the entry points of the RRPSS Reservation Manager UI
     * Prompts user to choose for the next operation
     * Operations include create a new reservation booking,
     * check a reservation booking and/or remove it,
     * and check the availability of the tables in the restaurant
     * @throws IOException when user does not input a number when integer input
     *     is expected
     * @throws ParseException when user does not enter the date in the right
     *     format
     */
    public static void reservation_ui() throws IOException, ParseException {
        // create and instance of scanner
        sc = new Scanner(System.in);
        int choice = -1;
        String input;

        // Loop when not exit
        while (choice != 0) {

            // Terminal UI output
            System.out.println();
            System.out.println("╒═════════════════════════════════════╕");
            System.out.println("│ RRPSS                               │");
            System.out.println("│   + Reservation Manager             │");
            System.out.println("╞═════════════════════════════════════╡");
            System.out.println("│ 0: EXIT                             │");
            System.out.println("│ 1: Create Reservation Booking       │");
            System.out.println("│ 2: Check/Remove Reservation Booking │");
            System.out.println("│ 3: Check Table Availability         │");
            System.out.println("╘═════════════════════════════════════╛");
            System.out.print("Choice: ");

            // Get user input
            input = sc.nextLine();

            // catch incorrect input format
            try {
                choice = Integer.parseInt(input.trim());

                // remove all expired reservations from list of reservations
                // before any further operations
                ReservationManager.removeExpiredReservations();

                switch (choice) {
                case 0:
                    break;
                case 1:
                    createUI();
                    break;
                case 2:
                    // clear console
                    RRPSS.clear_console();
                    checkReservationUI();
                    break;
                case 3:
                    // clear console
                    RRPSS.clear_console();
                    checkTableUI();
                    break;
                default:
                    // clear console
                    RRPSS.clear_console();
                    System.out.println("\nError: Invalid Choice!");
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nError: Invalid Choice!");
            }
        }
    }

    /**
     * When the user chooses to create a new reservation booking,
     * this UI method will be called
     * Prompts user to enter necessary details to create a new reservation
     * booking Details include name of customer, date and time of booking, no.
     * of pax and contact
     * @throws ParseException when user does not enter the date in the right
     *     format
     * @throws IOException when user does not input a number when integer input
     *     is expected
     */
    private static void createUI() throws ParseException, IOException {
        String name, contact;
        Date date, time, datetime;
        int noOfPax, ID;

        System.out.println("\n--- Create a reservation ---");

        // Get name input
        System.out.print("Please enter your name: ");
        name = sc.nextLine().trim();

        // Get date input
        System.out.print("Please book a date (DD/MM/YYYY): ");
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(sc.nextLine());
        } catch (ParseException e) {
            RRPSS.clear_console();
            System.out.println("\nError: Invalid date format");
            return;
        }
        // Check will local date to see if booking is made in advance
        LocalDate today = LocalDate.now();
        if (date.before(Date.from(today.atStartOfDay()
                                      .atZone(ZoneId.systemDefault())
                                      .toInstant()))) {
            RRPSS.clear_console();
            System.out.println(
                "\nError: Reservations can only be made in advance");
            return;
        }

        // Get time input
        System.out.print("Please book a time (hh:mm): ");
        try {
            time = new SimpleDateFormat("HH:mm").parse(sc.nextLine());
        } catch (ParseException e) {
            RRPSS.clear_console();
            System.out.println("\nError: Invalid time format");
            return;
        }

        // Combine date and time
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int hour = cal.get(Calendar.HOUR_OF_DAY); // save hour
        int min = cal.get(Calendar.MINUTE);       // save minute
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour); // set hour
        cal.set(Calendar.MINUTE, min);       // set minute
        datetime = cal.getTime();            // convert to date

        // check with local datetime to see if booking is made in advance
        LocalDateTime now = LocalDateTime.now();
        if (datetime.before(
                Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))) {
            RRPSS.clear_console();
            System.out.println(
                "\nError: Reservations can only be made in advance");
            return;
        }

        // Get number of pax
        System.out.print("Please enter no. of pax: ");
        try {
            noOfPax = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            RRPSS.clear_console();
            System.out.println("\nInvalid number");
            return;
        }

        // Pass name, datetime and number of pax into reservation manager
        // Reservation manager tries to find a table
        // If reservation manager found a table, reservation manager will assign
        // a non negative ID to the reservation
        ID = ReservationManager.addReservation(name, datetime, noOfPax);

        // Output result
        if (ID != -1) {
            RRPSS.clear_console();
            System.out.println("\nReservation added :)");
            ReservationManager.outputReservation(ID, "ID");
        } else {
            RRPSS.clear_console();
            System.out.println("\nNo empty table with " + noOfPax +
                               " or more seats, sorry :(");
        }
    }

    /**
     * When user chooses to check/remove reservation booking from
     * reservation_ui, this UI method will be called It first ask the user to
     * select an input type to search reservations under that input type Input
     * type includes ID, table number, customer name, reservation booking date,
     * and view all reservations Searching by ID and table number will result in
     * a single reservation to be printed, as one ID and one table can only
     * associate to one reservation at a time Searching by customer name and
     * booking date will resilt in a list of reservations to be printed, as each
     * customer or each date may have several reservation bookings If a list of
     * reservations is printed, user will be prompted to select a reservation
     * entry
     * @throws IOException when user does not input a number when integer input
     *     is expected
     * @throws ParseException when user does not enter the date in the right
     *     format
     */
    private static void checkReservationUI()
        throws IOException, ParseException {
        int choice = -1;
        String input;
        // to pass to other Reservation method calls
        Reservation reservation;

        while (choice == -1) {
            // Terminal output
            System.out.println();
            System.out.println("╒═════════════════════════════════════╕");
            System.out.println("│ RRPSS                               │");
            System.out.println("│   + Reservation Manager             │");
            System.out.println("│     + Check Reservation Booking     │");
            System.out.println("│       (Do you want to search by ?)  │");
            System.out.println("╞═════════════════════════════════════╡");
            System.out.println("│ 0: EXIT                             │");
            System.out.println("│ 1: Reservation ID                   │");
            System.out.println("│ 2: Table Number                     │");
            System.out.println("│ 3: Customer Name                    │");
            System.out.println("│ 4: Booking Date                     │");
            System.out.println("│ 5: View All Reservations            │");
            System.out.println("╘═════════════════════════════════════╛");
            System.out.print("Choice: ");

            // Get input
            input = sc.nextLine();

            // catch incorrect input
            try {
                choice = Integer.parseInt(input.trim());

                switch (choice) {
                case 0:
                    // exit loop if exit
                    break;

                case 1:
                    // Search By ID output
                    System.out.println("\n--- Search By ID ---");
                    System.out.print("Please enter your reservation ID: ");

                    // get user ID input
                    input = sc.nextLine().trim();
                    int ID = -1;
                    while (ID < 0) {
                        try {
                            ID = Integer.parseInt(input.trim());
                        } catch (NumberFormatException e) {
                            System.out.println(
                                "Please enter an numerical ID: ");
                            input = sc.nextLine();
                        }
                    }

                    RRPSS.clear_console();
                    // Search reservation by ID and output to terminal
                    reservation =
                        ReservationManager.outputReservation(ID, "ID");

                    // Give user option to remove reservation
                    if (reservation != null) {
                        removeUI(reservation);
                    } else {
                        RRPSS.clear_console();
                        System.out.println("\nReservation not found");
                    }
                    break;

                case 3:
                    // Terminal output prompt
                    System.out.println("\n--- Search By Name ---");
                    System.out.print("Please enter your name: ");

                    // Get user input, then search by name, and output a list of
                    // reservations under the name Get user choice of
                    // reservation
                    input = sc.nextLine().trim();

                    RRPSS.clear_console();
                    reservation =
                        ReservationManager.outputReservationList(input, "name");

                    // Print chosen reservation, ask user if he/she wants to
                    // remove the reservation
                    if (reservation != null) {
                        RRPSS.clear_console();
                        ReservationManager.outputReservation(reservation);
                        removeUI(reservation);
                    }
                    break;

                case 2:
                    // Terminal output prompt
                    System.out.println("\n--- Search By Table ---");
                    System.out.print("Please enter your table number: ");

                    // Get user input, then search by table number, and output a
                    // list of reservations under the name Get user choice of
                    // reservation
                    input = sc.nextLine().trim();
                    int tableNum = -1;
                    while (tableNum < 0) {
                        try {
                            tableNum = Integer.parseInt(input);
                        } catch (NumberFormatException e) {
                            System.out.print(
                                "Please enter a valid table number: ");
                            input = sc.nextLine().trim();
                        }
                    }

                    // search for reservation
                    RRPSS.clear_console();
                    reservation =
                        ReservationManager.outputReservation(tableNum, "table");

                    // Print reservation, ask user if he/she wants to
                    // remove the reservation
                    if (reservation != null) {
                        removeUI(reservation);
                    } else {
                        RRPSS.clear_console();
                        System.out.println("\nReservation not found");
                    }
                    break;

                case 4:
                    // Terminal output prompt
                    System.out.println("\n--- Search By Date ---");
                    System.out.print(
                        "Please enter your booking date (DD/MM/YYYY): ");

                    // Get user input, then search by date, and output a list of
                    // reservations under the name Get user choice of
                    // reservation
                    input = sc.nextLine().trim();
                    try {
                        new SimpleDateFormat("dd/MM/yyyy").parse(input);
                    } catch (ParseException e) {
                        System.out.println("\nInvalid date format");
                    }

                    // search reservation
                    RRPSS.clear_console();
                    reservation = ReservationManager.outputReservationList(
                        input, "table");

                    // Print chosen reservation, ask user if he/she wants to
                    // remove the reservation
                    if (reservation != null) {
                        RRPSS.clear_console();
                        ReservationManager.outputReservation(reservation);
                        removeUI(reservation);
                    }
                    break;

                case 5:
                    // Terminal output prompt
                    System.out.println("\n--- View All Reservations ---");

                    // Print list of all reservations
                    RRPSS.clear_console();
                    reservation =
                        ReservationManager.outputReservationList(input, "all");

                    // Print chosen reservation, ask user if he/she wants to
                    // remove the reservation
                    if (reservation != null) {
                        RRPSS.clear_console();
                        ReservationManager.outputReservation(reservation);
                        removeUI(reservation);
                    }
                    break;

                default:
                    RRPSS.clear_console();
                    System.out.println("\nInvalid Choice!");
                    choice = -1;
                    break;
                }
            } catch (NumberFormatException e) {
                RRPSS.clear_console();
                System.out.println("\nInvalid Choice!");
            }
        }
    }

    /**
     * This is the UI method called when system prints a list of entries for
     * reservation ot table After printing the list, it will prompt the user to
     * enter an index to select an entry from the list Press any other non-index
     * key to exit
     * @param outputMode 0: print list title, 1: print list entry, 2: ask user
     *     to choose from list
     * @param string1 String to be printed (can be title, or customer name or
     *     seat)
     * @param string2 Second string to be printed (ID or status)
     * @param string3 Third to be printed (index or table name)
     * @return the user's choice of the index number, returns -1 if any other
     *     keys is pressed
     * @throws IOException when user does not input a number when an integer
     *     input is expected
     */
    public static int listUI(int outputMode, String string1, String string2,
                             String string3) throws IOException {
        int returnValue;
        // outputMode = 0, print list title
        if (outputMode == 0) {
            System.out.println(string1);
        }
        // outputMode = 1, print list entries
        else if (outputMode == 1) {
            System.out.printf("│ %-8s %12s %13s │\n", string3, string1,
                              string2);
        }
        // outputMode = 2, ask user for their choice from list of items
        else if (outputMode == 2) {
            System.out.println("Enter an index number to check " + string1 +
                               " details.");
            System.out.println("Enter any other key to exit.");
            System.out.print("Choice: ");
            String input;
            input = sc.nextLine();
            try {
                returnValue = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                RRPSS.clear_console();
                return -1;
            }
            return returnValue;
        }
        return -1;
    }

    /**
     * After a reservation is chosen and displayed,
     * prompts the user to choode if he wants to remove the reservation
     * @param reservation reservation currently displayed
     * @throws IOException when user does not input a number when integer input
     *     is expected
     */
    private static void removeUI(Reservation reservation) throws IOException {
        String input;
        boolean success;
        System.out.print("\nDo you want to cancel this reservation? (Y/N) ");
        input = sc.nextLine();

        if (input.equals("Y") || input.equals("y")) {
            success = ReservationManager.removeReservation(reservation);
            if (success) {
                RRPSS.clear_console();
                System.out.println("\nReservation cancelled :)");
            } else {
                System.out.println("\nFailed!");
            }
        } else {
            RRPSS.clear_console();
        }
    }

    /**
     * When user chooses to check table availability from the reservation_ui,
     * this UI method will be called It prints list of table entries, and ask
     * user to choose a table to view its details
     * @throws IOException when user foes not input a number when integer input
     *     is expected
     */
    private static void checkTableUI() throws IOException {
        // List title
        System.out.println();
        System.out.println("╒═════════════════════════════════════╕");
        System.out.println("│ Table            Seat        Status │");
        System.out.println("╞═════════════════════════════════════╡");
        // List entries +
        // List ending
        ReservationManager.outputTableList();
        System.out.println();
    }

    /**
     * Called by reservation manager to print out a reservation's details
     * @param name name of customer that books the rservatiom
     * @param id Unique ID for the reservation booking
     * @param noOfPax No of pax for the reservation
     * @param datetime Date and time of the booking
     * @param until Reservation expiry time
     * @param tableNum Reserved table number
     */
    public static void printSingleReservation(String name, int id, int noOfPax,
                                              Date datetime, Date until,
                                              int tableNum) {
        System.out.println();
        System.out.println("╒═════════════════════════════════════╕");
        System.out.println("│             Reservation             │");
        System.out.println("╞═════════════════════════════════════╡");
        System.out.printf("│ %-22s %12s │\n", "Reservation ID: ", id);
        System.out.printf("│ %-22s %12s │\n", "Customer Name: ", name);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strTime = sdf.format(datetime);
        System.out.printf("│ %-18s %12s │\n", "Booked Time: ", strTime);

        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strUntil = sdf.format(until);
        System.out.printf("│ %-18s %12s │\n", "Booked until: ", strUntil);

        System.out.printf("│ %-22s %12s │\n", "Table: ", tableNum);
        System.out.printf("│ %-22s %12s │\n", "No of Pax: ", noOfPax);
        System.out.println("╘═════════════════════════════════════╛");
    }

    /**
     * Called by reservation manager to print out a table's details
     * @param tableNum Table number
     * @param reservedBy Name of customer that reserved the table
     * @param occupiedBy Name of customer that occupied the table
     * @param seat Number of seats of the table
     * @param reservedUntil Reservation expiry date and time
     * If the table is not reserved pr occupied, reservedBy, occupiedBy and
     * reservedUntil will be N/A
     */
    public static void printSingleTable(int tableNum, String reservedBy,
                                        String occupiedBy, int seat,
                                        String reservedUntil) {
        System.out.println();
        System.out.println("╒═════════════════════════════════════╕");
        System.out.printf("│ %21s               │\n", "Table " + tableNum);
        System.out.println("╞═════════════════════════════════════╡");
        System.out.printf("│ %-20s %14s │\n", "Number of seats: ", seat);
        System.out.printf("│ %-15s %19s │\n", "Occupied By: ", occupiedBy);
        System.out.printf("│ %-15s %19s │\n", "Reserved By: ", reservedBy);
        System.out.printf("│ %-12s %18s │\n",
                          "Reserved Until: ", reservedUntil);
        System.out.println("╘═════════════════════════════════════╛");
    }
}
