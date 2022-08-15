package control;

import boundary.OrderUI;
import entity.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;            
import java.time.format.DateTimeFormatter; 
import java.util.ArrayList;
import java.util.Date;

public class OrderManager {

    /**
     * The dynamic list for all the orders.
     */
    private static ArrayList<Order> order_list = new ArrayList<Order>();

    /**
     * Returns the list of Orders.
     * @return order_list
     */
    public static ArrayList<Order> getOrderList() { return order_list; }

    /**
     * Append a new order to order_list
     * @param table table of new order
     * @param staff staff for new order
     * @param customer customer of new order
     * @param date date of new order
     */
    public static void create_order(Table table, Staff staff, Customer customer,
                                    Date date) {
        Order newOrder = new Order(table, staff, customer, date);
        order_list.add(newOrder);
        table.setOrder(newOrder);
        //System.out.println("order added.");
        System.out.println("order list length: " + order_list.size());
    }

    /**
     * Print out all orders.
     */
    public static void view_order() {
        System.out.println("VIEW ORDER");

        System.out.println("order list length: " + order_list.size());
        int orderListLength = order_list.size();
        for (int i = 0; i < orderListLength; i++) {
            // System.out.println(order_list.get(i));
            OrderUI.print(order_list.get(i));

            for (Item item : order_list.get(i).getAla_carte_list()) {
                OrderUI.print(item.toStringWithQuantity());
            }
            OrderUI.print("╞══════════════════════════════╡");

            for (ItemSet set_ : order_list.get(i).getSet_list()) {
                OrderUI.print(set_.toStringWithQuantity());
            }

            OrderUI.print("╘══════════════════════════════╛");
        }
    }

    /**
     * Find and return order with specific id/customer name/table id
     *
     * @param filterBy  0 - all (calls view_order) 
     *                  1 - by id 
     *                  2 - by customer name
     *                  3 - by table number
     * @param key      key to be searched
     * @return order    order found / null if no match
     */
    public static Order find_order(int filterBy, String key) {

        int orderListLength = order_list.size();

        switch (filterBy) {
        // view all orders
        case 0:
            view_order();
            break;

        // filter by order id
        case 1:
            for (int i = 0; i < orderListLength; i++) {
                if (order_list.get(i).getId() == Integer.parseInt(key)) {
                    return order_list.get(i);
                }
            }
            break;

        // filter by customer name
        case 2:
            for (int i = 0; i < orderListLength; i++) {
                if (order_list.get(i).getCustomer().getName().equals(key)) {
                    return order_list.get(i);
                }
            }
            break;

        // filter by table number
        case 3:
            for (int i = 0; i < orderListLength; i++) {
                if (order_list.get(i).getTable().getTableNum() ==
                    Integer.parseInt(key)) {
                    return order_list.get(i);
                }
            }
            break;
        }
        return null;
    }

