package App.HKM;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.bson.Document;

import App.HKM.Extend.MsgBox;
import App.HKM.Extend.TableMouseListener;
import App.HKM.Extend.TextBubbleBorder;
import ServerManagerData.ClientToServer;
@SuppressWarnings("serial")
public class ListError extends JFrame implements ActionListener {
	private JTable table;
	private DefaultTableModel tableModel;
	private JPopupMenu popupMenu;
	private JMenuItem menuItemsuccses;
	public String[] columnNames;
	public static String text;
	public Object[][] rowData;

	public ListError(String[] columnNames, Object[][] rowData, String name) {
		super(name);
		this.columnNames = columnNames;
		this.rowData = rowData;
		// sample table data
		// constructs the table with sample data
		tableModel = new DefaultTableModel(this.rowData, this.columnNames);
		table = new MyTable(tableModel);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		table.getColumn("STT").setCellRenderer(tcr);
		table.getColumn("Date").setCellRenderer(tcr);
		table.getColumn("STT").setMinWidth(50);
		table.getColumn("Error").setMinWidth(300);
		table.getColumn("HomePage").setMinWidth(250);
		table.getColumn("Date").setMinWidth(200);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		// constructs the popup menu
		popupMenu = new JPopupMenu();
		menuItemsuccses = new JMenuItem("Completed");
		menuItemsuccses.addActionListener(this);
		popupMenu.add(menuItemsuccses);
		// sets the popup menu for the table
		table.setComponentPopupMenu(popupMenu);

		table.addMouseListener(new TableMouseListener(table));
		table.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
	                Point p = e.getPoint();
	                int row = table.rowAtPoint(p);
	                int col = table.columnAtPoint(p);
	                table.setToolTipText(String.valueOf(table.getModel().getValueAt(row, col)));
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
		// adds the table to the frame
		add(new JScrollPane(table));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 300);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public class MyTable extends JTable {
		private Color evenBackColor = new Color(72,61,139);
		private Color evenTextColor = Color.white;
		private Color oddBackColor = new Color(106,90,205);
		private Color oddTextColor = Color.white;
		private Color rolloverBackColor = new Color(186,85,211);
		private Color rolloverTextColor = Color.white;

		private int rolloverRowIndex = -1;

		public MyTable(TableModel model) {
			super(model);
			RolloverListener listener = new RolloverListener();
			addMouseMotionListener(listener);
			addMouseListener(listener);
		}

		public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
			Component c = super.prepareRenderer(renderer, row, column);
			if (isRowSelected(row)) {
				c.setForeground(getSelectionForeground());
				c.setBackground(getSelectionBackground());
			} else if (row == rolloverRowIndex) {
				c.setForeground(rolloverTextColor);
				c.setBackground(rolloverBackColor);
			} else if (row % 2 == 0) {
				c.setForeground(evenTextColor);
				c.setBackground(evenBackColor);
			} else {
				c.setForeground(oddTextColor);
				c.setBackground(oddBackColor);
			}
			if (column == 3) {
				c.setForeground(Color.CYAN);
			}
			if (column == 0) {
				c.setForeground(new Color(0, 0, 0));
			}
		
			if (column == 2) {
				c.setForeground(new Color(139, 69, 19));
			}
			if (column == 1) {
				c.setForeground(new Color(250,235,215));
			}
			return c;
		}

		private class RolloverListener extends MouseInputAdapter {

			public void mouseExited(MouseEvent e) {
				rolloverRowIndex = -1;
				repaint();
			}

			public void mouseMoved(MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				if (row != rolloverRowIndex) {
					rolloverRowIndex = row;
					repaint();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JMenuItem menu = (JMenuItem) event.getSource();
		if (menu == menuItemsuccses) {
			try {
				succses();
			} catch (UnknownHostException | MessagingException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}

	
	private void succses() throws AddressException, MessagingException, InterruptedException, URISyntaxException, IOException {
		MsgBox message = new MsgBox(null, "Do you want delete all?", true);
		if (message.isOk) {
			int selectedRow = table.getSelectedRow();
			String content = (String) tableModel.getValueAt(selectedRow, 1);
			ClientToServer CtS = new ClientToServer("update","admin","Error", "__Content__",content,"__Status__","true",0);
			   CtS.getValueRequests();
			   tableModel.removeRow(selectedRow);
		}
	}


}