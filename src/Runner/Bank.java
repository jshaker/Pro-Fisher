package Runner;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.entity.ItemWidget;
import com.epicbot.api.shared.methods.IInventoryAPI;
import com.epicbot.api.shared.util.time.Time;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Bank {

    private final BankConfiguration config;
    private final APIContext apiContext;
    private final Status status;

    public Bank(APIContext apiContext, BankConfiguration config, Status status) throws Exception {
        this.config = config;
        this.apiContext = apiContext;
        this.status = status;
    }

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
            diff.put(kv.getKey(), Math.max(kv.getValue() - count, 0));
        }
        diff.values().removeIf(f -> f == 0);
        return diff;
    }

    public static Map<String, Integer> CountExcess(IInventoryAPI inventory, Map<String, Integer> expected) {
        Map<String, Integer> inventoryCount = GetInventoryItemCounts(inventory);
        inventoryCount.remove("Coins");
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

    private static void debugMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> kv: map.entrySet()) {
            System.out.println(kv.getKey() + " : " + kv.getValue());
        }
    }

    public int Deposit() {
        if (apiContext.bank().isOpen()) {
            if (!apiContext.inventory().isEmpty()) {
                status.message = "Depositing";
                if (config.deposit_all) {
                    apiContext.bank().depositInventory();
                } else {
                    Map<String, Integer> excess = CountExcess(apiContext.inventory(), config.withdraw_items);
                    for (Map.Entry<String, Integer> kv : excess.entrySet()) {
                        apiContext.bank().deposit(kv.getValue(), kv.getKey());
                    }
                }
            }
            Time.sleep(2000, () -> InventoryContainsExactly(apiContext.inventory(), config.withdraw_items));
        } else {
            status.message = "Walking to bank to deposit";
            apiContext.webWalking().walkToBank(config.bank);
            apiContext.bank().open();
        }
        return 600;
    }
    public int Withdraw() {
        if (apiContext.bank().isOpen()) {
            status.message = "Withdrawing";
            if (config.withdraw_items.containsKey("Coins")) {
                apiContext.bank().withdrawAll("Coins");
            }
            Map<String, Integer> missing = CountMissing(apiContext.inventory(), config.withdraw_items);
            String[] failedToWithdrawItems = missing.entrySet().stream().filter(kv -> !apiContext.bank().withdraw(kv.getValue(), kv.getKey())).map(Map.Entry::getKey).toArray(String[]::new);
            if (failedToWithdrawItems.length > 0) {
                String error = "Error - Failed to withdraw: " + Arrays.toString(failedToWithdrawItems);
                System.out.println(error);
                status.message = error;
                return -1;
            }
        } else {
            status.message = "Walking to bank to withdraw";
            apiContext.webWalking().walkToBank(config.bank);
            apiContext.bank().open();
        }
        return 600;
    }
}