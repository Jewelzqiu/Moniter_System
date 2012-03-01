package osgi_client.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LeftPanel extends JPanel {
	
	Application application;
	JButton RefreshButton = new JButton("Refresh");
	ListPanel List = new ListPanel(this);
	
	public LeftPanel(final Application application) {
		this.application = application;
		this.setSize(170, 450);
		this.add(RefreshButton);
		RefreshButton.setPreferredSize(new Dimension(80, 30));
		RefreshButton.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		RefreshButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				application.refresh();
			}
		});
		this.add(List);
		List.setPreferredSize(new Dimension(170, 398));
	}
	
	public void updateData(String[] list) {
		List.updateData(list);
	}
	
	void updateDetail(String name) {
		application.updateDetail(name);
	}
	
}
