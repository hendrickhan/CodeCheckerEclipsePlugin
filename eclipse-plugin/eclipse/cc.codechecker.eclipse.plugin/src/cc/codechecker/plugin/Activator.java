package cc.codechecker.plugin;

import cc.codechecker.plugin.config.CodeCheckerContext;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cc.codechecker.plugin.init.StartupJob;

import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup {

	//Logger
	private static final Logger logger = LogManager.getLogger(Activator.class);
	
    // The plug-in ID
    public static final String PLUGIN_ID = "cc.codechecker.eclipse.plugin"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    private static IPath preferencesPath;

    /**
     * The constructor
     */
    public Activator() {
    	Properties props = new Properties();
    	try {
    		props.load(getClass().getResourceAsStream("log4j.properties"));
    	} catch(Exception e) {}
    	PropertyConfigurator.configure(props);
    	logger.log(Level.DEBUG,"SERVER_GUI_MSG >> Activator Created");
    }

    public static IPath getPreferencesPath() {
        return preferencesPath;
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
    	logger.log(Level.DEBUG,"SERVER_GUI_MSG >> Activator started");
        super.start(context);


        //earlyStartup();
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        CodeCheckerContext.getInstance().stopServers();
        super.stop(context);
    }

    @Override
    public void earlyStartup() {
        plugin = this;

        preferencesPath = getStateLocation();

        Job j = new StartupJob();
        j.setPriority(Job.INTERACTIVE);
        j.schedule();
    }
}
