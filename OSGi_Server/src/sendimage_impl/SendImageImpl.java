package sendimage_impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import osgi_interface.SendImage;

public class SendImageImpl implements SendImage {

	public void sendImage(byte[] bytes, String filename) {
		
		System.out.println("Got " + filename);
		String current_path = System.getProperty("user.home");
		String path = current_path + "/Moniter_images";
		File folder = new File(path);
		if ((!folder.exists()) || (!folder.isDirectory())) {
			folder.mkdir();
		}
		File image = getFileFromBytes(bytes, path + "/" + filename);
		System.out.println("Target: " + image.getAbsolutePath());
	}
	
	public static File getFileFromBytes(byte[] b, String outputFile) {
		File ret = null;
		BufferedOutputStream stream = null;
		try {
			ret = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(ret);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

}
