package control;

import entity.*;
import java.util.ArrayList;

/**
 * Control class to perform oeprations on the list of all tables
 */
public class TableList {
    /**
     * To represent the number of tables in the restaurant
     * number 4 at index 0 means: 4 table of (0+1)*2 seats
     * number 2 at index 3 means: 2 tables of (3+1)*2 seats
     */
    private static int[] numOfTables = {4, 4, 3, 2, 1};

    /**
     * List of all tables in the restaurant
     */
    private static ArrayList<Table> tableList = new ArrayList<Table>();

    /**
     * Initialise all tables in the restaurant
     */
    public static void tableListInit() {
        int id = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < numOfTables[i]; j++) {
                Table newTable = new Table(id, (i + 1) * 2);
                tableList.add(newTable);
                id++;
            }
        }
    }

    /**
     * Gets whole list of tables in the restaurant
     * @return whole list of all tables
     */
    public static ArrayList<Table> getTableList() { return tableList; }

    /**
     * Gets a table by its numnber
     * @param tableNum table number
     * @return the table object with that table number
     */
    public static Table getTable(int tableNum) {
        for (Table table : tableList) {
            if (table.getTableNum() == tableNum) {
                return table;
            }
        }
        return null;
    }

    /**
     * Search for an empty table of suitable size for reservation
     * @param noOfPax number of pax to be seated on a table
     * @return an empty table with suitable size for reservation
     */
    public static Table searchEmptyTable(int noOfPax) {
        int i, startIndex = 0;

        // Ignore tables with seats < number of pax
        for (i = 0; i < (noOfPax - 1) / 2; i++) {
            startIndex += numOfTables[i];
        }

        // Loop for all table size > number of pax
        while (startIndex < tableList.size()) {
            // If table not reserved or occupied, return the first result (for
            // smallest suitable table)
            if (!(tableList.get(startIndex).isReserved() ||
                  tableList.get(startIndex).isOccupied())) {
                return tableList.get(startIndex);
            }
            /*// If table is reserved but the new reserve will be at later time,
            still reserve else if (tableList.get(startIndex).isReserved()) { if
            (until.after(tableList.get(startIndex).getReservation().getUntil()))
            { return tableList.get(startIndex);
                }
            }*/
            startIndex++;
        }
        return null;
    }

    /**
     * Search for an empty table of suitable size for order
     * @param noOfPax number of pax to be seated on a table
     * @param name customer name
     * @return an empty table with suitable size for order
     */
    public static Table searchEmptyTable(int noOfPax, String name) {
        int i, startIndex = 0;

        // Ignore tables with seats < number of pax
        for (i = 0; i < (noOfPax - 1) / 2; i++) {
            startIndex += numOfTables[i];
        }

        for (i = startIndex; i < tableList.size(); i++) {
            if (tableList.get(i).isReserved()) {
                if (tableList.get(i).getReservation().getName().equals(name)) {
                    return tableList.get(i);
                }
            }
        }

        // Loop for all table size > number of pax
        while (startIndex < tableList.size()) {
            // If table not reserved or occupied, return the first result (for
            // smallest suitable table)
            if (!(tableList.get(startIndex).isReserved() ||
                  tableList.get(startIndex).isOccupied())) {
                return tableList.get(startIndex);
            }
            /*// If table is reserved but the new reserve will be at later time,
            still reserve else if (tableList.get(startIndex).isReserved()) { if
            (until.after(tableList.get(startIndex).getReservation().getUntil()))
            { return tableList.get(startIndex);
                }
            }*/
            startIndex++;
        }
        return null;
    }
}
