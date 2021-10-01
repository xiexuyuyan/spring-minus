package droid.content.pm;

import java.util.List;
import java.util.Map;

public abstract class PackageManager {

    public abstract List<PackageInfo> getAllPackages();
    public abstract void updatePackages(Map<ClassLoader, List<PackageInfo>> packageLinkedPackagesList);
}
