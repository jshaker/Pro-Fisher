package Fishing;

import Location.Location;
import Runner.*;
import com.epicbot.api.shared.APIContext;

import java.util.HashMap;

public class Generator {

    public static IRunner Generate(APIContext apiContext, Status status, Location location, Activity activity) {
        Configuration configuration = new Configuration();
        configuration.area_of_interest = location.area;
        configuration.must_have = new HashMap<>(location.must_have);
        configuration.must_have.put(activity.tool, 1);
        configuration.bank_config = new BankConfiguration(location.bank, new HashMap<>(configuration.must_have), false);
        configuration.name = location.name + " " + activity.name;
        configuration.get_entity = apiContext1 -> apiContext1.npcs().query().nameMatches("Fishing spot").actions(activity.interaction_name).results().nearest();
        return new FishingRunner(apiContext, configuration, status, activity.interaction_name);
    }
}
