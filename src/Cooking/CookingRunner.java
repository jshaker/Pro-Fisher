package Cooking;

import Runner.Configuration;
import Runner.Status;
import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.GameEntity;
import com.epicbot.api.shared.util.Random;
import com.epicbot.api.shared.util.time.Time;

import static java.awt.event.KeyEvent.VK_SPACE;

public class CookingRunner extends Runner.Runner {
    private final APIContext apiContext;
    private final Configuration configuration;
    private final Status status;

    private final Food food;

    public CookingRunner(APIContext apiContext, Configuration configuration, Status status, Food food) {
        super(apiContext, configuration, status);
        this.configuration = configuration;
        this.apiContext = apiContext;
        this.status = status;
        this.food = food;
    }

    @Override
    protected boolean DoNothingCondition() {
        return apiContext.localPlayer().isMoving();
    }

    @Override
    protected boolean IsDoingActivity() {
        return apiContext.localPlayer().getAnimation() == 896;
    }

    private boolean cookingInterfaceOpen() {
        return apiContext.widgets().get(270).getChild(5).isVisible();
    }

    private static int getShortSleepTime() {
        int SLEEP_MIN = 250;
        int SLEEP_MAX = 400;
        return Random.nextInt(SLEEP_MIN, SLEEP_MAX);
    }

    @Override
    protected int DoActivity() {
        GameEntity cookingRange = configuration.get_entity.Get(apiContext);
        if (cookingRange == null) {
            status.message = "Error - could not find cooking range";
            return configuration.default_delay;
        }
        boolean success = cookingRange.interact("Cook");
        if (!success) {
            status.message = "Error - could not start cooking";
            return 600;
        }
        Time.sleep(2000, this::cookingInterfaceOpen);
        if (cookingInterfaceOpen()) {
            apiContext.keyboard().holdKey(VK_SPACE, getShortSleepTime());
            status.message = "Started cooking";
            Time.sleep(2000, this::IsDoingActivity);
            return 600;
        }
        status.message = "Error - Cooking interface not opened";
        return 600;
    }

    @Override
    protected boolean ShouldDeposit() {
        return !apiContext.inventory().isEmpty() && !apiContext.inventory().contains(food.raw_name);
    }
}
