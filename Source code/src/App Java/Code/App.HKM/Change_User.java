package App.HKM;

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoCursor;

import Database.HKM.database;

public class Change_User {

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
					if (Login.cache_login != null) {
						Change_User window = new Change_User();
						window.frame.setVisible(true);
					}else{
						new Message("Bạn cần đăng nhập để có thể truy cập vào.");
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
	public Change_User() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 527, 314);

		JLabel lbliMtKhu = new JLabel("Đổi mật khẩu");
		lbliMtKhu.setHorizontalAlignment(SwingConstants.CENTER);
		lbliMtKhu.setForeground(new Color(75, 0, 130));
		lbliMtKhu.setFont(new Font("Tahoma", Font.PLAIN, 21));

		JLabel lblXinChoTi = new JLabel("Xin chào tài khoản:");
		lblXinChoTi.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblXinChoTi.setForeground(new Color(0, 128, 128));

		JLabel User = new JLabel("");
		User.setFont(new Font("Tahoma", Font.BOLD, 14));
		User.setForeground(new Color(165, 42, 42));

		JLabel lblNhpMtKhu = new JLabel("Nhập mật khẩu hiện tại:");
		lblNhpMtKhu.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNhpMtKhu.setForeground(new Color(0, 128, 128));

		JLabel lblNhpMtKhu_1 = new JLabel("Nhập mật khẩu mới:");
		lblNhpMtKhu_1.setForeground(new Color(0, 128, 128));
		lblNhpMtKhu_1.setFont(new Font("Tahoma", Font.BOLD, 14));

		JLabel lblNhpLiMt = new JLabel("Nhập lại mật khẩu mới:");
		lblNhpLiMt.setForeground(new Color(0, 128, 128));
		lblNhpLiMt.setFont(new Font("Tahoma", Font.BOLD, 14));

		txtpassword_ht = new JPasswordField();

		txtpassword_renew = new JPasswordField();

		txtpassword_new = new JPasswordField();

		JLabel error = new JLabel("");
		error.setForeground(new Color(255, 0, 0));
		error.setFont(new Font("Tahoma", Font.ITALIC, 11));
		User.setText(Login.cache_login);
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Lưu");
		tglbtnNewToggleButton.addMouseListener(new MouseAdapter() {
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
				} else {
					database conn = new database("user",database.phuongthuc);
					Document whereQuery = new Document("user", "admin");
					whereQuery.append("password", this.password_ht);
					MongoCursor<Document> cursor = conn.coll.find(whereQuery).iterator();
					if (cursor.hasNext()) {
						if (this.password_new.length < 8 || this.password_renew.length < 8) {
							errors = "Độ dài mật khẩu phải lớn hơn hoặc bằng 8 kí tự.";
						} else {
							if (!Arrays.equals(this.password_new, this.password_renew)) {
								System.out.println(this.password_new);
								System.out.println(this.password_renew);
								errors = "mật khẩu nhập lại không khớp.";
							} else {
								Bson filter = new Document("user", "admin");
								Bson newValue = new Document("password", String.valueOf(this.password_new));
								Bson updateOperationDocument = new Document("$set", newValue);
								conn.coll.updateOne(filter, updateOperationDocument);
								errors = "Đổi mật khẩu thành công.";
								try {
									TimeUnit.SECONDS.sleep(2);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								new Message("Đổi mật khẩu thành công");
								frame.dispose();
							}
						}
					} else {
						errors = "Tài khoản hoặc mật khẩu không khớp.";
					}
				}
				error.setText(errors);
			}
		});
		tglbtnNewToggleButton.setForeground(new Color(255, 0, 0));
		tglbtnNewToggleButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout
				.createSequentialGroup().addGap(7)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lbliMtKhu, GroupLayout.PREFERRED_SIZE, 497, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup().addComponent(lblXinChoTi).addGap(4)
								.addComponent(User))
						.addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout
								.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addComponent(lblNhpLiMt)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(txtpassword_renew,
												GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(lblNhpMtKhu).addComponent(lblNhpMtKhu_1))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(txtpassword_ht, GroupLayout.DEFAULT_SIZE, 166,
														Short.MAX_VALUE)
												.addComponent(txtpassword_new, GroupLayout.DEFAULT_SIZE, 166,
														Short.MAX_VALUE))))
								.addGap(169))))
				.addGroup(groupLayout.createSequentialGroup().addGap(28).addComponent(error)
						.addPreferredGap(ComponentPlacement.RELATED, 314, Short.MAX_VALUE)
						.addComponent(tglbtnNewToggleButton).addGap(60)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addGap(7).addComponent(lbliMtKhu)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(4)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblXinChoTi)
										.addComponent(User))
								.addGap(35).addComponent(lblNhpMtKhu))
						.addGroup(groupLayout.createSequentialGroup().addGap(54).addComponent(txtpassword_ht,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addGap(33)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblNhpMtKhu_1).addComponent(
						txtpassword_new, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(33)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblNhpLiMt).addComponent(
						txtpassword_renew, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
						GroupLayout.PREFERRED_SIZE))
				.addGap(13).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(tglbtnNewToggleButton).addComponent(error))
				.addGap(19)));
		frame.getContentPane().setLayout(groupLayout);
	}
}

