package entity;

/**
 * Represent a working personel in the restaurant, serving one or several tables.
 */
public class Staff {

    /**
     * Name of the stuff
     */
    private String name;
    private String gender;
    private String id;
    private String job_title;

   
    /**
     * Constructor
     * Create a stuff with a given name.
     * @param name name of the stuff
     */
    public Staff(String name) {
        this.name = name;
        this.job_title = "Waiter";
    }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getGender() { return this.gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getjob_title() { return this.job_title; }

    public void setJob_title(String job_title) { this.job_title = job_title; }
}
