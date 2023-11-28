package Woodcutting;

import com.epicbot.api.shared.methods.ILocalPlayerAPI;
import com.epicbot.api.shared.methods.ISkillsAPI;

import java.util.Arrays;

public class Axes {

    public static final Axe bronze = new Axe("Bronze axe", 0);
    public static final Axe iron = new Axe("Iron axe", 0);
    public static final Axe steel = new Axe("Steel axe", 5);
    public static final Axe black = new Axe("Black axe", 11);

    public static Axe[] GetAxesDescending() {
        return new Axe[]{iron, bronze};
    }

    public static Axe[] GetAxesDescendingMeetsRequirements(ISkillsAPI skills) {
        return Arrays.stream(GetAxesDescending()).filter(e -> skills.woodcutting().getCurrentLevel() >= e.required_level).toArray(Axe[]::new);
    }

}
