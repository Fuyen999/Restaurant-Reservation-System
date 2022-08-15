package entity;

/**
 * Represent a table in the restaurant
 */
public class Table {
    /**
     * a unique table number for each table
     */
    private int tableNum;

    /**
     * number of seats of the table
     */
    private int size;

    /**
     * reservation made onto the table
     */
    private Reservation reservation;

    /**
     * order made at the table
     */
    private Order order;

    /**
     * true only if the table is reserved
     */
    private boolean isReserved;

    /**
     * true only if the table is occupied
     */
    private boolean isOccupied;

    /**
     * constructor
     * @param tableNum table number
     * @param size number of seat
     */
    public Table(int tableNum, int size) {
        this.tableNum = tableNum;
        this.size = size;
        reservation = null;
        order = null;
        isReserved = false;
        isOccupied = false;
    }

    /**
     * Gets the table number
     */
    public int getTableNum() { return this.tableNum; }
    /**
     * Gets the number of seats
     * @return number of seats
     */
    public int getSize() { return this.size; }
    /**
     * gets the reservation made onto the table
     * @return reservation made onto the table
     */
    public Reservation getReservation() { return this.reservation; }
    /**
     * Sets the table number
     * @param tableNum table number to be set
     */
    public void setTableNum(int tableNum) { this.tableNum = tableNum; }
    /**
     * Sets the number of seats of the table
     * @param size number of seats of the table
     */
    public void setSize(int size) { this.size = size; }
    /**
     * Check if the table is reserved
     * @return true only if reserved
     */
    public boolean isReserved() { return isReserved; }
    /**
     * Set the reservation on the table
     * @param reservation reservation to be set
     */
    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        isReserved = true;
    }
    /**
     * Check if the table is occupied
     * @return true only if occupied
     */
    public boolean isOccupied() { return isOccupied; }
    /**
     * Gets the order made at the table
     * @return order made at the table
     */
    public Order getOrder() { return order; }
    /**
     * Sets the order at the table
     * @param order order made at the table
     */
    public void setOrder(Order order) {
        this.order = order;
        isOccupied = true;
    }
    /**
     * release the table from being occupied or reserved
     */
    public void releaseTable() {
        order = null;
        reservation = null;
        isOccupied = false;
        isReserved = false;
    }
    /**
     * release the reservation on the table
     */
    public void releaseReservation() {
        isReserved = false;
        reservation = null;
    }
}
