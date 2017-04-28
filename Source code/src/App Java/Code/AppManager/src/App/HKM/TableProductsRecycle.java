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
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import App.HKM.Extend.MsgBox;
import App.HKM.Extend.TableMouseListener;
import ServerManagerData.ClientToServer;

/**
 * A Swing program that demonstrates how to create a popup menu for a JTable
 * component.
 * 
 * @author www.codejava.net
 *
 */
public class TableProductsRecycle extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JPopupMenu popupMenu;
	private JMenuItem menuItemUnRemove;
	private JMenuItem menuItemUnRemoveAll;
	public String[] columnNames;
	public static String text;
	public Object[][] rowData;

	public TableProductsRecycle(String[] columnNames, Object[][] rowData, String name) {
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
		table.getColumn("Current Price").setCellRenderer(tcr);
		table.getColumn("Old Price").setCellRenderer(tcr);
		table.getColumn("Date Insert").setCellRenderer(tcr);
		table.getColumn("Status").setCellRenderer(tcr);
		table.getColumn("STT").setMaxWidth(50);
		table.getColumn("Title").setMinWidth(300);
		table.getColumn("Date Insert").setMinWidth(150);
		table.setRowSelectionAllowed(true);
		// constructs the popup menu
		popupMenu = new JPopupMenu();
		menuItemUnRemove = new JMenuItem("Restore");
		menuItemUnRemoveAll = new JMenuItem("Restore All");

		menuItemUnRemove.addActionListener(this);
		menuItemUnRemoveAll.addActionListener(this);

		popupMenu.add(menuItemUnRemove);
		popupMenu.add(menuItemUnRemoveAll);

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
		setSize(1000, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@SuppressWarnings("serial")
	public class MyTable extends JTable {
		private Color evenBackColor = new Color(95, 158, 160);
		private Color evenTextColor = Color.white;
		private Color oddBackColor = new Color(46, 139, 87);
		private Color oddTextColor = Color.white;
		private Color rolloverBackColor = new Color(128, 128, 128);
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
				c.setForeground(Color.BLUE);
			}
			if (column == 4) {
				c.setForeground(new Color(55, 65, 66));
			}
			if (column == 5) {
				c.setForeground(new Color(128, 0, 0));
			}
			if (column == 0) {
				c.setForeground(new Color(0, 0, 0));
			}
			if (column == 6) {
				c.setForeground(new Color(255, 215, 0));
			}
			if (column == 2) {
				c.setForeground(new Color(139, 69, 19));
			}
			if (column == 1) {
				c.setForeground(new Color(25, 25, 112));
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
		if (menu == menuItemUnRemove) {
			try {
				UnRemoveCurrentRow();
			} catch (InterruptedException | URISyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (menu == menuItemUnRemoveAll) {
			try {
				UnRemoveAllRows();
			} catch (InterruptedException | URISyntaxException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void UnRemoveCurrentRow() throws InterruptedException, URISyntaxException, IOException {
		MsgBox message = new MsgBox(null, "Do you want restore?", true);
		int selectedRow = table.getSelectedRow();
		String title = (String) tableModel.getValueAt(selectedRow, 1);
		if (message.isOk) {
			tableModel.removeRow(selectedRow);
			ClientToServer CtS = new ClientToServer("update", "admin", "TableWebInfo", "__Title__", title, "__Delete__",
					"false", 0);
			CtS.getValueRequests();
		}
	}

	private void UnRemoveAllRows() throws InterruptedException, URISyntaxException, IOException {
		MsgBox message = new MsgBox(null, "Do you want restore all?", true);
		if (message.isOk) {
			int rowCount = tableModel.getRowCount();
			for (int i = 0; i < rowCount; i++) {
				tableModel.removeRow(0);
			}
			ClientToServer CtS = new ClientToServer("update", "admin", "TableWebInfo", "null", "null", "__Delete__",
					"false", 0);
			CtS.getValueRequests();

		}
	}

}