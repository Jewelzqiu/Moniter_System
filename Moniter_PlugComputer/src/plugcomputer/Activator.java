package plugcomputer;

import java.io.File;
import java.util.Vector;

import moniter_interface.SendImage;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.r_osgi.RemoteServiceReference;
import ch.ethz.iks.r_osgi.URI;
import ch.ethz.iks.slp.Locator;
import ch.ethz.iks.slp.ServiceLocationEnumeration;
import ch.ethz.iks.slp.ServiceType;
import ch.ethz.iks.slp.ServiceURL;

public class Activator implements BundleActivator {

	private static BundleContext context;
	Vector ServiceLocation = new Vector();
	RemoteOSGiService remote;
	RemoteServiceReference[] refs;
	ServiceReference sref;
	SendImage SendImageService;
	ServiceReference locRef;
	Thread thread;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		locRef = context.getServiceReference(Locator.class.getName());
		if (locRef == null) {
			System.out.println("Locator reference is null");
		} else {
			Locator locator = (Locator) context.getService(locRef);
			ServiceLocationEnumeration slenum = locator.findServices(
					new ServiceType("service:osgi"), null, null);
			System.out.println("Found services:");
			while (slenum.hasMoreElements()) {
				ServiceURL service = (ServiceURL) slenum.nextElement();
				String url = service.toString();
				String address = url.substring(13);
				System.out.println("address: " + address);
				ServiceLocation.add(address);
			}
		}
		
		sref = context.getServiceReference(RemoteOSGiService.class.getName());
		if (sref == null) {
			throw new BundleException("Remote OSGi service not found!");
		}
		
		remote = (RemoteOSGiService) context.getService(sref);
		for (int i = 0; i < ServiceLocation.size(); i++) {
			refs = remote.getRemoteServiceReferences(
					new URI(ServiceLocation.elementAt(i).toString()),
					SendImage.class.getName(),
					null);
			
			if (refs == null) {
				System.out.println("Service not found at "
						+ ServiceLocation.elementAt(i).toString());
				continue;
			}
			
			SendImageService = (SendImage) remote.getRemoteService(refs[0]);
		}
		
		thread = new Thread(new CheckImage());
		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		thread.interrupt();
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
					System.out.println(image.getName());
					if (image.isFile() && image.canRead()) {						
						if (SendImageService != null) {
							SendImageService.sendImage(image);
						}						
						image.delete();
					}
				}
			}
			
		}
		
	}
	
}
