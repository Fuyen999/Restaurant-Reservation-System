package entity;

import java.util.HashMap;

public class ItemSet {

    private String name;
    private HashMap<String, Item> item_list;
    private int quantity;
    private double price;
    private String description;

    public ItemSet(String name, HashMap<String, Item> item_list, double price) {
        this.name = name;
        this.item_list = item_list;
        this.price = price;
    }

    public ItemSet(String name, HashMap<String, Item> item_list, double price,
                   String description) {
        this.name = name;
        this.quantity = 1;
        this.item_list = item_list;
        this.price = price;
        this.description = description;
    }

    public ItemSet(String name, int quantity, HashMap<String, Item> item_list,
                   double price, String description) {
        this.name = name;
        this.quantity = quantity;
        this.item_list = item_list;
        this.price = price;
        this.description = description;
    }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Generate a pretty-looking String of the item's info, EXCLUDING quantity.
     * looking like this:
     *
     *  │ setB                 $ 16.00 │
     *  │ - lemon tea            x 2   │
     *  │ - chicken chop         x 1   │
     *  │ - meatball noodle      x 1   │
     * @return pretty-looking String of the item's info, EXCLUDING quantity.
     */
    public String toString() {
        String result = String.format("│ %-20s $ %-5.2f │", name, price);

        for (String e : item_list.keySet()) {
            // result += item_list.get(e).getName() + ", ";
            //
            Item item = item_list.get(e);
            result += "\n" + String.format("│ - %-20s x %-3d │", item.getName(),
                                           item.getQuantity_set());
        }

        return result;
    }

    /**
     * Generate a pretty-looking String of the item's info, INCLUDING quantity.
     * looking like this:
     *
     *  │ setB             $ 16.00 x 1 │
     *  │ - lemon tea            x 2   │
     *  │ - chicken chop         x 1   │
     *  │ - meatball noodle      x 1   │
     * @return pretty-looking String of the item's info, INCLUDING quantity.
     */
    public String toStringWithQuantity() {
        String result =
            String.format("│ %-16s $ %-5.2f x %-2d│", name, price, quantity);

        for (String e : item_list.keySet()) {
            // result += item_list.get(e).getName() + ", ";
            //
            Item item = item_list.get(e);
            result += "\n" + String.format("│ - %-20s x %-3d │", item.getName(),
                                           quantity * item.getQuantity_set());
        }

        return result;
    }
}
