package meta.events;

public abstract class Event {
    protected boolean cancelled;

    public abstract boolean cancellable();

    public void cancel(){
        cancelled = true;
    }

    public boolean cancelled(){
        return cancelled;
    }
}
