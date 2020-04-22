package net.christophermerrill.update4j.multichannel.core;

import org.update4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LaunchApp implements Runnable
    {
    public void start(Configuration config)
        {
        _config = config;
        new Thread(this).start();
        }

    public void run()
        {
        _config.launch();
        }

    private Configuration _config;
    }


