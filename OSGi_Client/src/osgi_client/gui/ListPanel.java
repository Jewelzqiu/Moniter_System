package osgi_client.gui;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class ListPanel extends JScrollPane {
	
	LeftPanel lefPanel;
	
	public ListPanel(LeftPanel leftPanel) {
		this.setSize(200, 560);
		this.lefPanel = leftPanel;
		this.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
	}
	
	public void updateData(String[] list) {
		String[][] data = new String[list.length][1];
		for (int i = 0; i < list.length; i++) {
			data[i][0] = list[i];
		}
		
		TableModel model = 
				new DefaultTableModel(
						data,
						new String[] {"Images"});
		final JTable table = new JTable();
		table.setModel(model);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
		table.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		table.getTableHeader().setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		this.setViewportView(table);
		
		table.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				String name = (String) table.getValueAt(table.getSelectedRow(), 0);
				if (name != null) {
					if (e.getClickCount() == 1) {
						lefPanel.updateDetail(name);
					}
				}
			}
			
		});
	}
	
}
