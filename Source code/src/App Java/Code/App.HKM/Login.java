package App.HKM;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import org.bson.Document;
import com.mongodb.client.MongoCursor;

import Database.HKM.database;

public class Login {
	public static String cache_login;
	JFrame frame;
	private JTextField txt_user;
	private JPasswordField txt_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Login");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblLogin = new JLabel("\u0110\u0103ng Nh\u1EADp");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblLogin.setForeground(new Color(0, 0, 255));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblUser = new JLabel("User:");
		lblUser.setForeground(SystemColor.textHighlight);
		lblUser.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(SystemColor.textHighlight);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));

		txt_user = new JTextField();
		txt_user.setColumns(10);
		txt_password = new JPasswordField();

		JButton btnNewButton = new JButton("Login");

		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JLabel lblThngBo = new JLabel("");
		lblThngBo.setFont(new Font("Tahoma", Font.ITALIC, 13));
		lblThngBo.setForeground(Color.RED);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String user = txt_user.getText();
				@SuppressWarnings("deprecation")
				String password = txt_password.getText();
				
				if (password.isEmpty() || user.isEmpty()) {
					lblThngBo.setText("Bạn không được b�? trống.");
				} else {
					database conn = new database("user",database.phuongthuc);
					Document whereQuery = new Document("user", user);
					whereQuery.append("password", password);

					MongoCursor<Document> cursor = conn.coll.find(whereQuery).iterator();
					if (cursor.hasNext()) {
						System.out.println("suscces.");
						cache_login = cursor.next().getString("user");
						
						Menu window1 = new Menu();
						window1.frame.setVisible(true);
						frame.dispose();
					} else {
						lblThngBo.setText("Tài khoản hoặc mật khẩu sai.");
					}
				}
			}
		});

		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(7).addComponent(lblLogin,
								GroupLayout.PREFERRED_SIZE, 420, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
								.addGap(29).addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblPassword).addComponent(lblUser))
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup().addGap(31)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
														.addComponent(txt_user).addComponent(txt_password,
																GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
										.addGroup(groupLayout.createSequentialGroup().addGap(201)
												.addComponent(btnNewButton)))))
				.addContainerGap())
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup().addGap(19).addComponent(lblThngBo)
						.addContainerGap(389, Short.MAX_VALUE)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(7).addComponent(lblLogin).addGap(28)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblUser).addComponent(
						txt_user, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(30)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblPassword).addComponent(
						txt_password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(37).addComponent(btnNewButton).addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(lblThngBo).addContainerGap(26, Short.MAX_VALUE)));
		frame.getContentPane().setLayout(groupLayout);
	}
}

