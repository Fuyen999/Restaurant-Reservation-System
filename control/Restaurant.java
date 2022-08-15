package control;

// import entity.*;
import java.io.IOException;
// import java.util.ArrayList;

public class Restaurant {
    // private double revenue;
    // private ArrayList<Customer> customer_list;
    // private ArrayList<Staff> staff_list;

    public Restaurant() {}

    /**
     * This funtion initalizes menu and tables,
     * ought to be called at the begining of the program
     * @throws IOException exception during IO
     */
    public void initialize_restaurant() throws IOException {
        // initialize menu
        // if (! MenuManager.is_initialized) {
        MenuManager.readConfigFile();
        //}

        // initialize table
        TableList.tableListInit();
    }
}
