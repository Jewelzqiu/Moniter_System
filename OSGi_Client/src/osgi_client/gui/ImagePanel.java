package osgi_client.gui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	
	JLabel image = new JLabel();
	
	public ImagePanel() {
		this.setSize(500, 400);
		this.add(image);
	}
	
	void updateDetail(String name) {
		image.setIcon(new ImageIcon(name));
	}
	
}
