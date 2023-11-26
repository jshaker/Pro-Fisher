package Cooking;

import Runner.*;
import com.epicbot.api.shared.APIContext;
import java.util.HashMap;

public class Generator {

    public static IRunner Generate(APIContext apiContext, Status status, Location.Location location, Food food) {
        Configuration configuration = new Configuration();
        configuration.area_of_interest = location.area;
        configuration.must_have = new HashMap<>(location.must_have);
        configuration.must_have.put(food.raw_name, 1);
        configuration.bank_config = new BankConfiguration(location.bank, new HashMap<>(location.must_have), true);
        configuration.bank_config.withdraw_items.put(food.raw_name, 28);
        configuration.name = "Cooking " + food.cooked_name;
        configuration.get_entity = apiContext1 -> apiContext1.objects().query().nameMatches("Cooking range").results().nearest();
        return new CookingRunner(apiContext, configuration, status, food);
    }
}
