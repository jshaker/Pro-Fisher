package Fishing;

import Location.Location;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.webwalking.model.RSBank;

import java.util.HashMap;

public class Locations {

    public static final Location Karamja = new Location("Karamja", new Area(2924, 3175, 2925,3180), null, RSBank.DRAYNOR, new HashMap<String, Integer>() {{
        put("Coins", 60);
    }});
}
