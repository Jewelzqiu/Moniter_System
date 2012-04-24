package osgi_server.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

import osgi_server.gui.LeftPanel;
import osgi_server.gui.RightPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	LeftPanel leftPanel = new LeftPanel(this);
	RightPanel rightPanel = new RightPanel();

	String path = System.getProperty("user.home") + "/Moniter_images";
	String[] ImageList;

	public void init() throws IOException {
		this.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		this.setTitle("Moniter System Server");
		this.setSize(840, 565);
		this.setLayout(new FlowLayout());
		this.add(leftPanel);
		leftPanel.setPreferredSize(new Dimension(170, 518));
		this.add(rightPanel);
		rightPanel.setPreferredSize(new Dimension(640, 518));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		refresh();
		this.setVisible(true);

		final ServerSocket server = new ServerSocket(22222);
		new Thread() {

			public void run() {
				while (true) {
					refresh();
					try {
						Socket socket = server.accept();
						System.out.println("socket accepted");
						BufferedReader in = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						PrintWriter out = new PrintWriter(
								socket.getOutputStream());
						String request = in.readLine();
						System.out.println(request);
						if (request != null) {
							if (request.equals("list")) {
								for (String name : ImageList) {
									out.println(name);
								}
								out.flush();
							} else {
								FileInputStream infile = new FileInputStream(
										path + "/" + request);
								DataOutputStream output = new DataOutputStream(
										socket.getOutputStream());

								int size = 8192;
								byte[] buf = new byte[size];

								while (true) {
									int read = 0;
									if (infile != null) {
										read = infile.read(buf);
									}
									if (read == -1) {
										break;
									}
									output.write(buf);
								}
								output.flush();
								infile.close();
								output.close();
							}
							in.close();
							out.close();
							socket.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();
	}

	void updateDetail(String name) {
		File image = new File(path + "/" + name);
		if (image.exists() && image.isFile()) {
			rightPanel.updateDetail(image);
		}
	}

	public void refresh() {
		File folder = new File(path);
		if ((!folder.exists()) || (!folder.isDirectory())) {
			folder.mkdir();
			return;
		}
		String[] filelist = folder.list();
		String[] list = new String[filelist.length];
		int temp = 0;
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].substring(filelist[i].length() - 3).toLowerCase()
					.equals("jpg")
					|| filelist[i].substring(filelist[i].length() - 3)
							.toLowerCase().equals("png")) {
				list[temp++] = filelist[i];
			}
		}
		ImageList = new String[temp];
		for (int i = 0; i < temp; i++) {
			ImageList[i] = list[i];
		}
		leftPanel.updateData(ImageList);
	}
}
