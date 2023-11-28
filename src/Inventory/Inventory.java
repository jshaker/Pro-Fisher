package Inventory;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.ItemWidget;
import com.epicbot.api.shared.methods.IInventoryAPI;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Inventory {
    public static boolean InventoryContainsExactly(IInventoryAPI inventory, Map<String, Integer> counts) {
        Map<String, Integer> actual = GetInventoryItemCounts(inventory);
        if (actual.size() != counts.size()) { return false; }
        for (Map.Entry<String, Integer> kv : actual.entrySet()) {
            if (!Objects.equals(kv.getValue(), counts.get(kv.getKey()))) {
                return false;
            }
        }
        return true;
    }

    public static Map<String, Integer> GetInventoryItemCounts(IInventoryAPI inventory) {
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < 28; i++) {
            ItemWidget item = inventory.getItemAt(i);
            if (item == null) {
                continue;
            }
            if (!counts.containsKey(item.getName())) {
                counts.put(item.getName(), 0);
            }
            int count = 1;
            if (item.isStackable()) {
                count = item.getStackSize();
            }
            counts.put(item.getName(), counts.get(item.getName()) + count);
        }
        return counts;
    }

    public static Map<String, Integer> CountMissing(IInventoryAPI inventory, Map<String, Integer> expected) {
        Map<String, Integer> inventoryCount = GetInventoryItemCounts(inventory);
        Map<String, Integer> diff = new HashMap<>(expected);
        for (Map.Entry<String, Integer> kv: expected.entrySet()) {
            if (!inventoryCount.containsKey(kv.getKey())) {
                continue;
            }
            Integer count = inventoryCount.get(kv.getKey());
            diff.put(kv.getKey(), kv.getValue() - count);
        }
        diff.values().removeIf(f -> f <= 0);
        return diff;
    }

    public static Map<String, Integer> CountExcess(IInventoryAPI inventory, Map<String, Integer> expected) {
        Map<String, Integer> inventoryCount = GetInventoryItemCounts(inventory);
        if (expected.containsKey("Coins")) {
            inventoryCount.remove("Coins");
        }
        Map<String, Integer> diff = new HashMap<>(inventoryCount);
        for (Map.Entry<String, Integer> kv: expected.entrySet()) {
            if (!inventoryCount.containsKey(kv.getKey())) {
                continue;
            }
            Integer count = inventoryCount.get(kv.getKey());
            diff.put(kv.getKey(), Math.max(count - kv.getValue(), 0));
        }
        diff.values().removeIf(f -> f == null || f == 0);
        return diff;
    }

    public static boolean HasMustHave(IInventoryAPI inventory, Map<String, Integer> must_have) {
        return CountMissing(inventory, must_have).isEmpty();
    }
}
