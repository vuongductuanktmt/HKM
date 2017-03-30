package App.HKM;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.bson.Document;

import App.HKM.Extend.Extend;
import App.HKM.Extend.TextBubbleBorder;
import Database.HKM.MongoDB;
import javax.swing.ImageIcon;
import javax.swing.DropMode;
import java.awt.Toolkit;
import java.awt.event.MouseMotionAdapter;

public class Login {
	public static String cache_Login;
	public static String cache_load_image;
	public static String cache_theme;
	public static int page_size;
	JFrame frame;
	public JTextField txt_user;
	public JPasswordField txt_password;

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
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Login() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
		UnsupportedLookAndFeelException, IOException {
		MongoDB data_extend = new MongoDB("Extend");
		cache_load_image = data_extend.GetOneRecordString(new Document("__Name__","Image"),"__Value__");
		cache_theme = data_extend.GetOneRecordString(new Document("__Name__","Theme"),"__Value__");
		page_size = data_extend.GetOneRecordInt(new Document("__Name__","PageSize"),"__Value__");
		Extend extend = new Extend();
		extend.ChangeTheme(cache_theme);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frame = new JFrame("Login");
		frame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(Login.class.getResource("/App/HKM/image/Geocaching_48px_1.png")));
		frame.setBounds(100, 100, 450, 330);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel lblThngBo = new JLabel("    ");
		lblThngBo.setIcon(new ImageIcon(Login.class.getResource("/App/HKM/image/Info Squared_20px.png")));
		JLabel lblLogin = new JLabel("\u0110\u0103ng Nh\u1EADp");
		lblLogin.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblLogin.setForeground(new Color(0, 0, 255));
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblLogin_1 = new JLabel("Login");
		JLabel lblUser = new JLabel("User:");
		lblUser.setIcon(new ImageIcon(Login.class.getResource("/App/HKM/image/User_30px.png")));
		lblUser.setForeground(SystemColor.textHighlight);
		lblUser.setFont(new Font("Tahoma", Font.PLAIN, 18));

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setIcon(new ImageIcon(Login.class.getResource("/App/HKM/image/Password_30px.png")));
		lblPassword.setForeground(SystemColor.textHighlight);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
		Extend extend = new Extend();
		txt_user = new JTextField();
		txt_user.setDropMode(DropMode.INSERT);
		txt_user.setHorizontalAlignment(SwingConstants.LEFT);
		txt_user.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txt_user.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
			}
		});
		txt_user.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		txt_user.setColumns(10);
		txt_password = new JPasswordField();
		if (extend.CheckInternet()) {
			lblThngBo.setForeground(Color.GREEN);
			lblThngBo.setText("Đã kết nối internet!");
			txt_user.setEditable(true);
			txt_password.setEditable(true);
			lblLogin_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			lblThngBo.setForeground(Color.RED);
			lblThngBo.setText("Không có kết nối internet");
			txt_user.setEditable(false);
			txt_password.setEditable(false);
			lblLogin_1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (extend.CheckInternet()) {
						lblThngBo.setForeground(Color.GREEN);
						lblThngBo.setText("Đã kết nối internet!");
						txt_user.setEditable(true);
						txt_password.setEditable(true);
						lblLogin_1.setCursor(new Cursor(Cursor.HAND_CURSOR));
					} else {
						lblThngBo.setForeground(Color.RED);
						lblThngBo.setText("Không có kết nối internet");
						txt_user.setEditable(false);
						txt_password.setEditable(false);
						lblLogin_1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		txt_password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyCode() == KeyEvent.VK_ENTER && extend.CheckInternet()) {
						lblThngBo.setForeground(Color.RED);
						String user = txt_user.getText();
						@SuppressWarnings("deprecation")
						String password = txt_password.getText();

						if (password.isEmpty() || user.isEmpty()) {
							lblThngBo.setText("Bạn không được bỏ trống.");
							txt_password.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
							txt_user.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
						} else {
							try {
								MongoDB data = new MongoDB("User");
								if (data.CheckExistsRecord(
										new Document("__User__", user).append("__Password__", password))) {
									lblThngBo.setText("Đăng nhập thành công vui lòng đợi...");
									lblThngBo.setForeground(new Color(51, 102, 153));
									cache_Login = user;
									main window1 = new main();
									window1.frame.setVisible(true);
									frame.dispose();
								} else {
									lblThngBo.setText("Tài khoản hoặc mật khẩu sai.");
								}
							} catch (Exception e1) {
								// TODO: handle exception
							}
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		txt_password.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txt_password.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
			}
		});
		txt_password.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		lblThngBo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblLogin_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (extend.CheckInternet()) {
						lblThngBo.setForeground(Color.RED);
						String user = txt_user.getText();
						@SuppressWarnings("deprecation")
						String password = txt_password.getText();

						if (password.isEmpty() || user.isEmpty()) {
							lblThngBo.setText("Bạn không được bỏ trống.");
							txt_password.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
							txt_user.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
						} else {
							try {
								MongoDB data = new MongoDB("User");
								if (data.CheckExistsRecord(
										new Document("__User__", user).append("__Password__", password))) {
									lblThngBo.setText("Đăng nhập thành công vui lòng đợi...");
									cache_Login = user;
									lblThngBo.setForeground(new Color(51, 102, 153));
									main window1 = new main();
									window1.frame.setVisible(true);
									frame.dispose();
								} else {
									lblThngBo.setText("Tài khoản hoặc mật khẩu sai.");
								}
							} catch (Exception e1) {
								// TODO: handle exception
							}
						}
					}
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

			}
		});
		lblLogin_1.setIcon(new ImageIcon(Login.class.getResource("/App/HKM/image/Submit Progress_35px_1.png")));
		lblLogin_1.setOpaque(true);
		lblLogin_1.setBackground(new Color(224, 255, 255));
		lblLogin_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLogin_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin_1.setForeground(new Color(255, 0, 0));
		lblLogin_1.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup().addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING,
										groupLayout
												.createSequentialGroup().addGroup(groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(groupLayout.createSequentialGroup()
																.addGroup(groupLayout
																		.createParallelGroup(Alignment.LEADING)
																		.addComponent(lblPassword)
																		.addComponent(lblUser))
																.addGap(18).addGroup(
																		groupLayout
																				.createParallelGroup(Alignment.LEADING,
																						false)
																				.addComponent(txt_user)
																				.addComponent(txt_password,
																						GroupLayout.DEFAULT_SIZE, 146,
																						Short.MAX_VALUE)))
														.addComponent(lblLogin, GroupLayout.PREFERRED_SIZE, 420,
																GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addContainerGap(6, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblThngBo)
										.addPreferredGap(ComponentPlacement.RELATED, 257, Short.MAX_VALUE)
										.addComponent(lblLogin_1).addGap(21)))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(7).addComponent(lblLogin).addGap(28)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblUser).addComponent(
						txt_user, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(35)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(txt_password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPassword))
				.addPreferredGap(ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblThngBo)
						.addComponent(lblLogin_1, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE))
				.addGap(25)));
		frame.getContentPane().setLayout(groupLayout);
	}
}
