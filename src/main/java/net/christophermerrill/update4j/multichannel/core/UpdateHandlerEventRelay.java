package net.christophermerrill.update4j.multichannel.core;

import org.update4j.*;
import org.update4j.service.*;

import java.nio.file.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UpdateHandlerEventRelay implements UpdateHandler
    {
    @Override
    public void init(UpdateContext context) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.init(context);
        }

    @Override
    public void startCheckUpdates() throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.startCheckUpdates();
        }

    @Override
    public void startCheckUpdateFile(FileMetadata file) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.startCheckUpdateFile(file);
        }

    @Override
    public void doneCheckUpdateFile(FileMetadata file, boolean requires) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.doneCheckUpdateFile(file, requires);
        }

    @Override
    public void updateCheckUpdatesProgress(float frac) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.updateCheckUpdatesProgress(frac);
        }

    @Override
    public void doneCheckUpdates() throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.doneCheckUpdates();
        }

    @Override
    public void startDownloads() throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.startDownloads();
        }

    @Override
    public void startDownloadFile(FileMetadata file) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.startDownloadFile(file);
        }

    @Override
    public void updateDownloadFileProgress(FileMetadata file, float frac) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.updateDownloadFileProgress(file, frac);
        }

    @Override
    public void updateDownloadProgress(float frac) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.updateDownloadProgress(frac);
        }

    @Override
    public void validatingFile(FileMetadata file, Path tempFile) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.validatingFile(file, tempFile);
        }

    @Override
    public void doneDownloadFile(FileMetadata file, Path tempFile) throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.doneDownloadFile(file, tempFile);
        }

    @Override
    public void doneDownloads() throws Throwable
        {
        for (UpdateHandler listener : LISTENERS)
            listener.doneDownloads();
        }

    @Override
    public void failed(Throwable t)
        {
        for (UpdateHandler listener : LISTENERS)
            listener.failed(t);
        }

    @Override
    public void succeeded()
        {
        for (UpdateHandler listener : LISTENERS)
            listener.succeeded();
        }

    @Override
    public void stop()
        {
        for (UpdateHandler listener : LISTENERS)
            listener.stop();
        }

    @Override
    public long version()
        {
        return 0;
        }

    public static void addListener(UpdateHandler listener)
        {
        LISTENERS.add(listener);
        }

    static void removeListener(UpdateHandler listener)
        {
        LISTENERS.remove(listener);
        }

    private static final Set<UpdateHandler> LISTENERS = new HashSet<>();
    }
