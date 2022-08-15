package entity;

import java.util.HashMap;

public class Menu {

    private static HashMap<String, Item> item_list =
        new HashMap<String, Item>();

    private static HashMap<String, ItemSet> set_list =
        new HashMap<String, ItemSet>();

    /* Getter */
    public static HashMap<String, Item> getItem_list() { return item_list; }

    /* Setter */
    public static void setItem_list(HashMap<String, Item> item_list) {
        Menu.item_list = item_list;
    }

    /* Getter */
    public static HashMap<String, ItemSet> getSet_list() { return set_list; }

    /* Setter */
    public static void setSet_list(HashMap<String, ItemSet> set_list) {
        Menu.set_list = set_list;
    }
}
