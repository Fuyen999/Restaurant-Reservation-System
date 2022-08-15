package entity;

public class Customer {

    private String name;
    private String gender;
    private String contact;
    private int discount;

    public Customer() {
        this.name = "Customer";
        discount = 0;
    }

    public Customer(String name, int discount) {
        this.name = name;
        this.discount = discount;
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getGender(String gender) { return this.gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getContact() { return this.contact; }

    public void setContact(String contact) { this.contact = contact; }

    public int getDiscount() { return this.discount; }

    public void setDiscount(int discount) { this.discount = discount; }
}
