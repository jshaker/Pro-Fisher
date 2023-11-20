import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;

import java.util.HashMap;

public class DraynorFishingGenerator {
    public static DraynorFishing Generate(APIContext apiContext, Status status) {
        Configuration configuration = new Configuration();
        configuration.escape_configuration = new EscapeConfiguration();
        configuration.escape_configuration.safe_area = new Area(new Tile(3087,3227), 2);
        configuration.area_of_interest = new Area(new Tile(3086,3232), 8);
        configuration.bank_config = new BankConfiguration();
        configuration.bank_config.depositItems = new String[]{"Raw anchovies", "Raw shrimps"};
        configuration.bank_config.withdrawItems = new HashMap<String, Integer>();
        configuration.bank_config.condition = () -> apiContext.inventory().onlyContains("Small fishing net");
        configuration.name = "Draynor Small Net Fishing";
        DraynorFishing draynorFishing = new DraynorFishing(apiContext, configuration, status);
        return draynorFishing;
    }
}
