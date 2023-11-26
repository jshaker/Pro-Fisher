package Fishing;

import Location.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Options {

    private static final HashMap<Location, List<Activity>> locationToActivitiesMap = new HashMap<>();
    private static final HashMap<Activity, List<Location>> activitiesToLocationMap = new HashMap<>();

    private static final Option[] options = new Option[]{
            new Option(Locations.Karamja, Activities.LobsterCage),
            new Option(Locations.Karamja, Activities.Harpooning),
    };

    {
        for (Option option: options) {
            if (!locationToActivitiesMap.containsKey(option.location)) {
                locationToActivitiesMap.put(option.location, new ArrayList<>());
            }
            locationToActivitiesMap.get(option.location).add(option.activity);

            if (!activitiesToLocationMap.containsKey(option.activity)) {
                activitiesToLocationMap.put(option.activity, new ArrayList<>());
            }
            activitiesToLocationMap.get(option.activity).add(option.location);
        }
    }

    public static Option[] GetOptions() {
        return options;
    }

    public static List<Location> GetLocation(Activity activity) {
        return activitiesToLocationMap.get(activity);
    }

}
