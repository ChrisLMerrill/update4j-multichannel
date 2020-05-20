package net.christophermerrill.update4j.multichannel;

import net.christophermerrill.update4j.multichannel.core.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BootstrapSettings
    {
    public BootstrapSettings(BootstrapConfiguration config, File settings_folder)
        {
        _config = config;
        _settings_folder = settings_folder;
        }

    public boolean suppressUI()
        {
        return getBooleanProperty(SUPPRESS_UI_PARAM, false);
        }

    public String getSelectedChannel()
        {
        return _props.getProperty(SELECTED_CHANNEL_PARAM, _config.getAvailableReleaseChannelNames()[0]);
        }

    public boolean skipUpdateCheck()
        {
        return getBooleanProperty(SKIP_UPDATE_CHECK, false);
        }

    public String getAppClassname()
        {
        return _props.getProperty(APP_CLASS_PARAM, _config.getApplicationClassname());
        }

    public void setDefaults()
        {
        _props.setProperty(SELECTED_CHANNEL_PARAM, _config.getAvailableReleaseChannelNames()[0]);
        _props.setProperty(SUPPRESS_UI_PARAM, Boolean.toString(false));
        _props.setProperty(SKIP_UPDATE_CHECK, Boolean.toString(false));
        }

    public File getSettingsFolder()
        {
        return _settings_folder;
        }

    public String getConfigUrl(String channel)
        {
        String url = _props.getProperty(URL_PARAM_BASE);  // user could override via settings file
        if (url == null)
            url = _config.getUpdateSiteUrl();

        return String.format("%s%s.xml", url, channel);
        }

    public String[] getAppParameters()
        {
        String[] params;
        String params_custom = _props.getProperty(APP_PARAMS);  // user could override via settings file
        if (params_custom != null)
            params = params_custom.split(" ");
        else
            params = _config.getAppParameters();
        return params;
        }

    @SuppressWarnings("SameParameterValue")
    private boolean getBooleanProperty(String name, boolean default_value)
        {
        String prop = _props.getProperty(name);
        if (prop == null)
            return default_value;
        return Boolean.parseBoolean(prop);
        }

    public boolean load() throws IOException
        {
        File file = new File(_settings_folder, _config.getSettingsFilename());
        _props = new Properties();
        if (file.exists())
            try
                {
                FileInputStream instream = new FileInputStream(file);
                _props.load(instream);
                instream.close();
                return true;
                }
            catch (IOException e)
                {
                SimpleLogger.log("Unable to load settings from " + file.getAbsolutePath(), e);
                throw e;
                }
        else
            return false;
        }

    public void save() throws IOException
        {
        File file = new File(_settings_folder, _config.getSettingsFilename());
        try (FileOutputStream outstream = new FileOutputStream(file))
            {
            _props.store(outstream, _config.getLauncherTitle() + " properties");
            }
        catch (IOException e)
            {
            SimpleLogger.log("Unable to store " + file.getName(), e);
            throw e;
            }
        }

    private final BootstrapConfiguration _config;
    private final File _settings_folder;
    private Properties _props;

    protected static final String SELECTED_CHANNEL_PARAM = "selected_channel";
    protected static final String SUPPRESS_UI_PARAM = "suppress_ui";
    protected static final String SKIP_UPDATE_CHECK = "skip_update_check";
    protected static final String APP_CLASS_PARAM = "app_classname";
    protected static final String URL_PARAM_BASE = "url_base";
    protected static final String APP_PARAMS = "params";
    }