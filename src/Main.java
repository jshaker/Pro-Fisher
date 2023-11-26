import Runner.IRunner;
import Runner.Status;
import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;

import java.awt.*;

@ScriptManifest(name = "Pro Fisher", gameType = GameType.OS)
public class Main extends LoopScript {

    private Status status;
    private IRunner runner;

    private UserInterface userInterface;

    @Override
    public boolean onStart(String... strings) {
        this.status = new Status("Select a runner...");
        this.runner = null;
        this.userInterface = new UserInterface(getAPIContext(), this.status, (IRunner runner) -> {
            this.status.message = String.format("Loading runner: %s", runner.Name());
            System.out.println(status.message);
            this.runner = runner;
        });
        return true;
    }

    @Override
    public void onResume() {

    }


    @Override
    protected int loop() {
        if (this.runner != null) {
            return this.runner.Run();
        }
        return 600;
    }

    @Override
    protected void onPaint(Graphics2D g, APIContext ctx) {
        String name = "Empty";
        if (runner != null) {
            name = runner.Name();
        }
        PaintFrame frame = new PaintFrame(name);
        frame.addLine("status", status.message);
        frame.draw(g, 0, 170, ctx);
    }
}