package plugcomputer;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	class CheckImage implements Runnable {
		
		public void run() {
			
			while (true) {				
				File folder = new File("/home/ftp/images");
				if (!folder.isDirectory()) {
					continue;
				}
				
				File[] filelist = folder.listFiles();
				for (int i = 0; i < filelist.length; i++) {
					File image = filelist[i];
					if (image.isFile() && image.canRead()) {
						
						// proceed
						
						image.delete();
					}
				}
			}
			
		}
		
	}
	
}
