package net.christophermerrill.update4j.multichannel.core;

import net.christophermerrill.update4j.multichannel.*;
import org.update4j.*;
import org.update4j.service.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class AppLauncher implements Launcher
    {
    @Override
    public void run(LaunchContext context)
        {
        SimpleLogger.log("IdeLauncher.run entered");
        for (LaunchListener listener : LISTENERS)
            listener.starting();

        SimpleLogger.log("Files in launch context: ");
        for (FileMetadata file : context.getConfiguration().getFiles())
            {
            String whichpath = "?";
            if (file.isClasspath())
                whichpath = "classpath";
            else if (file.isModulepath())
                whichpath = "modulepath";
            SimpleLogger.log(String.format("  %s on %s", file.getPath().toString(), whichpath));
            }

        AtomicReference<Throwable> thrown = new AtomicReference<>(null);

        BootstrapSettings settings = UpdateLaunchBootstrap.INSTANCE.getSettings();
        Thread app_thread = new Thread(() ->
            {
            String classname = settings.getAppClassname();
            try
                {
                @SuppressWarnings("rawtypes")
                Class app_class = Class.forName(classname, true, context.getClassLoader());
                Method main = app_class.getMethod("main", String[].class);

                SimpleLogger.log("Invoking static main() on " + app_class.getSimpleName());
                main.invoke(app_class, (Object) settings.getAppParameters());
                }
            catch (NoClassDefFoundError e)
                {
                SimpleLogger.log("Unable to find class " + classname, e);
                thrown.set(e);
                }
            catch (ClassNotFoundException e)
                {
                Throwable cause = e.getCause() != null ? e.getCause() : e;
                SimpleLogger.log("Unable to load class " + classname, cause);
                thrown.set(e);
                }
            catch (NoSuchMethodException e)
                {
                SimpleLogger.log("Unable to find main() on class " + classname, e);
                thrown.set(e);
                }
            catch (IllegalAccessException e)
                {
                SimpleLogger.log("Unable to access main() on class " + classname, e);
                thrown.set(e);
                }
            catch (InvocationTargetException e)
                {
                SimpleLogger.log(String.format("Exeception thrown from %s.main(): %s", classname, e));
                e.printStackTrace();
                thrown.set(e);
                }
            catch (Exception e)
                {
                SimpleLogger.log(String.format("Unexpected exeception thrown from %s.main(): %s", classname, e));
                e.printStackTrace();
                thrown.set(e);
                }
            });

        app_thread.start();
        while (app_thread.isAlive())
            {
            try
                {
                app_thread.join();
                }
            catch (InterruptedException e)
                {
                // ok, that is fine.
                }
            }

        for (LaunchListener listener : LISTENERS)
            listener.completed(thrown.get());
        }

    @Override
    public long version()
        {
        return 1;
        }


    public interface LaunchListener
        {
        void starting();
        void completed(Throwable exception);
        }

    public static Set<LaunchListener> LISTENERS = new HashSet<>();
    }