    /**
     * Update an existing order i.e. add / remove an **a la carte **item, from order with
     * specified order id
     * Also maintains the price of an order.
     *
     * @param order_id  order id of the target order
     * @param item      a la carte item to be added/removed.
     * @param option    true - add an item false - remove an item (decrease
     *                  item.quantity by 1. If an item's quantity would become 0,
     *                  it gets removed.
     * @return 0 - successful operation; 1 - failed operation.
     */
    public static int update_order(int order_id, Item item, boolean option) {
        int orderListLength = order_list.size();

        int i = 0;
        boolean orderFound = false;
        for (i = 0; i < orderListLength; i++) {
            if (order_list.get(i).getId() == order_id) {
                orderFound = true;
                break;
            }
        }
        if (!orderFound) {
            return -1;
            // System.out.println("ERROR removing item: Specified order not
            // found!");
        }
        int currentOrderLength = order_list.get(i).getAla_carte_list().size();
        boolean itemFound = false;
        int j = 0;
        for (j = 0; j < currentOrderLength; j++) {
            if (order_list.get(i).getAla_carte_list().get(j).getName().equals(
                    item.getName())) {
                itemFound = true;
                break;
            }
        }

        // adding an item
        if (option == true) {
            double factor =
                1 - (double)order_list.get(i).getCustomer().getDiscount() / 100;
            order_list.get(i).setPrice(order_list.get(i).getPrice() +
                                       item.getPrice());
            order_list.get(i).setTax(order_list.get(i).getTax() +
                                     0.07 * item.getPrice() * factor);
            order_list.get(i).setDiscountedPrice(
                order_list.get(i).getDiscountedPrice() +
                item.getPrice() * (factor));

            // adding new item
            if (!itemFound) {
                order_list.get(i).getAla_carte_list().add(item);
            }
            // adding the quantity of existing item
            else {

                int quantity =
                    order_list.get(i).getAla_carte_list().get(j).getQuantity();
                order_list.get(i).getAla_carte_list().get(j).setQuantity(
                    quantity + 1);
            }

        }
        // removing an item
        else {

            // deleting an item
            if (!itemFound) {
                return -1;
                // System.out.println("ERROR removing item: Specified item not
                // found!");
            } else {
                double factor =
                    1 -
                    (double)order_list.get(i).getCustomer().getDiscount() / 100;
                order_list.get(i).setPrice(order_list.get(i).getPrice() -
                                           item.getPrice());
                order_list.get(i).setTax(order_list.get(i).getTax() -
                                         0.07 * item.getPrice() * factor);
                order_list.get(i).setDiscountedPrice(
                    order_list.get(i).getDiscountedPrice() -
                    item.getPrice() * (factor));

                int quantity =
                    order_list.get(i).getAla_carte_list().get(j).getQuantity();
                // remove item
                if (quantity == 1) {
                    order_list.get(i).getAla_carte_list().remove(j);
                }
                // decrement item.quantity
                else {
                    order_list.get(i).getAla_carte_list().get(j).setQuantity(
                        quantity - 1);
                }
            }
        }
        return 0;
    }

    /**
     * Update an existing order i.e. add / remove an **set**, from order with
     * specified order id
     *
     * @param order_id  order id of the target order
     * @param item     Set item to be added/removed.
     * @param option   true - add an item false - remove an item (decrease
     *                 item.quantity by 1. If an item's quantity would become 0,
     *                 it gets removed.
     */
    public static int update_order_set(int order_id, ItemSet item,
                                       boolean option) {
        int orderListLength = order_list.size();
        int i = 0;
        boolean orderFound = false;
        for (i = 0; i < orderListLength; i++) {
            if (order_list.get(i).getId() == order_id) {
                orderFound = true;
                break;
            }
        }
        if (!orderFound) {
            return -1;
            // System.out.println("ERROR removing item: Specified order not
            // found!");
        }
        int currentOrderLength = order_list.get(i).getSet_list().size();
        boolean itemFound = false;
        int j = 0;
        for (j = 0; j < currentOrderLength; j++) {
            if (order_list.get(i).getSet_list().get(j).getName().equals(
                    item.getName())) {
                itemFound = true;
                break;
            }
        }

        // adding an item
        if (option == true) {
            double factor =
                1 - (double)order_list.get(i).getCustomer().getDiscount() / 100;
            order_list.get(i).setPrice(order_list.get(i).getPrice() +
                                       item.getPrice());
            order_list.get(i).setTax(order_list.get(i).getTax() +
                                     0.07 * item.getPrice() * factor);
            order_list.get(i).setDiscountedPrice(
                order_list.get(i).getDiscountedPrice() +
                item.getPrice() * factor);

            // adding new item
            if (!itemFound) {
                order_list.get(i).getSet_list().add(item);
            }
            // adding the quantity of existing item
            else {
                int quantity =
                    order_list.get(i).getSet_list().get(j).getQuantity();
                order_list.get(i).getSet_list().get(j).setQuantity(quantity +
                                                                   1);
            }

        }
        // removing an item
        else {

            // deleting an item
            if (!itemFound) {
                return -1;
                // System.out.println("ERROR removing item: Specified item not
                // found!");
            } else {
                double factor =
                    1 -
                    (double)order_list.get(i).getCustomer().getDiscount() / 100;
                order_list.get(i).setPrice(order_list.get(i).getPrice() -
                                           item.getPrice());
                order_list.get(i).setTax(order_list.get(i).getTax() -
                                         0.07 * item.getPrice() * factor);
                order_list.get(i).setDiscountedPrice(
                    order_list.get(i).getDiscountedPrice() -
                    item.getPrice() * (factor));

                int quantity =
                    order_list.get(i).getSet_list().get(j).getQuantity();
                // remove item
                if (quantity == 1) {
                    order_list.get(i).getSet_list().remove(j);
                }
                // decrement item.quantity
                else {
                    order_list.get(i).getSet_list().get(j).setQuantity(
                        quantity - 1);
                }
            }
        }
        return 0;
    }

