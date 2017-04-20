package App.HKM;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
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


/**
 * A Swing program that demonstrates how to create a popup menu for a JTable
 * component.
 * 
 * @author www.codejava.net
 *
 */
public class AddCategoryParent extends JFrame implements ActionListener {

	private JTable table;
	private DefaultTableModel tableModel;
	private JPopupMenu popupMenu;
	private JMenuItem menuItemAdd;
	private JMenuItem menuItemEdit;
	private JMenuItem menuItemRemove;
	private JMenuItem menuItemRemoveAll;
	public String[] columnNames;
	public static String text;
	public Object[][] rowData;

	public AddCategoryParent(String[] columnNames, Object[][] rowData, String name) {
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
		table.getColumn("Name").setCellRenderer(tcr);
		table.getColumn("SumCategoryChild").setCellRenderer(tcr);
		table.getColumn("STT").setMaxWidth(50);
		table.getColumn("Name").setMinWidth(70);
		table.getColumn("Name").setMaxWidth(200);
		table.getColumn("SumCategoryChild").setMaxWidth(200);
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		// constructs the popup menu
		popupMenu = new JPopupMenu();
		menuItemAdd = new JMenuItem("Add");
		menuItemEdit = new JMenuItem("Edit");
		menuItemRemove = new JMenuItem("Delete");
		menuItemRemoveAll = new JMenuItem("Delete All");

		menuItemAdd.addActionListener(this);
		menuItemEdit.addActionListener(this);
		menuItemRemove.addActionListener(this);
		menuItemRemoveAll.addActionListener(this);

		popupMenu.add(menuItemAdd);
		popupMenu.add(menuItemEdit);
		popupMenu.add(menuItemRemove);
		popupMenu.add(menuItemRemoveAll);

		// sets the popup menu for the table
		table.setComponentPopupMenu(popupMenu);

		table.addMouseListener(new TableMouseListener(table));

		// adds the table to the frame
		add(new JScrollPane(table));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(450, 200);
		setLocationRelativeTo(null);
		setVisible(true);
	}

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

