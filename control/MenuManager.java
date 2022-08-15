package control;

import boundary.*;
import entity.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuManager {

    private static final String PATH_ITEM = "../src/item_config.csv";
    private static final String PATH_SET = "../src/set_config.csv";

    public static boolean is_initialized = false;

    /**
     * Constructor, should not be called
     *  @throws IOException
     */
    public MenuManager() throws IOException { readConfigFile(); }

    /**
     * Initialize the item_list and set_list from a csv config file
     *
     * Config files (can edit)
     * - PATH_ITEM = "./item_config.csv";
     * - PATH_SET = "./set_config.csv";
     *
     * item_config format: title(YES)
     * - name, price, type, description
     *
     * set_config format: title(YES)
     * - name, items, price, description
     *
     * @throws IOException
     */
    public static void readConfigFile() throws IOException {

        /* ----------------- ITEM Initialization --------------------- */
        FileReader fr = new FileReader(PATH_ITEM);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        // skip title
        line = br.readLine();
        while (line != null) {
            String[] lineData = line.split(",");
            Menu.getItem_list().put(lineData[0],
                                    new Item(lineData[0],
                                             Double.parseDouble(lineData[1]),
                                             lineData[2], lineData[3]));
            line = br.readLine();
        }

        fr.close();
        br.close();

        /* ----------------- SET Initialization --------------------- */

        fr = new FileReader(PATH_SET);
        br = new BufferedReader(fr);

        line = br.readLine();
        // skip title
        line = br.readLine();
        while (line != null) {
            String[] lineData = line.split(",");
            String[] items = lineData[1].split(";");

            HashMap<String, Item> set_item_list = new HashMap<String, Item>();

            for (String item : items) {

                String[] item_detail = item.split(":");

                String item_name = item_detail[0].trim();
                String item_quantity = item_detail[1].trim();

                // Error occurs when set contains item that item_list dont own
                if (!Menu.getItem_list().containsKey(item_name)) {
                    line = br.readLine();
                    continue;
                }

                Item result = Menu.getItem_list().get(item_name);

                result.setQuantity_set(Integer.parseInt(item_quantity));
                set_item_list.put(result.getName().trim(), result);
            }

            Menu.getSet_list().put(lineData[0],
                                   new ItemSet(lineData[0], set_item_list,
                                               Double.parseDouble(lineData[2]),
                                               lineData[3]));
            line = br.readLine();
        }

        // for (String name : Menu.getSet_list().keySet()) {
        // System.out.println(Menu.getSet_list().get(name));
        //}

        fr.close();
        br.close();
    }

    /**
     * Add or update an item to the item_list (menu)
     * @param String name: Name of item
     * @param double price: Price ofitem
     * @param String type: Type of item
     * @param String description: Descriptor of item
     */
    public static void add_update_menu_item(String name, double price,
                                            String type, String description) {
        Menu.getItem_list().put(name, new Item(name, price, type, description));
    }

    /**
     * remove an item form item_list (menu)
     * @param String name: name of the item to be removed from menu
     * @return true: success
     * @return false: fail
     */
    public static boolean remove_menu_item(String name) {
        if (Menu.getItem_list().containsKey(name)) {
            Menu.getItem_list().remove(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Process item_list string by finding corresponding items and quantity
     * Then create a list of items
     *
     * @param String item_lists: name of the item to be removed from menu
     *        Example: Item1: Quantity1; Item2 : Quantity2
     * @return a HashMap<String, Item> that contains items
     */
    public static HashMap<String, Item>
    add_item_list_of_set(String items_string) {
        String[] items = items_string.split(";");

        HashMap<String, Item> set_item_list = new HashMap<String, Item>();

        for (String item : items) {

            String[] item_detail = item.split(":");

            String item_name = item_detail[0].trim();
            String item_quantity = item_detail[1].trim();

            // Error occurs when set contains item that item_list dont own
            if (!Menu.getItem_list().containsKey(item_name)) {
                MenuUI.print_item_not_found(item_name);
                return null;
            }

            Item result = Menu.getItem_list().get(item_name.trim());
            try {
                result.setQuantity_set(Integer.parseInt(item_quantity));
            } finally {
                set_item_list.put(result.getName().trim(), result);
            }
        }

        return set_item_list;
    }

    /**
     * Add or update an promotion to the set_list (menu)
     * @param String name: name of the promotion set
     * @param HashMap set_item_list: get from
     *     `add_item_list_of_set(items_string)`
     * @param double price: price of set
     * @param String description: description of file
     *
     * @return false: fail
     * @return true: success
     */
    public static boolean
    add_update_menu_itemSet(String name, HashMap<String, Item> set_item_list,
                            double price, String description) {
        if (set_item_list == null) {
            // System.out.println("EXECUTED");
            return false;
        }
        // update or add
        Menu.getSet_list().put(
            name, new ItemSet(name, set_item_list, price, description));
        return true;
    }

    /**
     * remove an promotion form set_list (menu)
     * @param String name: name of the promotion set to be removed form menu
     */
    public static boolean remove_menu_item_itemSet(String name) {
        if (Menu.getSet_list().containsKey(name)) {
            Menu.getSet_list().remove(name);
            return true;
        } else {
            return false;
        }
    }

    /* Getter */
    public String getPATH_ITEM() { return PATH_ITEM; }

    /* Getter */
    public String getPATH_SET() { return PATH_SET; }

    /* Getter */
    public static HashMap<String, Item> getItemList() {
        return Menu.getItem_list();
    }
    /* Getter */
    public static HashMap<String, ItemSet> getSetList() {
        return Menu.getSet_list();
    }

    /**
     * Get list of Category that the menu items have
     * @return a list containing the types (categories)
     */
    public static ArrayList<String> getItemTypes() {
        ArrayList<String> result = new ArrayList<String>();

        for (String name : Menu.getItem_list().keySet()) {
            Item item = Menu.getItem_list().get(name);
            if (!result.contains(item.getType().trim())) {
                result.add(item.getType().trim());
            }
        }

        return result;
    }
}
