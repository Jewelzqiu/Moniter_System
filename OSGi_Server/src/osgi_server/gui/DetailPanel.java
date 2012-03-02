package osgi_server.gui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class DetailPanel extends JScrollPane {

	JTable table = new JTable();
	
	public DetailPanel() {
		this.setSize(500, 200);
		
		TableModel model = new DefaultTableModel(
				new String[][] {{"", ""}},
				new String[] {"Camera Number", "Date and Time"});
		table.setModel(model);
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();
		r.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, r);
		table.setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		table.getTableHeader().setFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		this.setViewportView(table);
	}
	
	void updateDetail(String name) {
		int index = name.indexOf('_');
		String number = name.substring(0, index);
		String time = name.substring(index + 1);
		String datetime = time.substring(0, 4) + "." + 
				time.substring(4, 6) + "." +
				time.substring(6, 8) + " " +
				time.substring(8, 10) + ":" +
				time.substring(10, 12) + ":" +
				time.substring(12, 14);
		table.setValueAt(number, 0, 0);
		table.setValueAt(datetime, 0, 1);
	}
	
}
