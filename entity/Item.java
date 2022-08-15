package entity;

public class Item {

    private String type;
    private String name;
    private String description;
    private double price;
    private int quantity = 1;
    private int quantity_set = 1;

    public Item(String name, double price, String type, String description) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
    }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getQuantity_set() { return quantity_set; }

    public void setQuantity_set(int quantity_set) {
        this.quantity_set = quantity_set;
    }

    /**
     * To String method
     *
     * │ ItemName        $ price │
     * @return pretty-looking String of the item's info, EXCLUDING quantity.
     */
    public String toString() {
        return String.format("│ - %-18s $ %-5.2f │", name, price);
    }

    /**
     * Generate a pretty-looking String of the item's info, INCLUDING quantity.
     * looking like this:
     *
     * │ lemon tea       $4.50 x 2 │
     * @return  pretty-looking String of the item's info, INCLUDING quantity.
     */
    public String toStringWithQuantity() {
        return String.format("│ - %-15s $ %-5.2f x%-2d│\n", name, price,
                             quantity);
    }
}
