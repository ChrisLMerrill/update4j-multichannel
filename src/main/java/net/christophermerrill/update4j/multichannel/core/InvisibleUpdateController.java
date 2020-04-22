package net.christophermerrill.update4j.multichannel.core;

import net.christophermerrill.update4j.multichannel.*;
import org.update4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InvisibleUpdateController
    {
    public InvisibleUpdateController(UpdateLaunchBootstrap bootstrap)
        {
        _bootstrap = bootstrap;
        }

    public void run()
        {
        CheckForUpdates updater = new CheckForUpdates(_bootstrap);
        updater.addListener(new CheckForUpdates.Listener()
            {
            @Override
            public void completed(File config_file, Configuration configuration, boolean updates_available)
                {
                SimpleLogger.log(String.format("UpdateCheck complete. Updates available=%s, config file is at %s", updates_available, config_file.getAbsolutePath()));
                if (updates_available)
                    new UpdateApp(configuration).start();
                else
                    configuration.launch();
                }

            public void startManifestDownload() {}
            });
        updater.start();
        }

    private final UpdateLaunchBootstrap _bootstrap;
    }