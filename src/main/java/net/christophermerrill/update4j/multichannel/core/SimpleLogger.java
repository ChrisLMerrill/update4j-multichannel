package net.christophermerrill.update4j.multichannel.core;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleLogger
    {
    public static void log(String message)
        {
        log(message, null);
        }

    public static void log(String message, Throwable t)
        {
        PrintStream printer = getPrinter();
        if (message != null)
            printer.println(message);
        if (t != null)
            t.printStackTrace(printer);
        printer.flush();
        }

    public static PrintStream getPrinter()
        {
        if (PRINTER == null)
            {
            OutputStream outstream;
            try
                {
                LOG_FILE = new File(LOGNAME);
                OUTSTREAM = new FileOutputStream(LOG_FILE);
                outstream = OUTSTREAM;
                }
            catch (Throwable e)
                {
                outstream = System.out;
                }
            PRINTER = new PrintStream(outstream);
            PRINTER.println("Log started at " + SimpleDateFormat.getDateTimeInstance().format(new Date()));
            PRINTER.flush();

            Runtime.getRuntime().addShutdownHook(new Thread(() ->
                {
                try
                    {
                    PRINTER.println("Log closing at " + SimpleDateFormat.getDateTimeInstance().format(new Date()));
                    PRINTER.flush();
                    PRINTER.close();
                    if (OUTSTREAM != null)
                        {
                        OUTSTREAM.flush();
                        OUTSTREAM.close();
                        }
                    }
                catch (IOException e)
                    { /* no-op */ }
                }, "SimpleLogger shutdown thread"));
            }
        return PRINTER;
        }

    private static PrintStream PRINTER;
    private static OutputStream OUTSTREAM;
    public static String LOGNAME = "log.txt";
    public static File LOG_FILE;
    }
