package Cooking;

public class Food {

    public String raw_name;

    public String cooked_name;

    public String burnt_name;

    public Food(String cooked_name, String raw_name, String burnt_name) {
        this.cooked_name = cooked_name;
        this.raw_name = raw_name;
        this.burnt_name = burnt_name;
    }
}
