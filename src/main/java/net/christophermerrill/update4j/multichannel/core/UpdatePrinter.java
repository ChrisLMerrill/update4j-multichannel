package net.christophermerrill.update4j.multichannel.core;

import org.update4j.*;
import org.update4j.service.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UpdatePrinter implements UpdateHandler
    {
    @Override
    public void init(UpdateContext context)
        {
        this.context = context;
        }

    @Override
    public void startCheckUpdates()
        {
        PRINTER.println("Starting update checks");
        PRINTER.println("Base path is " + context.getConfiguration().getBasePath());
        }

    @Override
    public void startCheckUpdateFile(FileMetadata file)
        {
        PRINTER.println("  starting update check for file " +  file.getPath());
        }

    @Override
    public void doneCheckUpdateFile(FileMetadata file, boolean requires)
        {
        PRINTER.print("  completed update check for file " + file.getPath());
        if (requires)
            PRINTER.println(":  UPDATE");
        else
            PRINTER.println(":  SYNCHRONIZED");
        }

    @Override
    public void updateCheckUpdatesProgress(float frac)
        {
        PRINTER.println(String.format("    update progress: %d%%", (int)(frac * 100)));
        }

    @Override
    public void doneCheckUpdates()
        {
        PRINTER.println("Completed update checks");
        }

    @Override
    public void startDownloads()
        {
        PRINTER.println("Starting downloads");
        }

    @Override
    public void startDownloadFile(FileMetadata file)
        {
        PRINTER.println("  starting download of file " + file.getPath());
        _download_progress = -1;
        }

    @Override
    public void updateDownloadFileProgress(FileMetadata file, float frac)
        {
        int percent = (int) (frac * 100f);
        if (percent != _download_progress)
            {
            PRINTER.println(String.format("    file progress: %d%%", percent));
            _download_progress = percent;
            }
        }

    @Override
    public void updateDownloadProgress(float frac)
        {
        // no need to print overall progress
        }

    @Override
    public void validatingFile(FileMetadata file, Path tempFile)
        {
        PRINTER.println("    validating file " + file.getPath());
        }

    @Override
    public void doneDownloadFile(FileMetadata file, Path tempFile)
        {
        PRINTER.println("  completed download of file " + file.getPath());
        }

    @Override
    public void doneDownloads()
        {
        PRINTER.println("Completed downloads");
        }

    @Override
    public void failed(Throwable t)
        {
        PRINTER.println("FAILURE reported:");
        t.printStackTrace(PRINTER);
        }

    @Override
    public void succeeded()
        {
        PRINTER.println("SUCCESS!");
        }

    @Override
    public void stop()
        {
        PRINTER.println("Update stopped.");
        }

    @Override
    public long version()
        {
        return 0;
        }

    public static PrintStream PRINTER = System.out;
    private UpdateContext context;
    private int _download_progress;
    }
