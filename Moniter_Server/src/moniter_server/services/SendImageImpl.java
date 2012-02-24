package moniter_server.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import moniter_interface.SendImage;
import moniter_server.Activator;

public class SendImageImpl implements SendImage {

	public void sendImage(File file) {
		// TODO save the file
		String filename = file.getName();
		File image = new File(Activator.path + "/" + filename);
		try {
			image.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			PrintWriter writer = new PrintWriter(image);
			String line;
			while ((line = reader.readLine()) != null) {
				writer.append(line);
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
