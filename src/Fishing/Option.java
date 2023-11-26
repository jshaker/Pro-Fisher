package Fishing;

import Location.Location;

public class Option {

    public Location location;

    public Activity activity;

    public Option(Location location, Activity activity) {
        this.location = location;
        this.activity = activity;
    }

    public String GetName() {
        return this.activity.name + " - " + this.location.name;
    }
}
