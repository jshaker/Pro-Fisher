package Woodcutting;

import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.webwalking.model.RSBank;

public class WoodcuttingLocations {
    public static final WoodcuttingLocation varrock_trees = new WoodcuttingLocation("Varrock", new Area(3152, 3450, 3136, 3456), null, Trees.tree, RSBank.VARROCK_WEST);
    public static final WoodcuttingLocation varrock_oaks = new WoodcuttingLocation("Varrock", new Area(new Tile(3129, 3420), 20), null, Trees.oak_tree, RSBank.VARROCK_WEST);
    public static final WoodcuttingLocation draynor_willows = new WoodcuttingLocation("Draynor", new Area(new Tile(3087, 3233), 5), new Area(new Tile(3097, 3253), 2), Trees.willow_tree, RSBank.DRAYNOR);
    public static final WoodcuttingLocation rimmington_willows = new WoodcuttingLocation("Rimmington", new Area(new Tile(2967, 3195), 5), null, Trees.willow_tree, RSBank.FALADOR_EAST);
    public static final WoodcuttingLocation rimmington_yews = new WoodcuttingLocation("Rimmington", new Area(new Tile(2936, 3232), 4), null, Trees.yew_tree, RSBank.FALADOR_EAST);

    public static WoodcuttingLocation[] GetLocations() {

        return new WoodcuttingLocation[]{
            varrock_trees,
            varrock_oaks,
            draynor_willows,
            rimmington_willows,
            rimmington_yews
        };
    }
}
