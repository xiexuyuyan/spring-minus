package droid.server.pm;

import droid.content.PackageInfo;

public abstract class InstallManager {
    public abstract PackageInfo[] load(String[] jarFilePaths);
    public abstract PackageInfo load(String jarFilePath);
}
