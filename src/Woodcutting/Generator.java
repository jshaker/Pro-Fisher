package Woodcutting;

import Fishing.Activity;
import Location.Location;
import Runner.*;
import com.epicbot.api.shared.APIContext;

import java.util.HashMap;

public class Generator {

    public static IRunner Generate(APIContext apiContext, Status status, WoodcuttingLocation location, Axe axe) {
        Configuration configuration = new Configuration();
        configuration.default_delay = 6000;
        configuration.area_of_interest = location.area;
        if (location.safe_area != null) {
            configuration.escape_configuration = new EscapeConfiguration();
            configuration.escape_configuration.avoid_combat = true;
            configuration.escape_configuration.safe_area = location.safe_area;
        }
        configuration.must_have = new HashMap<>();
        configuration.must_have.put(axe.name, 1);
        configuration.bank_config = new BankConfiguration(location.bank, new HashMap<>(configuration.must_have), false, configuration.default_delay);
        configuration.name = location.area_name + " " + location.tree.name;
        configuration.get_entity = apiContext1 -> apiContext1.objects().query().nameMatches(location.tree.name).actions("Chop down").results().nearest();
        return new WoodcuttingRunner(apiContext, configuration, status, location.tree);
    }
}
