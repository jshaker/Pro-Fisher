import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class DraynorFishing extends Runner {
    private final APIContext apiContext;
    private final Configuration configuration;
    private final Status status;

    public DraynorFishing(APIContext apiContext, Configuration configuration, Status status) {
        super(apiContext, configuration, status);
        this.configuration = configuration;
        this.apiContext = apiContext;
        this.status = status;
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
        if (!canReachFishingSpot(fishingSpot)) {
            return walkToFishingSpot(fishingSpot);
        }
        return startFishing(fishingSpot);
    }

    @Override
    protected boolean ShouldBank() {
        return apiContext.inventory().isFull();
    }

    private NPC getNearestFishingSpot() {
        return apiContext.npcs().query().nameMatches("Fishing spot").results().nearest();
    }

    private int startFishing(NPC fishingSpot) {
        boolean success = fishingSpot.interact("Small Net");
        if (!success) {
            status.message = "Error - could not start fishing";
        }
        status.message = "Starting fishing";
        return 600;
    }

    private boolean canReachFishingSpot(NPC fishingSpot) {
        return apiContext.localPlayer().getLocation().canReach(apiContext, fishingSpot.getLocation());
    }

    private int walkToFishingSpot(NPC fishingSpot) {
        status.message = "Walking to fishing spot";
        Tile nearest = apiContext.walking().getClosestTileOnMap(fishingSpot.getLocation());
        apiContext.webWalking().walkTo(nearest);
        return 600;
    }
}
