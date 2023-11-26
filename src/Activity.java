import Runner.Status;

public interface Activity {


    public abstract Status Do();

    public abstract boolean IsDoingActivity();
}
