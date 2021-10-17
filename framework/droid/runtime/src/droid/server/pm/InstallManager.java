package droid.server.pm;

import droid.content.PackageInfo;

import java.io.IOException;
import java.io.InputStream;

public abstract class InstallManager {
    public abstract PackageInfo[] load(String[] jarFilePaths) throws IOException;
    public abstract PackageInfo load(String jarFilePath) throws IOException;
    public abstract InputStream getResourceStream(String jarFilePath, String targetResourceName);
}
