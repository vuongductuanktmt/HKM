package App.HKM;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import App.HKM.Extend.Extend;
import App.HKM.Extend.Message;
import App.HKM.Extend.TextBubbleBorder;
import ServerManagerData.ClientToServer;
import java.awt.SystemColor;
import javax.swing.ImageIcon;

public class ChangeUser {

	JFrame frame;
	private JPasswordField txtpassword_ht;
	private JPasswordField txtpassword_renew;
	private JPasswordField txtpassword_new;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (Login.cache_Login == null) {
						ChangeUser window = new ChangeUser();
						window.frame.setVisible(true);
					} else {
						// new Message("Bạn cần đăng nhập để có thể truy cập
						// vào.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChangeUser() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Extend extend = new Extend();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 554, 366);

		JLabel lbliMtKhu = new JLabel("Đổi mật khẩu");
		lbliMtKhu.setHorizontalAlignment(SwingConstants.CENTER);
		lbliMtKhu.setForeground(new Color(75, 0, 130));
		lbliMtKhu.setFont(new Font("Tahoma", Font.PLAIN, 21));

		JLabel lblXinChoTi = new JLabel("Xin chào tài khoản:");
		lblXinChoTi.setIcon(new ImageIcon(ChangeUser.class.getResource("/App/HKM/image/Checkmark_30px.png")));
		lblXinChoTi.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblXinChoTi.setForeground(Color.GREEN);

		JLabel User = new JLabel("User");
		User.setFont(new Font("Tahoma", Font.BOLD, 14));
		User.setForeground(new Color(0, 128, 0));

		JLabel lblNhpMtKhu = new JLabel("Nhập mật khẩu hiện tại:");
		lblNhpMtKhu.setIcon(new ImageIcon(ChangeUser.class.getResource("/App/HKM/image/Password_30px_1.png")));
		lblNhpMtKhu.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNhpMtKhu.setForeground(SystemColor.textHighlight);

		JLabel lblNhpMtKhu_1 = new JLabel("Nhập mật khẩu mới:");
		lblNhpMtKhu_1.setIcon(new ImageIcon(ChangeUser.class.getResource("/App/HKM/image/Plus_30px.png")));
		lblNhpMtKhu_1.setForeground(SystemColor.textHighlight);
		lblNhpMtKhu_1.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblNhpLiMt = new JLabel("Nhập lại mật khẩu mới:");
		lblNhpLiMt.setIcon(new ImageIcon(ChangeUser.class.getResource("/App/HKM/image/Registered Trademark_30px.png")));
		lblNhpLiMt.setForeground(SystemColor.textHighlight);
		lblNhpLiMt.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtpassword_ht = new JPasswordField();
		txtpassword_ht.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtpassword_ht.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
			}
		});
		txtpassword_ht.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		txtpassword_renew = new JPasswordField();
		txtpassword_renew.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtpassword_renew.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
			}
		});
		txtpassword_renew.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		txtpassword_new = new JPasswordField();
		txtpassword_new.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtpassword_new.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
			}
		});
		txtpassword_new.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		if (Login.cache_Login != null) {
			User.setText(Login.cache_Login);
		}

		JLabel error = new JLabel("");
		error.setForeground(new Color(220, 20, 60));
		error.setIcon(new ImageIcon(ChangeUser.class.getResource("/App/HKM/image/Info Squared_20px.png")));
		JLabel Submit = new JLabel("LƯU");
		Submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		Submit.setOpaque(true);
		Submit.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		txtpassword_renew.addKeyListener(new KeyAdapter() {
			String password_ht;
			char[] password_new;
			char[] password_renew;
			String errors = "";

			@SuppressWarnings("deprecation")
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					this.password_ht = txtpassword_ht.getText();
					this.password_new = txtpassword_new.getPassword();
					this.password_renew = txtpassword_renew.getPassword();
					if (this.password_ht.isEmpty() || String.valueOf(this.password_new).isEmpty()
							|| String.valueOf(this.password_renew).isEmpty()) {
						errors = "Bạn không được bỏ trống.";
						txtpassword_ht.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
						txtpassword_new.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
						txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
					} else {
						try {
							ClientToServer CtS = new ClientToServer("check_exist_record", "admin", "User", "__User__",
									Login.cache_Login, "", 0);
							String request = CtS.getValueRequests();
							if (request.equals("true")) {
								CtS = new ClientToServer("check_exist_record", "admin", "User", "__Password__",
										extend.Token(String.valueOf(this.password_ht)), "", 0);
								request = CtS.getValueRequests();
								if (request.equals("true")) {
									if (this.password_new.length < 8 || this.password_renew.length < 8) {
										errors = "Độ dài mật khẩu phải lớn hơn hoặc bằng 8 kí tự.";
										txtpassword_new.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
										txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
									} else {
										if (!Arrays.equals(this.password_new, this.password_renew)) {
											System.out.println(this.password_new);
											System.out.println(this.password_renew);
											errors = "mật khẩu nhập lại không khớp.";
											txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
										} else {
											CtS = new ClientToServer("update", "admin", "User", "__User__",
													Login.cache_Login, "__Password__",
													extend.Token(String.valueOf(this.password_new)), 0);
											request = CtS.getValueRequests();
											errors = "Đổi mật khẩu thành công.";
											frame.setAlwaysOnTop(false);
											new Message("Đổi mật khẩu thành công");
											frame.dispose();
										}
									}
								}
							} else {
								errors = "Tài khoản hoặc mật khẩu không khớp.";
								txtpassword_new.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
								txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
							}
						} catch (Exception e2) {
							// TODO: handle exception
						}

					}
					error.setText(errors);
					txtpassword_ht.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
					txtpassword_ht.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));

				}
			}
		});
		Submit.addMouseListener(new MouseAdapter() {
			public String password_ht;
			public char[] password_new;
			private char[] password_renew;
			private String errors = "";

			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				this.password_ht = txtpassword_ht.getText();
				this.password_new = txtpassword_new.getPassword();
				this.password_renew = txtpassword_renew.getPassword();
				if (this.password_ht.isEmpty() || String.valueOf(this.password_new).isEmpty()
						|| String.valueOf(this.password_renew).isEmpty()) {
					errors = "Bạn không được bỏ trống.";
					txtpassword_ht.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
					txtpassword_new.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
					txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
				} else {
					try {
						ClientToServer CtS = new ClientToServer("check_exist_record", "admin", "User", "__User__",
								Login.cache_Login, "", 0);
						String request = CtS.getValueRequests();
						if (request.equals("true")) {
							CtS = new ClientToServer("check_exist_record", "admin", "User", "__Password__",
									extend.Token(String.valueOf(this.password_ht)), "", 0);
							request = CtS.getValueRequests();
							if (request.equals("true")) {
								if (this.password_new.length < 8 || this.password_renew.length < 8) {
									errors = "Độ dài mật khẩu phải lớn hơn hoặc bằng 8 kí tự.";
									txtpassword_new.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
									txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
								} else {
									if (!Arrays.equals(this.password_new, this.password_renew)) {
										System.out.println(this.password_new);
										System.out.println(this.password_renew);
										errors = "mật khẩu nhập lại không khớp.";
										txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
									} else {
										CtS = new ClientToServer("update", "admin", "User", "__User__",
												Login.cache_Login, "__Password__",
												extend.Token(String.valueOf(this.password_new)), 0);
										request = CtS.getValueRequests();
										errors = "Đổi mật khẩu thành công.";
										frame.setAlwaysOnTop(false);
										new Message("Đổi mật khẩu thành công");
										frame.dispose();
									}
								}
							}
						} else {
							errors = "Tài khoản hoặc mật khẩu không khớp.";
							txtpassword_new.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
							txtpassword_renew.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
						}
					} catch (Exception e2) {
						// TODO: handle exception
					}

				}
				error.setText(errors);
				txtpassword_ht.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
				txtpassword_ht.setBorder(new TextBubbleBorder(Color.RED, 2, 4, 0));
			}
		});
		Submit.setFont(new Font("Tahoma", Font.PLAIN, 21));
		Submit.setHorizontalAlignment(SwingConstants.CENTER);
		Submit.setBackground(new Color(210, 180, 140));
		Submit.setForeground(new Color(220, 20, 60));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout
				.setHorizontalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(
										groupLayout.createSequentialGroup().addGap(7)
												.addGroup(groupLayout
														.createParallelGroup(Alignment.LEADING)
														.addGroup(groupLayout.createSequentialGroup()
																.addComponent(lbliMtKhu, GroupLayout.DEFAULT_SIZE, 533,
																		Short.MAX_VALUE)
																.addContainerGap())
														.addGroup(groupLayout.createSequentialGroup()
																.addComponent(lblXinChoTi).addGap(18).addComponent(User)
																.addContainerGap())
														.addGroup(groupLayout.createSequentialGroup()
																.addGroup(groupLayout
																		.createParallelGroup(Alignment.LEADING)
																		.addGroup(groupLayout.createSequentialGroup()
																				.addGroup(groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(lblNhpLiMt)
																						.addComponent(lblNhpMtKhu)
																						.addComponent(lblNhpMtKhu_1))
																				.addGap(27)
																				.addGroup(groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(txtpassword_renew,
																								GroupLayout.DEFAULT_SIZE,
																								182, Short.MAX_VALUE)
																						.addGroup(Alignment.TRAILING,
																								groupLayout
																										.createSequentialGroup()
																										.addPreferredGap(
																												ComponentPlacement.RELATED,
																												GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												txtpassword_ht,
																												GroupLayout.PREFERRED_SIZE,
																												182,
																												GroupLayout.PREFERRED_SIZE))
																						.addComponent(txtpassword_new,
																								Alignment.TRAILING,
																								GroupLayout.DEFAULT_SIZE,
																								182, Short.MAX_VALUE))
																				.addGap(19))
																		.addGroup(groupLayout.createSequentialGroup()
																				.addGap(28).addComponent(error)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)))
																.addComponent(Submit, GroupLayout.PREFERRED_SIZE, 79,
																		GroupLayout.PREFERRED_SIZE)
																.addGap(37)))));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addGap(7).addComponent(lbliMtKhu)
				.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblXinChoTi)
						.addComponent(User))
				.addGap(35)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNhpMtKhu).addComponent(
						txtpassword_ht, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(33)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNhpMtKhu_1).addComponent(
						txtpassword_new, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(33)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblNhpLiMt).addComponent(
						txtpassword_renew, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(Submit, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(error))
				.addGap(25)));
		frame.getContentPane().setLayout(groupLayout);
	}
}
