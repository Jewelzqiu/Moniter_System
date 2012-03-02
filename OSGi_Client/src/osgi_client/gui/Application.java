package osgi_client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JFrame;
import osgi_client.Activator;

@SuppressWarnings("serial")
public class Application extends JFrame {
	
	Activator activator;
	LeftPanel leftPanel = new LeftPanel(this);
	RightPanel rightPanel = new RightPanel();
	
	public void init(Activator activator) {
		this.activator = activator;
		this.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		this.setTitle("Moniter System Client");
		this.setSize(700, 485);
		this.setLayout(new FlowLayout());
		this.add(leftPanel);
		leftPanel.setPreferredSize(new Dimension(170, 438));
		this.add(rightPanel);
		rightPanel.setPreferredSize(new Dimension(500, 438));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void updateData(String[] list) {
		leftPanel.updateData(list);
	}
	
	void updateDetail(String name) {
		String filename = activator.getImageName(name);
		rightPanel.updateDetail(name, filename);
	}
	
	void refresh() {
		activator.updateData();
	}
}
