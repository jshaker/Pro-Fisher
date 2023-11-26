package Cooking;

import java.util.ArrayList;
import java.util.List;

public class Foods {

    public static final Food Swordfish = new Food("Swordfish", "Raw swordfish", "Burnt swordfish");
    public static final Food Tuna = new Food("Tuna", "Raw tuna", "Burnt fish");
    public static final Food Lobster = new Food("Lobster", "Raw lobster", "Burnt lobster");

    private static final List<Food> foods = new ArrayList<>();

    static {
        foods.add(Lobster);
        foods.add(Tuna);
        foods.add(Swordfish);
    }

    public static List<Food> GetUncookedFoods() {
        return foods;
    }
}
