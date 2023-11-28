package Woodcutting;

import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.webwalking.model.RSBank;

public class WoodcuttingLocation {

    public String area_name;

    public Area area;

    public Area safe_area;

    public Tree tree;

    public RSBank bank;

    public WoodcuttingLocation(String area_name, Area area, Area safe_area, Tree tree, RSBank bank) {
        this.area_name = area_name;
        this.area = area;
        this.safe_area = safe_area;
        this.tree = tree;
        this.bank = bank;
    }
}
