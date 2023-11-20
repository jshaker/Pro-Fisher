import com.epicbot.api.shared.APIContext;

public interface Activity {


    public abstract Status Do();

    public abstract boolean IsDoingActivity();
}
