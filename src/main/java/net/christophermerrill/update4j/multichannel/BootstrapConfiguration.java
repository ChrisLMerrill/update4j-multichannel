package net.christophermerrill.update4j.multichannel;

import java.awt.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BootstrapConfiguration
    {
    /**
     * Title displayed to user
     *
     * Example: "My Application"
     */
    public abstract String getLauncherTitle();

    /**
     * Name of the update/launch log file.
     */
    public String getBootstrapLogFilename()
        {
        return "launcher-log.txt";
        }

    /**
     * Name of the home folder name for the application. Settings file, log file, etc. will be stored here.
     *
     * Example: ".myapp"
     */
    public abstract String getHomeFolderName();

    /**
     * Name of settings file
     */
    public String getSettingsFilename()
        {
        return "launcher.properties";
        }

    /**
     * Supported release channels. The DEFAULT channle must be first.
     *
     * Example: "stable", "eap", "dev"
     */
    public abstract String[] getAvailableReleaseChannelNames();

    /**
     * The fully-qualified name of the class to launch.
     */
    public abstract String getApplicationClassname();

    public abstract String getUpdateSiteUrl();

    public String[] getAppParameters()
        {
        return new String[0];
        }

    /**
     * An icon to show in UI.
     */
    public abstract Image getApplicationIcon();

    public File getBaseFolder()
        {
        if (_base == null)
            _base = new File(new File(System.getProperties().getProperty("user.home")), getHomeFolderName());
        return _base;
        }

    private File _base = null;
    }