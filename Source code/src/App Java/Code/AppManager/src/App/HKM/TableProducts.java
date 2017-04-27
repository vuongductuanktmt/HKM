package App.HKM;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
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
public class TableProducts extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel tableModel;
	private JPopupMenu popupMenu;
	private JMenuItem menuItemEdit;
	private JMenuItem menuItemRemove;
	private JMenuItem menuItemRemoveAll;
	public String[] columnNames;
	public static String text;
	public Object[][] rowData;

	public TableProducts(String[] columnNames, Object[][] rowData, String name) {
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
		menuItemEdit = new JMenuItem("Edit");
		menuItemRemove = new JMenuItem("Delete");
		menuItemRemoveAll = new JMenuItem("Delete All");

		menuItemEdit.addActionListener(this);
		menuItemRemove.addActionListener(this);
		menuItemRemoveAll.addActionListener(this);

		popupMenu.add(menuItemEdit);
		popupMenu.add(menuItemRemove);
		popupMenu.add(menuItemRemoveAll);

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
		private Color evenBackColor = new Color(95,158,160);
		private Color evenTextColor = Color.white;
		private Color oddBackColor = new Color(0,206,209);
		private Color oddTextColor = Color.white;
		private Color rolloverBackColor = new Color(238,232,170);
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
				c.setForeground(new Color(25,25,112));
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
		if (menu == menuItemRemove) {
			try {
				removeCurrentRow();
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
		} else if (menu == menuItemRemoveAll) {
			try {
				removeAllRows();
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
		} else if (menu == menuItemEdit) {
			try {
				EditRow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void removeCurrentRow()
			throws AddressException, MessagingException, InterruptedException, URISyntaxException, IOException {
		MsgBox message = new MsgBox(null, "Do you want delete?", true);
		int selectedRow = table.getSelectedRow();
		String title = (String) tableModel.getValueAt(selectedRow, 1);
		if (message.isOk) {
			tableModel.removeRow(selectedRow);
			ClientToServer CtS = new ClientToServer("update", "admin", "TableWebInfo", "__Title__", title, "__Delete__",
					"true", 0);
			CtS.getValueRequests();
		}
	}

	private void removeAllRows()
			throws AddressException, MessagingException, InterruptedException, URISyntaxException, IOException {
		MsgBox message = new MsgBox(null, "Do you want delete all?", true);
		if (message.isOk) {
			int rowCount = tableModel.getRowCount();
			for (int i = 0; i < rowCount; i++) {
				tableModel.removeRow(0);
			}
			ClientToServer CtS = new ClientToServer("update", "admin", "TableWebInfo", "null", "null", "__Delete__",
					"true", 0);
			CtS.getValueRequests();

		}
	}

	private void EditRow() {
		JTextField textField_Title;
		JTextField textField_Current;
		JTextField textField_Old;
		JLabel lblOldprice;
		JButton btnSave;
		JFrame edit = new JFrame("EDIT");
		edit.getContentPane().setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		edit.setBounds(100, 100, 432, 268);
		edit.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblTitle = new JLabel("Title");
		lblTitle.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Edit Property_20px.png")));
		lblTitle.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setVerticalAlignment(SwingConstants.TOP);

		textField_Title = new JTextField();
		textField_Title.setColumns(10);

		textField_Current = new JTextField();
		textField_Current.setColumns(10);

		JLabel lblCurrent = new JLabel("CurrentPrice");
		lblCurrent.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Edit Property_20px.png")));
		lblCurrent.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		textField_Old = new JTextField();
		textField_Old.setColumns(10);
		textField_Title.setOpaque(true);
		textField_Title.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		textField_Current.setOpaque(true);
		textField_Current.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		textField_Old.setOpaque(true);
		textField_Old.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		lblOldprice = new JLabel("OldPrice");
		lblOldprice.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Edit Property_20px.png")));
		lblOldprice.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		int selectedRow = table.getSelectedRow();
		String Title = String.valueOf(tableModel.getValueAt(selectedRow, 1));
		String Current =  String.valueOf(tableModel.getValueAt(selectedRow, 2));
		String Old = String.valueOf(tableModel.getValueAt(selectedRow, 3));
		if(Current.equals("null")){
			Current="Đang cập nhật";
			textField_Current.setForeground(Color.RED);
		}
		if(Old.length()>0){
			Old = Old.substring(23,Old.indexOf('₫')+1);
		}else{
			Old="Đang cập nhật";
			textField_Old.setForeground(Color.RED);
		}
		textField_Title.setText(Title);
		textField_Current.setText(Current);
		textField_Old.setText(Old);
		JButton button = new JButton("New button");
		JLabel lblInfo = new JLabel("");
		btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!textField_Title.getText().isEmpty() && !textField_Current.getText().isEmpty()
						&& !textField_Old.getText().isEmpty()) {

					try {
						ClientToServer CtS = new ClientToServer("update", "admin", "TableWebInfo", "__Title__", Title,
								"__Title__", textField_Title.getText(), 0);
						CtS.getValueRequests();
						CtS = new ClientToServer("update", "admin", "TableWebInfo", "__Title__", Title,
								"__CurrentPrice__", textField_Current.getText(), 0);
						CtS.getValueRequests();
						CtS = new ClientToServer("update", "admin", "TableWebInfo", "__Title__", Title, "__OldPrice__",
								textField_Old.getText(), 0);
						CtS.getValueRequests();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					tableModel.setValueAt(textField_Title.getText(), selectedRow, 1);
					tableModel.setValueAt(textField_Current.getText(), selectedRow, 2);
					tableModel.setValueAt(textField_Old.getText(), selectedRow, 3);
					lblInfo.setText("Edit Succes");
					lblInfo.setForeground(Color.GREEN);
				} else {
					lblInfo.setText("Empty");
					lblInfo.setForeground(Color.RED);
				}
			}
		});

		lblInfo.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Info Squared_20px.png")));
		GroupLayout groupLayout = new GroupLayout(edit.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(lblCurrent, GroupLayout.DEFAULT_SIZE, 151,
														Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED))
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(lblTitle, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGap(108))
										.addGroup(groupLayout.createSequentialGroup()
												.addComponent(lblOldprice, GroupLayout.DEFAULT_SIZE, 151,
														Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED)))
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(textField_Old)
										.addComponent(textField_Title, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
												204, Short.MAX_VALUE)
										.addComponent(textField_Current, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
												204, Short.MAX_VALUE))
								.addGap(209))
						.addGroup(Alignment.TRAILING,
								groupLayout.createSequentialGroup().addGap(32).addComponent(lblInfo)
										.addPreferredGap(ComponentPlacement.RELATED, 283, Short.MAX_VALUE)
										.addComponent(btnSave).addGap(39).addComponent(button).addGap(29)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(61)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(lblTitle).addComponent(
						textField_Title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_Current, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCurrent))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_Old, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblOldprice))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(button)
								.addComponent(btnSave))
						.addComponent(lblInfo, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(18, Short.MAX_VALUE)));
		edit.getContentPane().setLayout(groupLayout);
		edit.setSize(432, 268);
		edit.setVisible(true);
	}

}