package net.christophermerrill.update4j.multichannel.ui;

import net.christophermerrill.update4j.multichannel.*;
import net.christophermerrill.update4j.multichannel.core.*;
import org.update4j.*;
import org.update4j.service.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VisibleUpdateController extends JFrame
    {
    public VisibleUpdateController(UpdateLaunchBootstrap bootstrap)
        {
        setTitle(bootstrap.getConfig().getLauncherTitle());
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.gray, 5, true));
        setUndecorated(true);
        setIconImage(bootstrap.getConfig().getApplicationIcon());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);  // without this, the JVM will not terminate when the app is closed
        _layout = new CardLayout();
        getContentPane().setLayout(_layout);
        getContentPane().add(_log_panel, "logs");
        getContentPane().add(_checking_panel, "checking");
        getContentPane().add(_updating_panel, "updating");
        LaunchingPanel launching_panel = new LaunchingPanel(this);
        getContentPane().add(launching_panel, "launching");
        _layout.show(getContentPane(), "checking");

        pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - getHeight()) / 2);
        setLocation(x, y);

        setVisible(true);

        UpdateHandlerEventRelay.addListener(_my_handler);
        AppLauncher.LISTENERS.add(_launch_listener);

        CheckForUpdates updater = new CheckForUpdates(bootstrap);
        updater.addListener(new CheckForUpdates.Listener()
            {
            @Override
            public void startManifestDownload()
                {
                _checking_panel.setMessage("Downloading manifest...");
                }

            @Override
            public void completed(File config_file, Configuration configuration, boolean updates_available)
                {
                if (config_file != null && config_file.exists())
                    {
                    _checking_panel.setMessage("Manifest download complete.");
                    SimpleLogger.log(String.format("UpdateCheck complete. Updates available=%s, config file is at %s", updates_available, config_file.getAbsolutePath()));
                    }
                else
                    {
                    _checking_panel.setMessage("Manifest download failed.");
                    SimpleLogger.log("UpdateCheck failed. Updated manifest not available");
                    }
                if (updates_available)
                    new UpdateApp(configuration).start();
                else
                    new Thread(configuration::launch).start();
                }
            });
        updater.start();
        }

    private void addMessage(String s)
        {
        System.out.println(s);
        _log_panel.addMessage(s + "\n");
        }

    private void startShutdown()
        {
        Timer timer = new Timer(3000, (event) ->
            {
            if (!_shutdown_cancelled)
                shutdown();
            });
        timer.setRepeats(false);
        timer.start();
        }

    void shutdown()
        {
        setVisible(false);
        dispose();
        }

    private final CardLayout _layout;

    private final LogPanel _log_panel = new LogPanel();
    private final CheckingUpdatesPanel _checking_panel = new CheckingUpdatesPanel();
    private final UpdatingPanel _updating_panel = new UpdatingPanel();
    private int _update_file_count = 0;
    private boolean _shutdown_cancelled = false;

    @SuppressWarnings("FieldCanBeLocal")
    private final AppLauncher.LaunchListener _launch_listener = new AppLauncher.LaunchListener()
        {
        @Override
        public void starting()
            {
            _layout.show(getContentPane(), "launching");
            startShutdown();
            }

        @Override
        public void completed(Throwable exception)
            {
            if (exception != null)
                _shutdown_cancelled = true;
            }
        };

    @SuppressWarnings("FieldCanBeLocal")
    private final UpdateHandler _my_handler = new UpdateHandler()
        {
        @Override
        public void startCheckUpdates()
            {
            addMessage("starting update checks");
            _checking_panel.setMessage("starting update checks");
            }

        @Override
        public void startCheckUpdateFile(FileMetadata file)
            {
            addMessage("checking asset " + file.getPath());
            String name = file.getPath().toString();
            int index = name.lastIndexOf("/");
            if (index == -1)
                index = name.lastIndexOf("\\");
            if (index > 0)
                name = name.substring(index + 1);
            _checking_panel.setMessage("checking " + name);
            }

        @Override
        public void doneCheckUpdateFile(FileMetadata file, boolean requires)
            {
            if (requires)
                _update_file_count++;
            }

        @Override
        public void doneCheckUpdates()
            {
            addMessage("update checks are complete");
            _updating_panel.setMessage("update checks are complete");
            }

        @Override
        public void startDownloads()
            {
            _layout.show(getContentPane(), "updating");
            _updating_panel.setTotalUpdates(_update_file_count);
            addMessage("downloads starting");
            }

        @Override
        public void startDownloadFile(FileMetadata file)
            {
            addMessage("downloading asset " + file.getPath());
            String name = file.getPath().toString();
            int index = name.lastIndexOf("/");
            if (index == -1)
                index = name.lastIndexOf("\\");
            if (index > 0)
                name = name.substring(index + 1);
            _updating_panel.startNewDownload();
            _updating_panel.setMessage(name);
            }

        @Override
        public void updateDownloadProgress(float frac)
            {
            _updating_panel.setProgress(frac);
            }

        @Override
        public void doneDownloads()
            {
            addMessage("downloads complete");
            }

        @Override
        public void failed(Throwable t)
            {
            addMessage("update failed due to " + t.getMessage());
            _updating_panel.setMessage("update failed due to " + t.getMessage());
            _layout.show(getContentPane(), "launching");
            }

        @Override
        public void succeeded()
            {
            addMessage("update complete");
            _updating_panel.setMessage("update complete");
            _layout.show(getContentPane(), "launching");
            }

        public void init(UpdateContext context) {}
        public void updateCheckUpdatesProgress(float frac) {}
        public void updateDownloadFileProgress(FileMetadata file, float frac) {}
        public void validatingFile(FileMetadata file, Path tempFile) {}
        public void doneDownloadFile(FileMetadata file, Path tempFile) {}

        public void stop() {}
        public long version() { return 0; }
        };
    }
