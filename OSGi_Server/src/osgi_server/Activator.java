package osgi_server;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import osgi_interface.SendImage;
import sendimage_impl.SendImageImpl;

import ch.ethz.iks.r_osgi.RemoteOSGiService;
import ch.ethz.iks.slp.Advertiser;
import ch.ethz.iks.slp.ServiceURL;

public class Activator implements BundleActivator {

	private static BundleContext context;
	@SuppressWarnings("rawtypes")
	ServiceRegistration registration; 
	Advertiser advertiser; 
	@SuppressWarnings("rawtypes")
	ServiceReference advRef; 
	@SuppressWarnings("rawtypes")
	Hashtable properties = new Hashtable(); 
	String IPAddr = new String(); 

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
		
		String current_path = System.getProperty("user.home");
		String path = current_path + "/Moniter_images";
		File folder = new File(path);
		if ((!folder.exists()) || (!folder.isDirectory())) {
			folder.mkdir();
		}
		
		advRef = context.getServiceReference(Advertiser.class.getName());         
	    properties.put(RemoteOSGiService.R_OSGi_REGISTRATION, Boolean.TRUE);     
	    registration = context.registerService(SendImage.class.getName(), 
	        new SendImageImpl(),  properties); 
	     
	    getIPAddresses();
	    if (IPAddr.equals("")) { 
	        System.out.println("Invalid IP address"); 
	        return; 
	    }
	    
	    if (advRef == null)  { 
	    	System.out.println("advRef is null!"); 
	    } else    { 
	    	System.out.println("Got reference for Advertiser"); 
	    	advertiser = (Advertiser) context.getService(advRef); 
	    	advertiser.register( 
	    			new ServiceURL("service:osgi:r-osgi://"  + IPAddr + ":9278", 50), 
	    			null); 
	    	System.out.println("registered: " + "service:osgi:r-osgi://" + IPAddr + ":9278"); 
	    }
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		
		registration.unregister(); 
		advertiser.deregister(new ServiceURL( 
			    "service:osgi:r-osgi://"  + IPAddr + ":9278", 1000)); 
		System.out.println("unregistered"); 
	}

	// get the IP address of the local machine
	@SuppressWarnings("rawtypes")
	public void getIPAddresses() throws SocketException {
	    Enumeration e = NetworkInterface.getNetworkInterfaces(); 
	    while (e.hasMoreElements()) { 
	    	NetworkInterface ni = (NetworkInterface) e.nextElement(); 
	    	Enumeration ia = ni.getInetAddresses(); 
	    	while (ia.hasMoreElements())  { 
	    		InetAddress addr = (InetAddress) ia.nextElement(); 
	    		if (addr.isLoopbackAddress()) { 
	    			continue; 
	    		} 
	    		if (addr instanceof  Inet4Address) { 
	    			IPAddr = addr.getHostAddress(); 
	    		} 
	    	} 
	    } 	
	} 
	
}
