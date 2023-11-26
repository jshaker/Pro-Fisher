package Cooking;

import java.util.ArrayList;
import java.util.List;

public class Options {

    private static final List<Option> options = new ArrayList<>();

    static {
        for (Location.Location location: Locations.GetLocations()) {
            for (Food food: Foods.GetUncookedFoods()) {
                options.add(new Option(location, food));
            }
        }
    }

    public static List<Option> GetOptions() {
        return options;
    }
}
