public class ThreadTest {
    public static void main(String[] args) {
        String cm = "cm create com.yuyan.harp/.MainController";
        String[] classComponent = cm.split("/");
        String packageName = classComponent[0];
        System.out.println("packageName = " + packageName);
        String fullClassName = packageName + classComponent[1];
        System.out.println("fullClassName = " + fullClassName);
    }
}
