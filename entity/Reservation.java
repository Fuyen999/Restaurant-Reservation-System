package entity;

import java.util.Date;

/**
 * Represents a reservation made in the restaurant
 */
public class Reservation {
    /**
     * Customer name of the reservation
     */
    private String name;

    /**
     * Number of pax of the reservation
     */
    private int numOfPax;

    /**
     * Date and time of the reservation
     */
    private Date datetime;

    /**
     * Expiry date and time of the reservation
     */
    private Date expiry;

    /**
     * Table number of the reservation
     */
    private Table table;

    /**
     * ID number of the reservation
     */
    private int id;

    /**
     * Constructor
     * @param name Customer name of the reservation
     * @param numOfPax Number of pax of the reservation
     * @param datetime Date and time of the reservation
     * @param expiry Expiry date and time of the reservation
     * @param table Table number of the reservation
     * @param id ID of the reservation
     */
    public Reservation(String name, int numOfPax, Date datetime, Date expiry,
                       Table table, int id) {
        this.name = name;
        this.numOfPax = numOfPax;
        this.datetime = datetime;
        this.expiry = expiry;
        this.table = table;
        this.id = id;
    }

    /**
     * Gets the customer name of the reservation
     * @return Customer name of the reservation
     */
    public String getName() { return name; }

    /**
     * Sets the customer name of the reservation
     * @param name Customer name of the reservation
     */
    public void setName(String name) { this.name = name; }

    /**
     * Gets the number of pax of the reservation
     * @return Number of pax of the reservation
     */
    public int getNumOfPax() { return numOfPax; }

    /**
     * Sets the number of pax of the reservation
     * @param name Number of pax of the reservation
     */
    public void setNumOfPax(int numOfPax) { this.numOfPax = numOfPax; }

    /**
     * Gets the date and time of the reservation
     * @return Date abd time of the reservation
     */
    public Date getDatetime() { return datetime; }

    /**
     * Sets the date and time of the reservation
     * @param name Date and time of the reservation
     */
    public void setDatetime(Date datetime) { this.datetime = datetime; }

    /**
     * Gets the expiry date and time of the reservation
     * @return Expiry date and time of the reservation
     */
    public Date getExpiry() { return expiry; }

    /**
     * Sets the expiry date and time of the reservation
     * @param name Expiry date and time the reservation
     */
    public void setexpiry(Date expiry) { this.expiry = expiry; }

    /**
     * Gets the table number of the reservation
     * @return Table number of the reservation
     */
    public Table getTable() { return table; }

    /**
     * Sets the table number of the reservation
     * @param name Table number of the reservation
     */
    public void setTable(Table table) { this.table = table; }

    /**
     * Gets the ID number of the reservation
     * @return ID number of the reservation
     */
    public int getId() { return id; }

    /**
     * Sets the ID number of the reservation
     * @param name ID number of the reservation
     */
    public void setId(int id) { this.id = id; }
}
