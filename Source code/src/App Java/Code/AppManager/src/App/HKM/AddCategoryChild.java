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
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import App.HKM.Data.CategoryParents;
import App.HKM.Extend.TableMouseListener;
import ServerManagerData.ClientToServer;
import ServerManagerData.ServerToClient;

/**
 * A Swing program that demonstrates how to create a popup menu for a JTable
 * component.
 * 
 * @author www.codejava.net
 *
 */
public class AddCategoryChild extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	public String[] columnNames;
	public static String text;
	public Object[][] rowData;
	public static int edit = 0;

	public AddCategoryChild(String[] columnNames, Object[][] rowData, String name)
			throws URISyntaxException, IOException {
		super(name);
		this.columnNames = columnNames;
		this.rowData = rowData;
		// sample table data

		// constructs the table with sample data
		tableModel = new DefaultTableModel(this.rowData, this.columnNames);
		table = new MyTable(tableModel);
		table.getSelectionModel().setSelectionInterval(2, 2);
		TableColumn tableCol = table.getColumnModel().getColumn(2);
		JComboBox comboBox = new JComboBox();
		ArrayList<CategoryParents> category_parents = new ArrayList<CategoryParents>();
		ClientToServer CtS = new ClientToServer("get_all_record", "admin", "CategoryParents", "null", "null",
				"__CategoryParentName__", 1);
		String data = CtS.getValueRequests();
		category_parents = ServerToClient.GetListArrayCategoryParents(data);
		for (CategoryParents categoryParent : category_parents) {
			comboBox.addItem(categoryParent.get__CategoryParentName__());
		}
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edit++;
				if (edit == 2) {
					try {
						int selectedRow = table.getSelectedRow();
						String child_name = (String) tableModel.getValueAt(selectedRow, 1);
						ClientToServer CtS = new ClientToServer("update_category_child", "admin", "TableWebInfo",
								"__Category__.__CategoryChildName__", child_name, "__CategoryParentName__",
								String.valueOf(comboBox.getSelectedItem()), 0);
						CtS.getValueRequests();
						System.out.println(String.valueOf(comboBox.getSelectedItem()));
					} catch (URISyntaxException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					edit = 0;
				}
			}
		});
		tableCol.setCellEditor(new DefaultCellEditor(comboBox));
		tableCol.setPreferredWidth(200);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		table.getColumn("STT").setCellRenderer(tcr);
		table.getColumn("Parent").setCellRenderer(tcr);
		table.getColumn("STT").setMaxWidth(50);
		table.setRowSelectionAllowed(true);
		// constructs the popup menu
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
		setSize(600, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@SuppressWarnings("serial")
	public class MyTable extends JTable {
		private Color evenBackColor = new Color(95, 158, 160);
		private Color evenTextColor = Color.white;
		private Color oddBackColor = new Color(0, 128, 128);
		private Color oddTextColor = Color.white;
		private Color rolloverBackColor = new Color(154, 205, 50);
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
				c.setForeground(Color.BLUE);
			}
			if (column == 1) {
				c.setForeground(Color.WHITE);
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
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}