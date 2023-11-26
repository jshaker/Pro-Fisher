package Cooking;

import Fishing.Activity;

public class Option {

    public Location.Location location;

    public Food food;

    public Option(Location.Location location, Food food) {
        this.location = location;
        this.food = food;
    }

    public String GetName() {
        return "Cook " + this.food.cooked_name + " - " + this.location.name;
    }

}
