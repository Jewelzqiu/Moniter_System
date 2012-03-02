package osgi_server.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.JFrame;

import osgi_server.gui.LeftPanel;
import osgi_server.gui.RightPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	LeftPanel leftPanel = new LeftPanel(this);
	RightPanel rightPanel = new RightPanel();
	
	String path = System.getProperty("user.home") + "/Moniter_images";
	
	public void init() {
		this.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		this.setTitle("Moniter System Server");
		this.setSize(700, 485);
		this.setLayout(new FlowLayout());
		this.add(leftPanel);
		leftPanel.setPreferredSize(new Dimension(170, 438));
		this.add(rightPanel);
		rightPanel.setPreferredSize(new Dimension(500, 438));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		refresh();
		this.setVisible(true);
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
			if (filelist[i].substring(filelist[i].length() - 3)
							.toLowerCase().equals("jpg") ||
					filelist[i].substring(filelist[i].length() - 3)
							.toLowerCase().equals("png")) {
				list[temp++] = filelist[i];
			}
		}
		String[] newlist = new String[temp];
		for (int i = 0; i < temp; i++) {
			newlist[i] = list[i];
		}
		leftPanel.updateData(newlist);
	}
}
