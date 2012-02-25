package osgi_client.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {
	
	DetailPanel detailPanel = new DetailPanel();
	ImagePanel imagePanel = new ImagePanel();
	
	public RightPanel() {
		this.setSize(400, 600);
		this.add(detailPanel);
		detailPanel.setPreferredSize(new Dimension(400, 200));
		this.add(imagePanel);
		imagePanel.setPreferredSize(new Dimension(400, 400));
	}
	
}
