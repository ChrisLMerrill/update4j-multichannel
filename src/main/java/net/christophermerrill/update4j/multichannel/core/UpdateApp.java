package net.christophermerrill.update4j.multichannel.core;

import org.update4j.*;
import org.update4j.service.*;

import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UpdateApp implements Runnable
    {
    public UpdateApp(Configuration config)
        {
        _config = config;
        UpdateHandlerEventRelay.addListener(_update_listener);
        }

    public void start()
        {
        new Thread(this).start();
        }

    @Override
    public void run()
        {
        _config.update();
        }

    private final Configuration _config;
    private final UpdateHandler _update_listener = new UpdateHandler()
        {
        @Override
        public void failed(Throwable t)
            {
            SimpleLogger.log("Update failed: " + t.getMessage(), t);
            new LaunchApp().start(_config);
            new Thread(() -> UpdateHandlerEventRelay.removeListener(_update_listener)).start();
            }

        @Override
        public void succeeded()
            {
            new LaunchApp().start(_config);
            new Thread(() -> UpdateHandlerEventRelay.removeListener(_update_listener)).start();
            }

        public void init(UpdateContext context) {}
        public void startCheckUpdates() {}
        public void startCheckUpdateFile(FileMetadata file) {}
        public void doneCheckUpdateFile(FileMetadata file, boolean requires) {}
        public void updateCheckUpdatesProgress(float frac) {}
        public void doneCheckUpdates() {}
        public void startDownloads() {}
        public void startDownloadFile(FileMetadata file) {}
        public void updateDownloadFileProgress(FileMetadata file, float frac) {}
        public void updateDownloadProgress(float frac) {}
        public void validatingFile(FileMetadata file, Path tempFile) { }
        public void doneDownloadFile(FileMetadata file, Path tempFile) { }
        public void doneDownloads() { }
        public void stop() { }
        public long version() { return 0; }
        };
    }