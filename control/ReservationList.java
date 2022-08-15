package control;

import entity.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Control class to perform operations on the list of all reservation bookings
 * @author Yew Fu Yen
 * @version 1.0
 * @since 2021-11-14
 */
public class ReservationList {
    /**
     * The list of all reservation bookings of the restaurant
     */
    private static ArrayList<Reservation> reservationList =
        new ArrayList<Reservation>();

    /**
     * ID to start counting from, each reservation created will add the count by
     * 1
     */
    private static int IdCount = 10000;

    /**
     * Expiration period of reservations in minutes
     */
    private static int expiryMinutes = 3;

    /**
     * Gets whole list of all current reservations
     * @return list of all current reservations
     */
    public static ArrayList<Reservation> getReservationList() {
        return reservationList;
    }

    /**
     * Search through the list by the list index number of the reservation
     * @param index index number of the reservation in ArrayList
     * @return reservation at list[index]
     */
    public static Reservation searchByIndex(int index) {
        return reservationList.get(index);
    }

    /**
     * Gets a new unique id to be assigned to a resevation
     * @return new ID, added by 1
     */
    public static int getNewId() { return ++IdCount; }

    /**
     * search through the list of all reservations for a single reservation by
     * its reservation ID
     * @param ID ID of the reservation
     * @return reservation that had the ID number, return null if not found
     */
    public static Reservation searchByID(int ID) {
        for (Reservation reservation : reservationList) {
            if (reservation.getId() == ID) {
                return reservation;
            }
        }
        return null;
    }

    /**
     * search through the list of all reservations for a list of reservations by
     * its customer name
     * @param name customer name of the reservation
     * @return all reservations that had the customer name, return null if not
     *     found
     */
    public static ArrayList<Reservation> searchByName(String name) {
        sortByID();
        ArrayList<Reservation> searchResults = new ArrayList<Reservation>();
        for (Reservation reservation : reservationList) {
            if (reservation.getName().equals(name)) {
                searchResults.add(reservation);
            }
        }
        if (searchResults.size() != 0) {
            return searchResults;
        }
        return null;
    }

    /**
     * search through the list of all reservations for a single reservation by
     * its table number
     * @param tableNum table number of the reservation
     * @return reservation that had the table number, return null if not found
     */
    public static Reservation searchByTable(int tableNum) {
        for (Reservation reservation : reservationList) {
            if (reservation.getTable().getTableNum() == tableNum) {
                return reservation;
            }
        }
        return null;
    }

    /**
     * search through the list of all reservations for a list of reservations by
     * its date
     * @param dateString string of date of the reservation booking
     * @return all reservations on the specified date, return null if not found
     */
    public static ArrayList<Reservation> searchByDate(String dateString)
        throws ParseException {
        sortByID();
        // get date, month and year from string
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        ArrayList<Reservation> searchResults = new ArrayList<Reservation>();
        for (Reservation reservation : reservationList) {
            // compare date, month and year
            cal.setTime(reservation.getDatetime());
            if (cal.get(Calendar.DAY_OF_MONTH) == day &&
                cal.get(Calendar.MONTH) == month &&
                cal.get(Calendar.YEAR) == year) {
                searchResults.add(reservation);
            }
        }
        if (searchResults.size() != 0) {
            return searchResults;
        }
        return null;
    }

    /**
     * add a new reservation to the list of all reservations
     * @param reservation new reservation to be added
     */
    public static void addReservation(Reservation reservation) {
        reservationList.add(reservation);
    }

    /**
     * remove a reservation from the list of all reservations
     * @param reservation reservation to be removed from the list of
     *     reservations
     * @return  true - successful operation; false - failed operation.
     */
    public static boolean removeReservation(Reservation reservation) {
        return reservationList.remove(reservation);
    }

    /**
     * sort the list of all reservations is ascending order by the date of the
     * reservations
     */
    private static void sortByDatetime() {
        Collections.sort(reservationList, new Comparator<Reservation>() {
            @Override
            public int compare(Reservation reservation1,
                               Reservation reservation2) {
                return reservation1.getDatetime().compareTo(
                    reservation2.getDatetime());
            }
        });
    }

    /**
     * sort the list of all reservations by their ID number
     */
    private static void sortByID() {
        Collections.sort(reservationList, new Comparator<Reservation>() {
            @Override
            public int compare(Reservation reservation1,
                               Reservation reservation2) {
                return (reservation1.getId() < reservation2.getId()
                            ? -1
                            : (reservation1.getId() == reservation2.getId()
                                   ? 0
                                   : 1));
            }
        });
    }

    /**
     * Sort the list of all reservations by their date
     * Then, check all reservations to see if the reservation had expired (local
     * time larger than expiry time) Remove the reservation if expired
     */
    public static void removeExpiredReservations() {
        sortByDatetime();
        LocalDateTime now = LocalDateTime.now();
        while (reservationList.size() > 0) {
            Date expiry = reservationList.get(0).getExpiry();
            if (expiry.before(Date.from(
                    now.atZone(ZoneId.systemDefault()).toInstant()))) {
                ReservationManager.removeReservation(reservationList.get(0));
            } else {
                break;
            }
        }
    }

    /**
     * get expiry period
     * @return expiry period in minutes
     */
    public static int getExpiryMinutes() { return expiryMinutes; }

    /**
     * set an expiry peiord
     * @param changedExpiryMinutes new expiry period to be set
     */
    public static void setExpiryMinutes(int changedExpiryMinutes) {
        expiryMinutes = changedExpiryMinutes;
    }
}
