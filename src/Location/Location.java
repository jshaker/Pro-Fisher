package Location;

import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.webwalking.model.RSBank;

import java.util.HashMap;


public class Location {

    public String name;

    public Area area;

    public Area safe_area;

    public RSBank bank;

    public HashMap<String, Integer> must_have;

    public Location(String name, Area area, Area safe_area, RSBank bank, HashMap<String, Integer> must_have) {
        this.name = name;
        this.area = area;
        this.safe_area = safe_area;
        this.bank = bank;
        this.must_have = must_have;
    }
}