	@Override
	public void actionPerformed(ActionEvent event) {
		JMenuItem menu = (JMenuItem) event.getSource();
		if (menu == menuItemAdd) {
			addNewRow();
		} else if (menu == menuItemRemove) {
			try {
				removeCurrentRow();
			} catch (UnknownHostException | MessagingException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (menu == menuItemRemoveAll) {
			try {
				removeAllRows();
			} catch (UnknownHostException | MessagingException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (menu == menuItemEdit) {
			try {
				EditRow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void addNewRow() {
		JFrame add = new JFrame("ADD");
		add.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JTextField textField = new JTextField();
		textField.setColumns(10);
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(Add.class.getResource("/App/HKM/image/Info Squared_20px.png")));
		JLabel lblNewLabel = new JLabel("Category Name:");
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setIcon(new ImageIcon(Add.class.getResource("/App/HKM/image/Category_20px.png")));
		textField.setOpaque(true);
		textField.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		textField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					
					if (!textField.getText().isEmpty()) {
						ClientToServer CtS = new ClientToServer("check_exist_record","admin","__CategoryParentName__", textField.getText(),"null","null",0);
						String request = CtS.getValueRequests();
						if (!request.equals("true")) {
							 CtS = new ClientToServer("create","admin", "CategoryParents", "__CategoryParentName__", textField.getText(), "null", 0);
							String data = CtS.getValueRequests();
							tableModel.addRow(new Object[] { tableModel.getRowCount() + 1, textField.getText(), 0 });
							lblNewLabel_1.setText("Insert Success.");
							lblNewLabel_1.setForeground(Color.GREEN);
						} else {
							lblNewLabel_1.setText("Parent Exists.");
							lblNewLabel_1.setForeground(Color.RED);
						}
					} else {
						lblNewLabel_1.setText("Parent name empty.");
						lblNewLabel_1.setForeground(Color.RED);
					}
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		GroupLayout groupLayout = new GroupLayout(add.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addContainerGap()
										.addComponent(lblNewLabel).addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup().addGap(28).addComponent(lblNewLabel_1)))
						.addContainerGap(236, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(44)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(
						textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(32).addComponent(lblNewLabel_1).addContainerGap(153, Short.MAX_VALUE)));
		add.getContentPane().setLayout(groupLayout);
		add.setLocationRelativeTo(null);
		add.setSize(400, 160);
		add.setVisible(true);
	}

	private void removeCurrentRow() throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		MsgBox message = new MsgBox(null, "Do you want delete?", true);
		int selectedRow = table.getSelectedRow();
		String parent_name = (String) tableModel.getValueAt(selectedRow, 1);
		if (message.isOk && !parent_name.equals("Chung")) {
			tableModel.removeRow(selectedRow);
			MongoDB data_parents = new MongoDB("CategoryParents");
			MongoDB data_childs = new MongoDB("CategoryChild");
			MongoDB data_products = new MongoDB("TableWebInfo");
			List<Document> childs = new ArrayList<>();
			childs = data_childs.Query(new Document("__CategoryParentName__", parent_name), new Document());
			for (Document child : childs) {
				List<Document> products = new ArrayList<>();
				data_products.UpdateRecordAll(new Document("__LinkTitle__", child.getString("__LinkTitle__")),
						new Document("__CheckNew__", true));
			}
			data_parents.RemoveRecordOne(new Document("__CategoryParentName__", parent_name));
			data_childs.UpdateRecordAll(new Document("__CategoryParentName__", parent_name),
					new Document("__CategoryParentName__", "Chung"));
		}
	}

	private void removeAllRows() throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		MsgBox message = new MsgBox(null, "Do you want delete all?", true);
		if (message.isOk) {
			int rowCount = tableModel.getRowCount();
			for (int i = 0; i < rowCount; i++) {
				tableModel.removeRow(0);
			}
			MongoDB data_parents = new MongoDB("CategoryParents");
			MongoDB data_childs = new MongoDB("CategoryChild");
			MongoDB data_products = new MongoDB("TableWebInfo");
			tableModel.addRow(new Object[] { 1, "Chung",
					data_childs.CountRecords(new Document("__CategoryParentName__", "Chung")) });
			List<Document> parents = new ArrayList<>();
			parents = data_parents.Query(new Document(), new Document());
			for (Document parent : parents) {
				if (!parent.getString("__CategoryParentName__").equals("Chung")) {
					List<Document> childs = new ArrayList<>();
					childs = data_childs.Query(
							new Document("__CategoryParentName__", parent.getString("__CategoryParentName__")),
							new Document());
					data_childs.UpdateRecordAll(
							new Document("__CategoryParentName__", parent.getString("__CategoryParentName__")),
							new Document("__CategoryParentName__", "Chung"));
					for (Document child : childs) {
						data_products.UpdateRecordAll(new Document("__LinkTitle__", child.getString("__LinkTitle__")),
								new Document("__CheckNew__", true));
					}
					data_parents.RemoveRecordOne(
							new Document("__CategoryParentName__", parent.getString("__CategoryParentName__")));
				}

			}

		}
	}

	private void EditRow() {
		JFrame edit = new JFrame("EDIT");
		edit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JTextField textField = new JTextField();
		textField.setColumns(10);
		int selectedRow = table.getSelectedRow();
		if (selectedRow > -1) {
			String parent_name = (String) tableModel.getValueAt(selectedRow, 1);
			textField.setText(parent_name);
			JLabel lblNewLabel_1 = new JLabel("");
			lblNewLabel_1.setIcon(new ImageIcon(Add.class.getResource("/App/HKM/image/Info Squared_20px.png")));
			JLabel lblNewLabel = new JLabel("Category Name:");
			lblNewLabel.setForeground(Color.BLUE);
			lblNewLabel.setIcon(new ImageIcon(Add.class.getResource("/App/HKM/image/Category_20px.png")));
			textField.setOpaque(true);
			textField.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
			textField.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {

					try {
						if (!textField.getText().isEmpty()) {
							if (textField.getText().equals(parent_name)) {
								lblNewLabel_1.setText("Please edit name.");
								lblNewLabel_1.setForeground(Color.RED);
							}
							ClientToServer CtS = new ClientToServer("check_exist_record","admin","CategoryParents", "__CategoryParentName__",textField.getText(),"null",0);
							String request = CtS.getValueRequests();
							if (!request.equals("true")) {
								CtS = new ClientToServer("update","admin","CategoryParents", "__CategoryParentName__",parent_name,"__CategoryParentName__",textField.getText(),0);
								 request = CtS.getValueRequests();
								//MongoDB data_childs = new MongoDB("CategoryChild");
								//data_childs.UpdateRecordAll(new Document("__CategoryParentName__", parent_name),
										//new Document("__CategoryParentName__", textField.getText()));
								tableModel.setValueAt(textField.getText(), selectedRow, 1);
								lblNewLabel_1.setText("Edit Success.");
								lblNewLabel_1.setForeground(Color.GREEN);
							}
						} else {
							lblNewLabel_1.setText("Parent name empty.");
							lblNewLabel_1.setForeground(Color.RED);
						}
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			});
			GroupLayout groupLayout = new GroupLayout(edit.getContentPane());
			groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout
									.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup().addContainerGap()
											.addComponent(lblNewLabel).addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(textField, GroupLayout.PREFERRED_SIZE,
													GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGroup(
											groupLayout.createSequentialGroup().addGap(28).addComponent(lblNewLabel_1)))
							.addContainerGap(236, Short.MAX_VALUE)));
			groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
					.addGroup(groupLayout.createSequentialGroup().addGap(44)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
											GroupLayout.PREFERRED_SIZE))
							.addGap(32).addComponent(lblNewLabel_1).addContainerGap(153, Short.MAX_VALUE)));
			edit.getContentPane().setLayout(groupLayout);
			edit.setLocationRelativeTo(null);
			edit.setSize(400, 160);
			edit.setVisible(true);
		}
	}

}