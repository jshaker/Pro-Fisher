package Runner;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.GameEntity;
import com.epicbot.api.shared.entity.ItemWidget;
import com.epicbot.api.shared.methods.IInventoryAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public abstract class Runner implements IRunner {

    private final Configuration configuration;
    private final APIContext apiContext;
    private final Bank bank;

    private final Status status;

    public Runner(APIContext apiContext, Configuration configuration, Status status) {
        this.configuration = configuration;
        this.apiContext = apiContext;
        this.status = status;
        try {
            this.bank = new Bank(apiContext, configuration.bank_config, status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int escape() {
        status.message = "Escaping";
        apiContext.webWalking().walkTo(configuration.escape_configuration.safe_area.getRandomTile());
        return configuration.default_delay;
    }

    private int walkToAreaOfInterest() {
        status.message = "Walking to area of interest";
        apiContext.webWalking().walkTo(configuration.area_of_interest.getRandomTile());
        return configuration.default_delay;
    }

    public static boolean HasMustHave(Map<String, Integer> must_have, APIContext apiContext1) {
        String[] missing = must_have.entrySet().stream().filter(e -> {
            if (!apiContext1.inventory().contains(e.getKey())) {
                return true;
            }
            if (apiContext1.inventory().getItem(e.getKey()).isStackable()) {
                return apiContext1.inventory().getItem(e.getKey()).getStackSize() < e.getValue();
            }
            return apiContext1.inventory().getCount(e.getKey()) < e.getValue();
        }).map(Entry::getKey).toArray(String[]::new);
        if (missing.length == 0) {
            return true;
        }
        return false;
    }

    private boolean levelUpInterfacePresent() {
        return apiContext.widgets().get(233).isVisible();
    }

    protected abstract boolean DoNothingCondition();

    protected abstract boolean IsDoingActivity();

    protected abstract int DoActivity();

    protected abstract boolean ShouldDeposit();

    public int Run() {
        if (DoNothingCondition()) {
            return configuration.default_delay;
        }
        if (ShouldDeposit()) {
            return this.bank.Deposit();
        }
        if (!HasMustHave(configuration.must_have, apiContext)) {
            return this.bank.Withdraw();
        }
        if (configuration.escape_configuration != null) {
            if (configuration.escape_configuration.avoid_combat && apiContext.localPlayer().isInCombat()) {
                return escape();
            }
        }
        if (!configuration.area_of_interest.contains(apiContext.localPlayer().getLocation())) {
            return walkToAreaOfInterest();
        }
        GameEntity entity = configuration.get_entity.Get(apiContext);
        if (!apiContext.localPlayer().getLocation().canReach(apiContext, entity.getLocation())) {
            return walkToAreaOfInterest();
        }
        if (IsDoingActivity()) {
            if (!levelUpInterfacePresent()) {
                status.message = "Doing work";
                return configuration.default_delay;
            }
        }
        return DoActivity();
    }

    public String Name() {
        return configuration.name;
    }
}
