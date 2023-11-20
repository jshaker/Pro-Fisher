import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;

import java.awt.*;

public abstract class Runner implements IRunner {

    private final Configuration configuration;
    private final APIContext apiContext;
    private final Bank bank;

    private final Status status;

    public Runner(APIContext apiContext, Configuration configuration, Status status) {
        this.configuration = configuration;
        this.apiContext = apiContext;
        this.status = status;
        this.bank = new Bank(apiContext, configuration.bank_config, status);
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

    private boolean levelUpInterfacePresent() {
        return apiContext.widgets().get(233).isVisible();
    }

    protected abstract boolean DoNothingCondition();

    protected abstract boolean IsDoingActivity();

    protected abstract int DoActivity();

    protected abstract boolean ShouldBank();

    public int Run() {
        if (DoNothingCondition()) {
            return configuration.default_delay;
        }
        if (ShouldBank()) {
            Status status = this.bank.Run();
            return configuration.default_delay;
        }
        if (configuration.escape_configuration != null) {
            if (configuration.escape_configuration.avoid_combat && apiContext.localPlayer().isInCombat()) {
                return escape();
            }
        }
        if (!configuration.area_of_interest.contains(apiContext.localPlayer().getLocation())) {
            return walkToAreaOfInterest();
        }
        if (IsDoingActivity()) {
            if (!levelUpInterfacePresent()) {
                status.message = "Doing work";
                return configuration.default_delay;
            }
            System.out.println("Level up interface detected");
        }
        DoActivity();
        return configuration.default_delay;
    }

    public String Name() {
        return configuration.name;
    }
}
