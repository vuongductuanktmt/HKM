package App.HKM;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.bson.Document;

import App.HKM.Extend.Extend;
import App.HKM.Extend.MsgBox;

import javax.swing.JComboBox;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Theme {

	JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Theme window = new Theme();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws UnknownHostException 
	 * @throws MessagingException 
	 * @throws AddressException 
	 * @throws HeadlessException 
	 * @throws InterruptedException 
	 */
	public Theme() throws UnknownHostException, HeadlessException, AddressException, MessagingException, InterruptedException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws UnknownHostException 
	 * @throws HeadlessException 
	 * @throws MessagingException 
	 * @throws AddressException 
	 * @throws InterruptedException 
	 */
	private void initialize() throws UnknownHostException, HeadlessException, AddressException, MessagingException, InterruptedException {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		MongoDB data = new MongoDB("Extend");
		JLabel lbChangeTheme = new JLabel("Change theme:");
		lbChangeTheme.setForeground(SystemColor.activeCaption);
		lbChangeTheme.setFont(new Font("Tahoma", Font.BOLD, 12));
		JLabel info = new JLabel("");
		JComboBox comboBoxChangeTheme = new JComboBox();
		comboBoxChangeTheme.setBackground(SystemColor.inactiveCaption);
		comboBoxChangeTheme.setForeground(Color.BLUE);
		comboBoxChangeTheme.setModel(new DefaultComboBoxModel(new String[] {"METAL", "FAST", "SMART", "ACRYL", "AERO", "BERNSTEIN", "ALUMINIUM", "MCWIN", "MINT", "HIFI", "NOIRE", "LUNA"}));
		comboBoxChangeTheme.setSelectedItem(ModifyXMLFile.getTheme());
		comboBoxChangeTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
					MsgBox message = new MsgBox(null, "Do you want change?", true);
					if(message.isOk){
						ModifyXMLFile.ChangeTheme(comboBoxChangeTheme.getSelectedItem().toString());
						Login.cache_load_image = (String) comboBoxChangeTheme.getSelectedItem();
						info.setText("Bạn cần logout lại để thấy thay đổi.");
					}else{
						comboBoxChangeTheme.setSelectedItem(data.GetOneRecordString(new Document("__Name__","Theme"), "__Value__"));
					}
			}
		});
		JLabel lblLoadImage = new JLabel("Load Image:");
		lblLoadImage.setForeground(SystemColor.activeCaption);
		lblLoadImage.setFont(new Font("Tahoma", Font.BOLD, 12));
		JComboBox comboBoxLoadImage = new JComboBox();
		comboBoxLoadImage.setModel(new DefaultComboBoxModel(new String[] {"true", "false"}));
			comboBoxLoadImage.setSelectedItem(ModifyXMLFile.getloadimage());
		comboBoxLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					ModifyXMLFile.Changeloadimage(comboBoxLoadImage.getSelectedItem().toString());
					Login.cache_load_image = (String) comboBoxLoadImage.getSelectedItem();
					if(Login.cache_load_image.equals("true")){
						info.setText("Bật chế độ hiển thị ảnh.");
					}else{
						info.setText("Tắt chế độ hiển thị ảnh.");
					}
			}
		});
		
		comboBoxLoadImage.setForeground(Color.RED);
		comboBoxLoadImage.setBackground(SystemColor.inactiveCaption);
		
		info.setForeground(Color.GREEN);
		info.setIcon(new ImageIcon(Theme.class.getResource("/App/HKM/image/Info Squared_20px.png")));
		
		JLabel lblPageSize = new JLabel("Page Size:");
		lblPageSize.setForeground(SystemColor.activeCaption);
		lblPageSize.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 50, 1));
		spinner.setValue(Login.page_size);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Login.page_size = (int) spinner.getValue();
				ModifyXMLFile.Changepagesize(String.valueOf(Login.page_size));
				info.setText("Bạn cần logout lại để thấy thay đổi.");
			}
		});
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(lbChangeTheme)
								.addGap(18)
								.addComponent(comboBoxChangeTheme, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGroup(groupLayout.createSequentialGroup()
								.addComponent(lblLoadImage, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(comboBoxLoadImage, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(info)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPageSize, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(231, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbChangeTheme)
						.addComponent(comboBoxChangeTheme, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addComponent(lblLoadImage, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(18)
							.addComponent(comboBoxLoadImage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPageSize, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
					.addComponent(info)
					.addGap(19))
		);
		frame.getContentPane().setLayout(groupLayout);
	}
}
