package net.christophermerrill.update4j.multichannel.core;

import net.christophermerrill.update4j.multichannel.*;
import org.update4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CheckForUpdates implements Runnable
    {
    public CheckForUpdates(UpdateLaunchBootstrap bootstrap)
        {
        _bootstrap = bootstrap;
        }

    public void start()
        {
        _thread = new Thread(this);
        _thread.start();
        }

    @Override
    public void run()
        {
        try
            {
            checkForUpdates();
            }
        catch (InterruptedException e)
            {
            SimpleLogger.log("Update check cancelled");
            for (Listener listener : _listeners)
                listener.completed(_config_file, _config, false);
            }
        catch (Exception e)
            {
            for (Listener listener : _listeners)
                listener.completed(_config_file, _config, false);
            }
        }

    @SuppressWarnings("RedundantThrows")
    private void checkForUpdates() throws InterruptedException
        {
        for (Listener listener : _listeners)
            listener.startManifestDownload();

        // download the latest config file for the channel
        _config_file = new File(_bootstrap.getConfig().getBaseFolder(), "channel/" + _bootstrap.getSettings().getSelectedChannel() + ".xml");
        if (_config_file.exists())
            _config = loadConfig(_config_file);
        File config_folder = _config_file.getParentFile();
        if (!config_folder.exists() && !config_folder.mkdirs())
            {
            SimpleLogger.log("Unable to create channel folder: " + _config_file.getParentFile().getAbsolutePath());
            for (Listener listener : _listeners)
                listener.completed(_config_file, _config, false);
            return;
            }

        String url_configured = _bootstrap.getSettings().getConfigUrl(_bootstrap.getSettings().getSelectedChannel());
        try
            {
            URL url = new URL(url_configured);
            SimpleLogger.log(String.format("Downloading config file from %s into %s", url, _config_file.getAbsolutePath()));

            try (InputStream instream = url.openStream(); FileOutputStream outstream = new FileOutputStream(_config_file))
                {
                byte[] buffer = new byte[5000];
                int available;
                int total = 0;
                while ((available = instream.read(buffer, 0, buffer.length)) != -1)
                    {
                    outstream.write(buffer, 0, available);
                    total += available;
                    }
                SimpleLogger.log(String.format("Config file download complete. %d bytes written.", total));
                _config = loadConfig(_config_file);
                }
            catch (FileNotFoundException e)
                {
                // ok if not found
                }
            catch (IOException e)
                {
                SimpleLogger.log("Unable to download updated config file", e);
                for (Listener listener : _listeners)
                    listener.completed(_config_file, _config, false);
                return;
                }
            }
        catch (MalformedURLException e)
            {
            SimpleLogger.log("Can't attempt config file download due to malformed URL: " + url_configured, e);
            for (Listener listener : _listeners)
                listener.completed(_config_file, _config, false);
            return;
            }

        boolean update = (_config != null) && updateAvailable();
        for (Listener listener : _listeners)
            listener.completed(_config_file, _config, update);
        }

    @SuppressWarnings("unused")  // not supported in UI, yet
    public void cancel()
        {
        _thread.interrupt();
        }

    public static Configuration loadConfig(File config_file)
        {
        try (FileInputStream instream = new FileInputStream(config_file))
            {
            SimpleLogger.log("Reading config from " + config_file.getCanonicalPath());
            Configuration config = Configuration.read(new InputStreamReader(instream));
            SimpleLogger.log("config loaded.");
            return config;
            }
        catch (Exception e)
            {
            SimpleLogger.log("Unable to load config file.", e);
            return null;
            }
        }

    private boolean updateAvailable()
        {
        try
            {
            SimpleLogger.log("Checking for updates...");
            boolean update = _config.requiresUpdate();
            SimpleLogger.log("Update available: " + update);
            return _config != null && update;
            }
        catch (IOException e)
            {
            SimpleLogger.log("Cannot determine if updates are available. Will not attempt.", e);
            return false;
            }
        }
    public void addListener(Listener listener)
        {
        _listeners.add(listener);
        }

    private final UpdateLaunchBootstrap _bootstrap;
    private final Set<Listener> _listeners = new HashSet<>();
    private File _config_file;
    private Configuration _config;
    private Thread _thread;

    public interface Listener
        {
        void startManifestDownload();
        void completed(File config_file, Configuration configuration, boolean updates_available);
        }
    }
