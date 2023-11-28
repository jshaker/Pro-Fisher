package Runner;

import com.epicbot.api.shared.APIContext;

import static Inventory.Inventory.HasMustHave;

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
        return 30000;
    }

    protected int WalkToAreaOfInterest() {
        status.message = "Walking to area of interest";
        apiContext.webWalking().walkTo(configuration.area_of_interest.getRandomTile());
        return configuration.default_delay;
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
        if (!HasMustHave(apiContext.inventory(), configuration.must_have)) {
            return this.bank.Withdraw();
        }
        if (configuration.escape_configuration != null) {
            if (configuration.escape_configuration.avoid_combat && apiContext.localPlayer().isInCombat()) {
                return escape();
            }
        }
        if (!configuration.area_of_interest.contains(apiContext.localPlayer().getLocation())) {
            return WalkToAreaOfInterest();
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
