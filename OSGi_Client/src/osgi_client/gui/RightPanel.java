package osgi_client.gui;

import java.awt.Dimension;
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
	
	void updateDetail(String name, String localname) {
		detailPanel.updateDetail(name);
		imagePanel.updateDetail(localname);
	}
	
}
