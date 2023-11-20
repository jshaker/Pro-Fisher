import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.entity.NPC;
import com.epicbot.api.shared.methods.ICameraAPI;
import com.epicbot.api.shared.model.Area;
import com.epicbot.api.shared.model.Tile;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;
import com.epicbot.api.shared.util.time.Time;
import com.epicbot.api.shared.webwalking.model.RSBank;
import com.epicbot.api.shared.methods.IWalkingAPI;

import java.awt.*;
import java.util.Objects;

@ScriptManifest(name = "Pro Fisher", gameType = GameType.OS)
public class Main extends LoopScript {
    private String status = "Starting";
    private final int DEFAULT_DELAY = 50;
    private final Area FISHING_AREA = new Area(new Tile(3086,3232), 8);
    private final Area SAFE_AREA = new Area(new Tile(3087,3227), 2);

    private void addArea(Area a1, Area a2) {
        Tile[] tiles = a2.getTiles();
        for (Tile tile : tiles) {
            a1.add(tile.getX(), tile.getY());
        }
    }

    private NPC getNearestFishingSpot() {
        return getAPIContext().npcs().query().nameMatches("Fishing spot").results().nearest();
    }

    @Override
    public boolean onStart(String... strings) {
        ICameraAPI cam = getAPIContext().camera();

        cam.setPitch(98, false);
        cam.setYawDeg(0, false);
        return true;
    }

    private boolean levelUpInterfacePresent() {
        if (getAPIContext().widgets().get(233).isVisible()) {
            return true;
        }

        return false;
    }

    private int escapeBattle() {
        status = "Escaping combat";
        getAPIContext().webWalking().walkTo(SAFE_AREA.getRandomTile());
        return DEFAULT_DELAY;
    }

    private int walkToArea() {
        status = "Walking to fishing area";
        getAPIContext().webWalking().walkTo(FISHING_AREA.getRandomTile());
        return DEFAULT_DELAY;
    }

    private int walkToFishingSpot(NPC fishingSpot) {
        status = "Walking to fishing spot";
        Tile nearest = getAPIContext().walking().getClosestTileOnMap(fishingSpot.getLocation());
        getAPIContext().webWalking().walkTo(nearest);
        return 600;
    }

    private int startFishing(NPC fishingSpot) {
        boolean success = fishingSpot.interact("Small Net");
        if (!success) {
            status = "Error - could not start fishing";
        }
        status = "Starting fishing";
        return 600;
    }

    private int busyFishing() {
        status = "Fishing";
        return DEFAULT_DELAY;
    }

    private boolean isFishing() {
        return getAPIContext().localPlayer().getInteracting() != null && getAPIContext().localPlayer().getInteracting().getName().equals("Fishing spot");
    }

    private boolean canReachFishingSpot(NPC fishingSpot) {
        return getAPIContext().localPlayer().getLocation().canReach(getAPIContext(), fishingSpot.getLocation());
    }

    @Override
    protected int loop() {
        if (getAPIContext().localPlayer().isMoving()) {
            return DEFAULT_DELAY;
        }
        if (getAPIContext().inventory().isFull()) {
            return bank();
        }
        if (getAPIContext().localPlayer().isInCombat()) {
            return escapeBattle();
        }
        if (!FISHING_AREA.contains(getAPIContext().localPlayer().getLocation())) {
            return walkToArea();
        }
        if (isFishing()) {
            if (!levelUpInterfacePresent()) {
                return busyFishing();
            }
            System.out.println("Level up interface detected");
        }
        NPC fishingSpot = getNearestFishingSpot();
        if (fishingSpot == null) {
            status = "Error - could not find fishing spot";
            return DEFAULT_DELAY;
        }
        if (!canReachFishingSpot(fishingSpot)) {
            return walkToFishingSpot(fishingSpot);
        }
        return startFishing(fishingSpot);
    }

    @Override
    protected void onPaint(Graphics2D g, APIContext ctx) {
        PaintFrame frame = new PaintFrame("Jeremie's Pro Fisher");
        frame.addLine("status", status);
        frame.draw(g, 0, 170, ctx);
    }

    private int bank() {
        if(getAPIContext().bank().isOpen()) {
            if(!getAPIContext().inventory().isEmpty()) {
                getAPIContext().bank().depositAll("Raw anchovies");
                getAPIContext().bank().depositAll("Raw shrimps");
                Time.sleep(2000, () -> getAPIContext().inventory().onlyContains("Small fishing net"));
            }
        } else {
            status = "Walking to bank";
            getAPIContext().webWalking().walkToBank(RSBank.DRAYNOR);
            getAPIContext().bank().open();
        }
        return DEFAULT_DELAY;
    }
}