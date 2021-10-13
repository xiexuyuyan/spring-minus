package droid.server.pm;

import droid.content.PackageInfo;

import java.io.IOException;

public abstract class InstallManager {
    public abstract PackageInfo[] load(String[] jarFilePaths) throws IOException;
    public abstract PackageInfo load(String jarFilePath) throws IOException;
}
