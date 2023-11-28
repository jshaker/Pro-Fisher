package Cooking;

import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.webwalking.model.RSBank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Locations {

    public static final Location.Location Lumbridge = new Location.Location("Lumbridge", new Area(0, new Tile(3211,3215,0), new Tile(3211,3216,0)), null, RSBank.LUMBRIDGE_TOP, new HashMap<String, Integer>());

    private static final List<Location.Location> locations = new ArrayList<>();

    static {
        locations.add(Lumbridge);
    }

    public static List<Location.Location> GetLocations() {
        return locations;
    }
}
