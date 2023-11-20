import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.GameType;
import com.epicbot.api.shared.methods.ICameraAPI;
import com.epicbot.api.shared.script.LoopScript;
import com.epicbot.api.shared.script.ScriptManifest;
import com.epicbot.api.shared.util.paint.frame.PaintFrame;

import java.awt.*;

@ScriptManifest(name = "Pro Fisher", gameType = GameType.OS)
public class Main extends LoopScript {

    private Status status;
    private IRunner runner;


    @Override
    public boolean onStart(String... strings) {
        status = new Status("Starting");
        runner = DraynorFishingGenerator.Generate(getAPIContext(), status);
        ICameraAPI cam = getAPIContext().camera();

        cam.setPitch(98, false);
        cam.setYawDeg(0, false);
        return true;
    }


    @Override
    protected int loop() {
        return runner.Run();
    }

    @Override
    protected void onPaint(Graphics2D g, APIContext ctx) {
        PaintFrame frame = new PaintFrame(runner.Name());
        frame.addLine("status", status.message);
        frame.draw(g, 0, 170, ctx);
    }
}