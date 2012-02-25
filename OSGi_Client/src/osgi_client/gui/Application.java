package osgi_client.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import org.osgi.framework.BundleContext;

@SuppressWarnings("serial")
public class Application extends JFrame {
	
	BundleContext context;
	ListPanel listPanel = new ListPanel();
	RightPanel rightPanel = new RightPanel();
	
	public void init(BundleContext context) {
		this.context = context;
		
		this.setTitle("Moniter System");
		this.setSize(630, 645);
		this.setLayout(new FlowLayout());
		this.add(listPanel);
		listPanel.setPreferredSize(new Dimension(200, 600));
		this.add(rightPanel);
		rightPanel.setPreferredSize(new Dimension(400, 600));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
}
