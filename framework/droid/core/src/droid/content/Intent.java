package droid.content;

public class Intent {

    private ComponentName component;
    private Action action;

    public Intent() {
    }

    public Intent(ComponentName component) {
        this.component = component;
    }

    public Intent(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public Intent setAction(Action action) {
        this.action = action;
        return this;
    }

    public ComponentName getComponent() {
        return component;
    }

    public Intent setComponent(ComponentName component) {
        this.component = component;
        return this;
    }

    @Override
    public String toString() {
        return "Intent{" +
                "component=" + component +
                ", action=" + action +
                '}';
    }
}
