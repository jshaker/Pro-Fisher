package Runner;

import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.util.time.Time;

import java.util.Arrays;
import java.util.Map;

import static Inventory.Inventory.*;


public class Bank {

    private final BankConfiguration config;
    private final APIContext apiContext;
    private final Status status;

    public Bank(APIContext apiContext, BankConfiguration config, Status status) throws Exception {
        this.config = config;
        this.apiContext = apiContext;
        this.status = status;
    }

    private static void debugMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> kv: map.entrySet()) {
            System.out.println(kv.getKey() + " : " + kv.getValue());
        }
    }

    public int Bank() {
        if (apiContext.bank().isOpen()) {
            if (!apiContext.inventory().isEmpty()) {
                return Deposit();
            }
            return Withdraw();
        }
        status.message = "Walking to bank to deposit";
        apiContext.webWalking().walkToBank(config.bank);
        apiContext.bank().open();
        return config.default_delay;
    }

    private int Deposit() {
        status.message = "Depositing";
        if (config.deposit_all) {
            apiContext.bank().depositInventory();
        } else {
            Map<String, Integer> excess = CountExcess(apiContext.inventory(), config.withdraw_items);
            for (Map.Entry<String, Integer> kv : excess.entrySet()) {
                apiContext.bank().deposit(kv.getValue(), kv.getKey());
            }
        }
        return config.default_delay;
    }
    private int Withdraw() {
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
        return config.default_delay;
    }
}