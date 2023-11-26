package Fishing;

import Runner.Configuration;
import Runner.Runner;
import Runner.Status;
import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;

class FishingRunner extends Runner {
    private final APIContext apiContext;
    private final Configuration configuration;
    private final Status status;

    private final String interaction_name;

    public FishingRunner(APIContext apiContext, Configuration configuration, Status status, String interaction_name) {
        super(apiContext, configuration, status);
        this.configuration = configuration;
        this.apiContext = apiContext;
        this.status = status;
        this.interaction_name = interaction_name;
    }

    @Override
    protected boolean DoNothingCondition() {
        return apiContext.localPlayer().isMoving();
    }

    @Override
    protected boolean IsDoingActivity() {
        return apiContext.localPlayer().getInteracting() != null && apiContext.localPlayer().getInteracting().getName().equals("Fishing spot");
    }

    @Override
    protected int DoActivity() {
        NPC fishingSpot = getNearestFishingSpot();
        if (fishingSpot == null) {
            status.message = "Error - could not find fishing spot";
            return configuration.default_delay;
        }
        return startFishing(fishingSpot);
    }

    @Override
    protected boolean ShouldDeposit() {
        return apiContext.inventory().isFull();
    }

    private NPC getNearestFishingSpot() {
        return apiContext.npcs().query().nameMatches("Fishing spot").actions(interaction_name).results().nearest();
    }

    private int startFishing(NPC fishingSpot) {
        boolean success = fishingSpot.interact(interaction_name);
        if (!success) {
            status.message = "Error - could not start fishing";
        }
        status.message = "Starting fishing";
        return 600;
    }
}