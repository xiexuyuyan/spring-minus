package droid.content;

public class ComponentName {
    String mPackageName;
    String mClassName;

    public ComponentName() {

    }

    public ComponentName(String mPackageName, String mClassName) {
        this.mPackageName = mPackageName;
        this.mClassName = mClassName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public void setClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    @Override
    public String toString() {
        return "ComponentName{" +
                "mPackageName='" + mPackageName + '\'' +
                ", mClassName='" + mClassName + '\'' +
                '}';
    }
}
