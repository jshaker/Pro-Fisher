package Runner;

import com.epicbot.api.shared.webwalking.model.RSBank;

import java.util.Map;

public class BankConfiguration {
    public boolean deposit_all;
    public Map<String, Integer> withdraw_items;
    public RSBank bank;

    public BankConfiguration(RSBank bank, Map<String,Integer> withdraw_items, boolean deposit_all) {
        this.bank = bank;
        this.withdraw_items = withdraw_items;
        this.deposit_all = deposit_all;
    }
}