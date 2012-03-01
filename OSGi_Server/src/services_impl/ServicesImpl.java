package services_impl;

import java.io.File;
import osgi_interface.Services;
import osgi_interface.Util;

public class ServicesImpl implements Services {
	
	String path = System.getProperty("user.home") + "/Moniter_images";

	public void sendImage(byte[] bytes, String filename) {
		
		System.out.println("Got " + filename);
		File folder = new File(path);
		if ((!folder.exists()) || (!folder.isDirectory())) {
			folder.mkdir();
		}
		File image = Util.getFileFromBytes(bytes, path + "/" + filename);
		System.out.println("Target: " + image.getAbsolutePath());
	}

	public byte[] getImage(String filename) {
		File file = new File(path + "/" + filename);
		if (file.exists() && file.isFile()) {
			return Util.getBytesFromFile(file);
		}
		return null;
	}

	public String getList() {		
		File folder = new File(path);
		if (folder.exists() && folder.isDirectory()) {
			String list = "";
			String[] names = folder.list();
			for (int i = 0; i < names.length - 1; i++) {
				list += names[i] + " ";
			}
			list += names[names.length - 1];
			return list;
		}
		return null;
	}
	
}
