package control;

import boundary.*;
import entity.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import rrpss.RRPSS;

/**
 * Control class to interact with both Reservation and Table entity classes as
 * well as ReservationUI class Access infotmation from entity classes and pass
 * information to ReservationUI boundary class to be printed out
 * @author Yew Fu Yen
 * @version 1.0
 * @since 2021-11-14
 */
public class ReservationManager {
    /**
     * Receives input from ReservationUI
     * Finds a suitable empty table
     * Assign an ID
     * Create a new reservation instance in list of reservations
     * Reserve the table
     * @param name Customer name
     * @param datetime Date and time of reservation booking
     * @param noOfPax No of pax of reservation
     * @return Unique ID of reservation
     */
    public static int addReservation(String name, Date datetime, int noOfPax) {
        // set expiry time
        Date until = new Date(datetime.getTime() +
                              (ReservationList.getExpiryMinutes() * 60 * 1000));

        // search for empty table
        Table table = TableList.searchEmptyTable(noOfPax);

        // return false if no table found
        if (table == null) {
            return -1;
        }

        // add reservation if table found
        int id = ReservationList.getNewId();
        Reservation reservation =
            new Reservation(name, noOfPax, datetime, until, table, id);
        ReservationList.addReservation(reservation);
        table.setReservation(reservation);
        return reservation.getId();
    }

    /**
     * Receive input from Reservation UI
     * Search through ReservationList for the first relevant reservation
     * @param num Can be reservation ID or table number
     * @param type "ID" to search by ID, "table" to search by table number
     * @return first reservation result searched
     */
    public static Reservation outputReservation(int num, String type) {
        Reservation reservation = null;

        // search for reservation
        switch (type) {
        case "ID":
            reservation = ReservationList.searchByID(num);
            break;
        case "table":
            reservation = ReservationList.searchByTable(num);
        default:
            break;
        }

        // pass details to UI to be printed
        if (reservation != null) {
            ReservationUI.printSingleReservation(
                reservation.getName(), reservation.getId(),
                reservation.getNumOfPax(), reservation.getDatetime(),
                reservation.getExpiry(), reservation.getTable().getTableNum());
        }
        return reservation;
    }

    /**
     * Receive reservation input from ReservationUI
     * Fetch information from specified reservation
     * Then pass the details to UI to be printed
     * @param reservation reservation to be printed
     */
    public static void outputReservation(Reservation reservation) {
        // pass details to UI to be printed
        if (reservation != null) {
            ReservationUI.printSingleReservation(
                reservation.getName(), reservation.getId(),
                reservation.getNumOfPax(), reservation.getDatetime(),
                reservation.getExpiry(), reservation.getTable().getTableNum());
        }
    }

    /**
     * Search through list of all reservations
     * Compile all relevant search results
     * Eg: All reservations under the name of XXX will be listed
     * Pass the details to ReservationUI to be printed out
     * Get user choice from ReservationUI to return a specific reservation
     * @param input Search term, can be name, date in DD/MM/YYYY format
     * @param type "name" to search by name, "date" to search by date, "all" to
     *     fetch all reservations
     * @return reservation selected by user
     * @throws ParseException  input cannot be parsed to integer
     * @throws IOException exception during IO
     */
    public static Reservation outputReservationList(String input, String type)
        throws ParseException, IOException {
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();

        // search for a list of relevant reservations
        switch (type) {
        case "name":
            reservationList = ReservationList.searchByName(input);
            ReservationUI.listUI(0, "\n--- Search Results ---",
                                 Integer.toString(0), Integer.toString(0));
            ReservationUI.listUI(0, "Customer Name: " + input,
                                 Integer.toString(0), Integer.toString(0));
            break;
        case "date":
            reservationList = ReservationList.searchByDate(input);
            ReservationUI.listUI(0, "\n--- Search Results ---",
                                 Integer.toString(0), Integer.toString(0));
            ReservationUI.listUI(0, "Booking date: " + input,
                                 Integer.toString(0), Integer.toString(0));
            break;
        case "all":
            reservationList = ReservationList.getReservationList();
            break;
        default:
            return null;
        }

        // print the search results in a list of reservation entries
        if (reservationList != null) {
            int index = 0;
            ReservationUI.listUI(0, "╒═════════════════════════════════════╕",
                                 Integer.toString(0), Integer.toString(0));
            ReservationUI.listUI(0, "│ No.              Name            ID │",
                                 Integer.toString(0), Integer.toString(0));
            ReservationUI.listUI(0, "╞═════════════════════════════════════╡",
                                 Integer.toString(0), Integer.toString(0));
            for (Reservation reservation : reservationList) {
                ReservationUI.listUI(1, reservation.getName(),
                                     Integer.toString(reservation.getId()),
                                     Integer.toString(++index));
            }
            ReservationUI.listUI(0, "╘═════════════════════════════════════╛",
                                 Integer.toString(0), Integer.toString(0));

            // calls UI method to ask user to choose a reservation
            index = ReservationUI.listUI(2, "reservation", Integer.toString(0),
                                         Integer.toString(0));

            // gets reservation and return it
            if (index > 0 && index <= reservationList.size()) {
                return reservationList.get(index - 1);
            }
        } else {
            ReservationUI.listUI(0, "No reservation found", Integer.toString(0),
                                 Integer.toString(0));
        }
        return null;
    }

    /**
     * This method releases the table before a reservation is removed
     * Then remove the reservation from ReservationList
     * @param reservation reservation object to be removed
     * @return true - sucessful operation; false - failed operation
     */
    public static boolean removeReservation(Reservation reservation) {
        reservation.getTable().releaseReservation();
        return ReservationList.removeReservation(reservation);
    }

    /**
     * This method prints the status and number of seats of all tables
     * Then if the user chooses a table from the UI,
     * this method fetch table details from table object and pass into UI to be
     * printed
     * @throws IOException exception during IO
     */
    public static void outputTableList() throws IOException {
        String state;
        int tableNum;

        // set status of each table and print into list
        for (Table table : TableList.getTableList()) {
            state = ((table.isOccupied())   ? "Occupied"
                     : (table.isReserved()) ? "Reserved"
                                            : "Empty");
            ReservationUI.listUI(1, Integer.toString(table.getSize()), state,
                                 "Table " + table.getTableNum());
        }

        // list closing
        ReservationUI.listUI(0, "╘═════════════════════════════════════╛",
                             Integer.toString(0), Integer.toString(0));

        // calls UI method to ask user to choose a table to view details
        tableNum = ReservationUI.listUI(2, "table", Integer.toString(0),
                                        Integer.toString(0));

        // gets table details and pass into UI method to be printed
        if (tableNum > 0 && tableNum <= TableList.getTableList().size()) {
            Table table = TableList.getTable(tableNum);
            String reservedBy = "N/A", occupiedBy = "N/A",
                   reservedUntil = "N/A";

            if (table.isReserved()) {
                reservedBy = table.getReservation().getName();
                Date date =
                    new Date(table.getReservation().getDatetime().getTime() +
                             (ReservationList.getExpiryMinutes() * 60 * 1000));
                reservedUntil =
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
            }

            if (table.isOccupied()) {
                occupiedBy = table.getOrder().getCustomer().getName();
            }

            RRPSS.clear_console();
            ReservationUI.printSingleTable(table.getTableNum(), reservedBy,
                                           occupiedBy, table.getSize(),
                                           reservedUntil);
        } else {
            RRPSS.clear_console();
        }
    }

    /**
     * This method calls the ReservationList to remove expired reservations
     */
    public static void removeExpiredReservations() {
        ReservationList.removeExpiredReservations();
    }
}
