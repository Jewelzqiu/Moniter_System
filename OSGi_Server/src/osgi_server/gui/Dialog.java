package osgi_server.gui;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Dialog extends JDialog {

	JLabel label = new JLabel();
	
	public Dialog(String text) {
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setSize(250, 100);
		label.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setText(text);
		this.setTitle("Server");
		this.add(label);
		this.setVisible(true);
	}
	
	public void setText(String text) {
		this.label.setText(text);
	}
	
}
