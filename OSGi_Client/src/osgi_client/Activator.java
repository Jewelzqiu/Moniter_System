package osgi_client;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

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

import osgi_client.gui.Application;
import osgi_client.gui.Dialog;
import osgi_interface.Services;
import osgi_interface.Util;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	Application application = new Application();
	Dialog dialog;
	
	@SuppressWarnings("rawtypes")
	Vector ServiceLocation = new Vector(); 
	RemoteOSGiService remote; 
	RemoteServiceReference[] refs;
	
	@SuppressWarnings("rawtypes")
	ServiceReference sref;
	Services Service;
	
	@SuppressWarnings("rawtypes")
	ServiceReference locRef;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@SuppressWarnings("unchecked")
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		dialog = new Dialog("Initializing...");
		application.init(this);
		
		dialog.setText("Connecting to  the server...");
		locRef = context.getServiceReference(Locator.class.getName());  
		if (locRef == null)  { 
			System.out.println("locRef is null!");
			dialog.setText("Cannot find the server");
			return;			
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
		
		dialog.setText("Requesting services from the server...");
		sref = context.getServiceReference(RemoteOSGiService.class.getName());  
		if (sref == null)  {
			dialog.setText("Cannot get services.");
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
		       
			Service = (Services) remote.getRemoteService(refs[0]);
			if (Service != null) {
				System.out.println("Found service");
				break;
			}
		}
		
		dialog.setText("Requesting data...");
		updateData();
		application.setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	public void updateData() {
		if (Service == null) {
			dialog.setText("Get Service error");
			return;
		}
		String namelist = Service.getList();
		StringTokenizer tokenizer = new StringTokenizer(namelist);
		String[] list = new String[tokenizer.countTokens()];
		int temp = 0;
		while (tokenizer.hasMoreTokens()) {
			list[temp++] = tokenizer.nextToken();
		}
		application.updateData(list);
		dialog.dispose();
	}
	
	public String getImageName(String name) {
		if (Service == null) {
			return null;
		}
		
		File file = new File("temp");
		if ((!file.exists()) && (!file.isDirectory())) {
			file.mkdir();
		}
		byte[] bytes = Service.getImage(name);
		
		File image = Util.getFileFromBytes(bytes, "temp/" + name);
		return image.getAbsolutePath();
	}

}