    /**
     * Function to check if an order with specified order id exists and is open.
     * @param id order id of query
     * @return false - not exist/closed; true - exist and open
     */
    public static boolean checkIsOpenOrder(String id) {

        // 1 represents id
        Order order = find_order(1, id);
        if (order == null) {
            return false;
        }
        return order.getIsOpen();
    }
    /**
     * Prepare all the information for printing invoice for an existing order selected from user by
     * specified order id
     * The final printing job is done at OrderUI by passing all he necessary data to OrderUI.print_invoice_details(key,content)
     *
     * @param order Selected order object from user
     */

    public static void print_invoice(Order order) {
        // close order
        order.setIsOpen(false);

        //get current datetime as print datetime
        LocalDateTime dateObj = LocalDateTime.now();
        //formate the datetime
        DateTimeFormatter formateDateObj =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String printDate = formateDateObj.format(dateObj);

        //format the order created date
        Date createdDate = order.getDate();
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String createDate = dt.format(createdDate);

        //calculate the discountPrice and total price
        double subTotal = order.getPrice();
        int discount = order.getCustomer().getDiscount();
        double discountPrice = discount * subTotal / 100.0;
        double tax = order.getTax();
        double total = order.getDiscountedPrice() + tax;

        //print invoice including Order ID created Date and Table Number
        OrderUI.print_invoice_details("ID", String.valueOf(order.getId()));
        OrderUI.print_invoice_details("seperation", "null");
        OrderUI.print_invoice_details("Created Date", createDate);
        OrderUI.print_invoice_details(
            "Table", String.valueOf(order.getTable().getTableNum()));
        OrderUI.print_invoice_details("seperation", "null");

        /// print ala_carte_list
        for (int i = 0; i < order.getAla_carte_list().size(); i++) {
            Item alaItem = order.getAla_carte_list().get(i);
            if (alaItem.getQuantity() > 0) {
                String itemDetail = alaItem.getQuantity() + "x" +
                                    String.format("%.2f", alaItem.getPrice());
                OrderUI.print_invoice_details(alaItem.getName(), itemDetail);
            }
        }

        // print set_list
        for (int j = 0; j < order.getSet_list().size(); j++) {
            ItemSet setItem = order.getSet_list().get(j);
            String setDetail = setItem.getQuantity() + "x" +
                               String.format("%.2f", setItem.getPrice());
            OrderUI.print_invoice_details(setItem.getName(), setDetail);
        }

        //print sub-Total price, total price, Tax price
        OrderUI.print_invoice_details("seperation", "null");
        OrderUI.print_invoice_details("Total", String.format("%.2f", total));
        OrderUI.print_invoice_details("Sub-Total",
                                      String.format("%.2f", subTotal));
        OrderUI.print_invoice_details("7%Tax", String.format("%.2f", tax));

        //print discount price
        if (discount > 0) {
            OrderUI.print_invoice_details("Discount",
                                          String.format("%.2f", discountPrice));
        }

        //print Customer Name,Staff Name and Print Time
        OrderUI.print_invoice_details("seperation", "null");
        OrderUI.print_invoice_details("Customer Name",
                                      order.getCustomer().getName());
        OrderUI.print_invoice_details("Staff Name", order.getStaff().getName());
        OrderUI.print_invoice_details("Print Time", printDate);
        OrderUI.print_invoice_details("seperation", "null");
        OrderUI.print_invoice_details("END", "null");

        //if invoice is printed, release the table
        order.getTable().releaseTable();
        
    }
}
