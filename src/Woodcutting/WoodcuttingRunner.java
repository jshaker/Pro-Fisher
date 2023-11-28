package Woodcutting;

import Runner.Configuration;
import Runner.Runner;
import Runner.Status;
import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.SceneObject;

class WoodcuttingRunner extends Runner {
    private final APIContext apiContext;
    private final Configuration configuration;
    private final Status status;

    private final Tree tree;

    public WoodcuttingRunner(APIContext apiContext, Configuration configuration, Status status, Tree tree) {
        super(apiContext, configuration, status);
        this.configuration = configuration;
        this.apiContext = apiContext;
        this.status = status;
        this.tree = tree;
    }

    @Override
    protected boolean DoNothingCondition() {
        return apiContext.localPlayer().isMoving();
    }

    @Override
    protected boolean IsDoingActivity() {
        return apiContext.localPlayer().getAnimation() == 879 || apiContext.localPlayer().getAnimation() == 873;
    }


    @Override
    protected int DoActivity() {
        SceneObject nearestTree = getNearestTree();
        if (nearestTree == null) {
            status.message = "Error - could not find tree";
            return super.WalkToAreaOfInterest();
        }
        return startChopping(nearestTree);
    }

    @Override
    protected boolean ShouldDeposit() {
        return apiContext.inventory().isFull();
    }

    private SceneObject getNearestTree() {
        return apiContext.objects().query().nameMatches(tree.name).actions("Chop down").results().nearest();
    }

    private int startChopping(SceneObject nearestTree) {
        boolean success = nearestTree.interact("Chop down");
        if (!success) {
            status.message = "Error - could not start chopping";
        }
        status.message = "Starting chopping";
        return configuration.default_delay;
    }
}