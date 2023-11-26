package Fishing;

import java.util.concurrent.locks.Condition;

public class Activity {

    public String name;

    public String tool;

    public Fish[] fishes;

    public String interaction_name;

    public Activity(String name, String tool, String interaction_name, Fish[] fishes) {
        this.name = name;
        this.tool = tool;
        this.interaction_name = interaction_name;
        this.fishes = fishes;
    }
}
