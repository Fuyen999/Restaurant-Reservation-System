package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Order {

    private int id;
    private Table table;
    private Staff staff;
    private Customer customer;
    private Date date;
    // private Item ala_carte_list;
    ArrayList<Item> ala_carte_list = new ArrayList<Item>();
    // private ItemSet set_list;
    ArrayList<ItemSet> set_list = new ArrayList<ItemSet>();
    private double tax;
    private double price;
    private double discountedPrice;
    private boolean is_open = true;

    private static int orderCount = 0;

    public static int orderListLength() { return orderCount; }

    /**
     * Create a dummy order for testing.
     */
    public Order() {
        this.id = orderCount;
        orderCount++;
    }

    /**
     * Create an order with specified table, staff, customer and date
     * @param table table of the order
     * @param staff staff of the order
     * @param customer customer of the order
     * @param date date of the order
     */
    public Order(Table table, Staff staff, Customer customer, Date date) {
        this.id = orderCount;
        orderCount++;

        this.table = table;
        this.staff = staff;
        this.customer = customer;
        this.date = date;

        this.price = 0;
        this.tax = 0;
        this.discountedPrice = 0;
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }
    public Date getDate() { return this.date; }

    public void setDate(Date date) { this.date = date; }

    public Table getTable() { return this.table; }
    public void setTable(Table table) { this.table = table; }

    public Staff getStaff() { return this.staff; }
    public void setStaff(Staff staff) { this.staff = staff; }

    public Customer getCustomer() { return this.customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public double getTax() { return this.tax; }
    public void setTax(double tax) { this.tax = tax; }
    public double getPrice() { return this.price; }
    public void setPrice(double price) { this.price = price; }
    public ArrayList<Item> getAla_carte_list() { return this.ala_carte_list; }
    public void setAla_carte_list(ArrayList<Item> alc_list) {
        this.ala_carte_list = alc_list;
    }
    public ArrayList<ItemSet> getSet_list() { return this.set_list; }
    public void setSet_list(ArrayList<ItemSet> set_list) {
        this.set_list = set_list;
    }

    public double getDiscountedPrice() { return this.discountedPrice; }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public boolean getIsOpen() { return this.is_open; }
    public void setIsOpen(boolean status) { this.is_open = status; }


    /**
     * Convert an Order to a string, EXCLUDING details of order items.
     */
    @Override
    public String toString() {
        String result = "";
        result += "╒═══════════════════════════════════════════╕\n";
        result += "│                    Order                  │\n";
        result += "╞═══════════════════════════════════════════╡\n";

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String strUntil = sdf.format(this.getDate());

        result += String.format("│ %-20s %20s │\n",
                                "Order isOpen: ", this.getIsOpen());
        result += String.format("│ %-20s %20s │\n", "Order id: ", this.getId());
        result += String.format("│ %-20s %20s │\n", "Date: ", strUntil);
        result +=
            String.format("│ %-20s %20s │\n",
                          "Table number: ", this.getTable().getTableNum());
        result +=
            String.format("│ %-20s %20s │\n",
                          "Customer name: ", this.getCustomer().getName());
        result += String.format("│ %-30s $ %8.2f │\n", "Total Price",
                                this.getPrice());
        result += String.format("│ %-30s $ %8.2f │\n", "Discounted Price",
                                this.getDiscountedPrice());
        // result += "╘═══════════════════════════════════════════╛";
        result += "╞═══════════════════════════════════════════╛";

        return result;
    }

    /**
     * Convert an Order to a string, INCLUDING details of order items.
     * @return pretty-formatted string, INCLUDING details of order items.
     */
    public String toStringWithDetail() {
        String result = this.toString() + "\n";

        for (Item item : this.getAla_carte_list()) {
            result += item.toStringWithQuantity();
        }

        result += String.format("╞══════════════════════════════╡\n");

        for (ItemSet set_ : this.getSet_list()) {
            result += String.format(set_.toStringWithQuantity() + "\n");
        }

        result += String.format("╘══════════════════════════════╛\n");

        return result;
    }
}
