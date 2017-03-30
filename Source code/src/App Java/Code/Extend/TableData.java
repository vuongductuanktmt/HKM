package App.HKM.Extend;

/*
 * Copyright 2002 and later by MH Software-Entwicklung. All rights reserved.
 * Use is subject to license terms.
 */

import java.awt.*;
import java.awt.event.*;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import org.bson.Document;
import App.HKM.Login;
import Database.HKM.MongoDB;

public class TableData extends JFrame {
	private Object[] lastItem;
	private AtomicInteger lastIndex = new AtomicInteger(-1);
	private JTable table;
	private DefaultTableModel tableModel; 
	private JPopupMenu popupMenu;
	private JMenuItem menuItemAdd;
	private JMenuItem menuItemRemove;
	private JMenuItem menuItemRemoveAll;
	public TableData(String name, String[] columnNames, Object[][] data, boolean check_tick) {
		super(name);

		// setup menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic('F');
		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic('x');
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
		this.getContentPane().add(contentPanel, BorderLayout.CENTER);
		JTable myTable = new MyTable(new MyTableModel(columnNames, data));
		myTable.setRowHeight(24);
		myTable.setSelectionBackground(Color.orange);
		myTable.setSelectionForeground(Color.black);
		myTable.getSelectionModel().setSelectionInterval(2, 2);
		TableColumn tableCol = myTable.getColumnModel().getColumn(2);
		contentPanel.add(new JScrollPane(myTable), BorderLayout.CENTER);
		this.setContentPane(contentPanel);

		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		myTable.getColumn("STT").setCellRenderer(tcr);
		myTable.getColumn("Current Price").setCellRenderer(tcr);
		myTable.getColumn("Old Price").setCellRenderer(tcr);
		myTable.getColumn("Date Insert").setCellRenderer(tcr);
		myTable.getColumn("Status").setCellRenderer(tcr);
		myTable.getColumn("STT").setMaxWidth(50);
		myTable.getColumn("Delete").setMaxWidth(50);
		myTable.getColumn("Title").setMinWidth(300);
		myTable.getColumn("Date Insert").setMinWidth(150);
		myTable.setColumnSelectionAllowed(true);
		myTable.setRowSelectionAllowed(true);
		lastItem = new Object[myTable.getColumnCount()];
		// store last value of selected table item in an array.
		myTable.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {

				lastIndex.set(myTable.getSelectedRow());
				int row = lastIndex.get();
				for (int i = 0; i < lastItem.length; i++) {
					lastItem[i] = myTable.getValueAt(row, i);

				}
				if ((!check_tick) ? (boolean) myTable.getValueAt(row, 7) : !(boolean) myTable.getValueAt(row, 7)) {
					MsgBox message = new MsgBox(null, "Do you want change?", true);
					MongoDB DataMongo = null;
					try {
						DataMongo = new MongoDB("TableWebInfo");
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (message.isOk) {
						System.out.println("Ok pressed");
						Document filter = new Document("__LinkTitle__", lastItem[2]);
						Document newValue = new Document("__Delete__", (!check_tick) ? true : false);
						DataMongo.UpdateRecord(filter, newValue);
					}

					if (!message.isOk) {
						System.out.println("Cancel pressed");
						myTable.getModel().setValueAt((!check_tick) ? false : true, row, 7);
					}
				} else {
					myTable.isCellEditable(row, 2);
					MongoDB DataMongo = null;
					String title = null;
					List<Document> docs = null;
					try {
						DataMongo = new MongoDB("TableWebInfo");
						docs = DataMongo.Query(new Document("__LinkTitle__", lastItem[2]), new Document());
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					for (Document doc : docs) {
						title = doc.getString("__Title__").toString();
					}
					if (!title.equals(lastItem[1].toString())) {
						MsgBox message = new MsgBox(null, "Do you want change?", true);

						if (message.isOk) {
							System.out.println("Ok pressed");
							Document filter = new Document("__LinkTitle__", lastItem[2]);
							Document newValue = new Document("__Title__", lastItem[1]);
							DataMongo.UpdateRecord(filter, newValue);
						}
						if (!message.isOk) {
							System.out.println("Cancel pressed");

							myTable.getModel().setValueAt(title, row, 1);

						}
					}

				}

			}
		});
		// add listeners
		// add listeners
		// show application
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(32, 32);
		setSize(1000, 500);
		setVisible(true);
	} // end CTor TableDemo
	public TableData(String name, String[] columnNames, Object[][] data) {
		super(name);

		// setup menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setMnemonic('F');
		JMenuItem menuItem = new JMenuItem("Exit");
		menuItem.setMnemonic('x');
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		menu.add(menuItem);
		menuBar.add(menu);
		setJMenuBar(menuBar);
		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
		this.getContentPane().add(contentPanel, BorderLayout.CENTER);
		JTable myTable = new MyTable(new MyTableModel(columnNames, data));
		myTable.setRowHeight(24);
		myTable.setSelectionBackground(Color.orange);
		myTable.setSelectionForeground(Color.black);
		myTable.getSelectionModel().setSelectionInterval(2, 2);
		TableColumn tableCol = myTable.getColumnModel().getColumn(2);
		contentPanel.add(new JScrollPane(myTable), BorderLayout.CENTER);
		this.setContentPane(contentPanel);
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
		tcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		myTable.getColumn("STT").setCellRenderer(tcr);
		myTable.setColumnSelectionAllowed(true);
		myTable.setRowSelectionAllowed(true);
		lastItem = new Object[myTable.getColumnCount()];
		// store last value of selected table item in an array.
		myTable.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {

				lastIndex.set(myTable.getSelectedRow());
				int row = lastIndex.get();
				for (int i = 0; i < lastItem.length; i++) {
					lastItem[i] = myTable.getValueAt(row, i);
				}
					myTable.isCellEditable(row, 2);
			}
		});
		// add listeners
		// add listeners
		// show application
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocation(32, 32);
		setSize(1000, 500);
		setVisible(true);
	} // end CTor TableDemo
	// ---------------------------------------------------------------------------------------
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

	// ---------------------------------------------------------------------------------------
	class MyTableModel extends AbstractTableModel {
		MyTableModel(String[] columnNames, Object[][] data) {
			this.columnNames = columnNames;
			this.data = data;
		}

		private String[] columnNames;
		private Object[][] data;

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.length;
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		/*
		 * JTable uses this method to determine the default renderer/ editor for
		 * each cell. If we didn't implement this method, then the last column
		 * would contain text ("true"/"false"), rather than a check box.
		 */
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			return true;
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		public void setValueAt(Object value, int row, int col) {
			data[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	}

	// ---------------------------------------------------------------------------------------
} // end class TableDemo