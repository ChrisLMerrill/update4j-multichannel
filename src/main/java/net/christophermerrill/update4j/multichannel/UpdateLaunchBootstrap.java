package net.christophermerrill.update4j.multichannel;

import net.christophermerrill.update4j.multichannel.core.*;
import net.christophermerrill.update4j.multichannel.ui.*;
import org.update4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")
public abstract class UpdateLaunchBootstrap
    {
    public void boot() throws IOException
        {
        INSTANCE = this;

        // load config
        _config = createConfig();
        File settings_folder = _config.getBaseFolder();
        if (!settings_folder.exists() && !settings_folder.mkdirs())
            {
            String message = "Unable to create settings folder: " + settings_folder.getAbsolutePath();
            System.out.println(message);
            throw new IOException(message);
            }

        // setup logging
        SimpleLogger.LOGNAME = new File(settings_folder, _config.getBootstrapLogFilename()).getAbsolutePath();
        SimpleLogger.log(String.format("%s Bootstrap is starting in %s ...", _config.getLauncherTitle(), System.getProperty("user.dir")));
        UpdatePrinter.PRINTER = SimpleLogger.getPrinter();
        UpdateHandlerEventRelay.addListener(new UpdatePrinter());

        // load settings
        _settings = createSettings(_config, settings_folder);
        if (!_settings.load())
            {
            _settings.setDefaults();
            _settings.save();
            }

        if (_settings.skipUpdateCheck())
            {
            SimpleLogger.log("Skipping update check (due to configuration setting).");
            String channel = _settings.getSelectedChannel();
            System.setProperty("release.channel", channel);
            File config_file = new File(_settings.getSettingsFolder(), "channel/" + channel + ".xml");
            Configuration config = CheckForUpdates.loadConfig(config_file);
            if (config != null)
                config.launch();
            }
        else
            {
            // run the Update GUI or not?
            if (_settings.suppressUI())
                new InvisibleUpdateController(this).run();
            else
                new VisibleUpdateController(this);
            }
        }

    protected abstract BootstrapConfiguration createConfig();

    protected abstract BootstrapSettings createSettings(BootstrapConfiguration config, File settings_folder);

    public BootstrapConfiguration getConfig()
        {
        return _config;
        }

    public BootstrapSettings getSettings()
        {
        return _settings;
        }

    protected BootstrapSettings _settings;
    protected BootstrapConfiguration _config;
    public static UpdateLaunchBootstrap INSTANCE;
    }