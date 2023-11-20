import com.epicbot.api.shared.APIContext;
import com.epicbot.api.shared.util.time.Time;
import com.epicbot.api.shared.webwalking.model.RSBank;

public class Bank {

    private final BankConfiguration config;
    private final APIContext apiContext;
    private final Status status;
    public Bank(APIContext apiContext, BankConfiguration config, Status status) {
        this.config = config;
        this.apiContext = apiContext;
        this.status = status;
    }

    public Status Run() {
        if(apiContext.bank().isOpen()) {
            if(!apiContext.inventory().isEmpty()) {
                for (String depositItem : config.depositItems) {
                    apiContext.bank().depositAll(depositItem);
                }
                Time.sleep(2000, () -> config.condition.Evaluate());
            }
        } else {
            status.message = "Walking to bank";
            apiContext.webWalking().walkToBank(RSBank.DRAYNOR);
            apiContext.bank().open();
        }
        return status;
    }
}