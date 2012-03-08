package osgi_server.gui;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {
	
	DetailPanel detailPanel = new DetailPanel();
	ImagePanel imagePanel = new ImagePanel();
	
	public RightPanel() {
		this.setSize(500, 450);
		this.add(detailPanel);
		detailPanel.setPreferredSize(new Dimension(640, 36));
		this.add(imagePanel);
		imagePanel.setPreferredSize(new Dimension(640, 480));
	}
	
	void updateDetail(File image) {
		detailPanel.updateDetail(image.getName());
		imagePanel.updateDetail(image.getAbsolutePath());
	}
	
}
