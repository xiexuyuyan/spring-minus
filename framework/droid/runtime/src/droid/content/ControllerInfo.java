package droid.content;

public class ControllerInfo {

    private Action[] actions;
    private String mPackageName;
    private String mClassName;

    public ControllerInfo(String mPackageName, String mClassName) {
        this.mPackageName = mPackageName;
        this.mClassName = mClassName;
    }

    public ControllerInfo() {
    }

    public ControllerInfo(ComponentName componentName) {
        mPackageName = componentName.getPackageName();
        mClassName = componentName.getClassName();
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public Action[] getActions() {
        return actions;
    }

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public Parameter[] completeAction(Argument[] arguments) {
        for (Action action : actions) {
            Parameter[] parameters = action.getParameters();
            if (!(parameters.length == arguments.length)) {
                continue;
            }
            int j = 0;
            for (; j < parameters.length; j++) {
                if (!parameters[j].getName().equals(arguments[j].getName())) {
                    break;
                }
            }
            if (j == parameters.length) {
                return parameters;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (actions == null || actions.length == 0) {
            return "mPackageName = " + mPackageName
                    + ", mClassName = " + mClassName
                    + ", actions = null.";
        }
        StringBuilder actionsString = new StringBuilder();
        for (int i = 0; i < actions.length; i++) {
            actionsString.append(i);
            actionsString.append(". ");
            actionsString.append(actions[i]);
            actionsString.append(" ");
        }
        return "mPackageName = " + mPackageName
                + ", mClassName = " + mClassName
                + ", actionsString = [" + actionsString + "]";
    }
}
