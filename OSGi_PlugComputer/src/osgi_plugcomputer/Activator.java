package osgi_plugcomputer;

import java.io.File;
import java.util.Vector;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import osgi_interface.Services;
import osgi_interface.Util;

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
	Services sendService;
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
		if (locRef == null)  { 
			System.out.println("locRef is null!"); 
		} else { 
			System.out.println("Got reference for Locator"); 
			Locator locator = (Locator) context.getService(locRef); 
			ServiceLocationEnumeration  slenum  = locator.findServices( 
					new ServiceType("service:osgi"),  null, null); 
			System.out.println("RESULT:"); 
			while (slenum.hasMoreElements())  { 
				ServiceURL service  = (ServiceURL) slenum.nextElement(); 
				String url = service.toString(); 
				String address  = url.substring(13); 
				System.out.println("address:  " + address); 
				ServiceLocation.add(address); 
			} 
		} 
		
		sref = context.getServiceReference(RemoteOSGiService.class.getName());  
		if (sref == null)  { 
			throw new BundleException("OSGi remote service not found"); 
		}
		
		remote = (RemoteOSGiService) context.getService(sref);     
		for (int i = 0; i < ServiceLocation.size(); i++) { 
			refs = remote.getRemoteServiceReferences( 
					new URI(ServiceLocation.elementAt(i).toString()), 
					Services.class.getName(), null);
			if (refs == null)  { 
				System.out.println("Service not found at "  
						+ ServiceLocation.elementAt(i).toString()); 
				continue; 
			} 
		       
			sendService = (Services) remote.getRemoteService(refs[0]);
			if (sendService != null) {
				System.out.println("Found service");
				break;
			}
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
			System.out.println("Thread started!");
			while (true) {				
				File folder = new File("/home/ftp/images");
				if (!folder.isDirectory()) {
					continue;
				}
				
				File[] filelist = folder.listFiles();
				for (int i = 0; i < filelist.length; i++) {
					File image = filelist[i];
					if (image.isFile() && image.canRead()) {						
						if (sendService != null) {
							sendService.sendImage(Util.getBytesFromFile(image), image.getName());
						}
						System.out.println(image.getName());
						image.delete();
					}
				}
			}
			
		}
		
	}

}
