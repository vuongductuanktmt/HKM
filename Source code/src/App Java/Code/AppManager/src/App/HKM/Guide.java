package App.HKM;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Toolkit;

public class Guide {

	JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (Login.cache_Login == null) {
						Guide window = new Guide();
						window.frame.setVisible(true);
					}else{
						//new Message("Bạn cần đăng nhập để có thể truy cập vào.");
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
	public Guide() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Guide");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Guide.class.getResource("/App/HKM/image/Geocaching_48px_1.png")));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 568, 334);

		JLabel lblHngDnS = new JLabel("Hướng Dẫn Sử Dụng");
		lblHngDnS.setBounds(7, 7, 538, 26);
		lblHngDnS.setForeground(new Color(106, 90, 205));
		lblHngDnS.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblHngDnS.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblHngDnS);

		JTextArea txtrPhnMmS = new JTextArea();
		txtrPhnMmS.setFont(new Font("Tahoma", txtrPhnMmS.getFont().getStyle(), 15));
		txtrPhnMmS.setForeground(new Color(0, 128, 128));
		txtrPhnMmS.setTabSize(10);
		txtrPhnMmS.setLineWrap(true);
		txtrPhnMmS.setLineWrap(true);
		txtrPhnMmS.setWrapStyleWord(true);
		txtrPhnMmS.setOpaque(false);
		txtrPhnMmS.setEditable(false);
		txtrPhnMmS.setText(
				"Phần mềm sử dụng rất đơn giản chỉ cần làm theo các thao tác là bạn có thể lấy được các thông tin cần thiết. Xin cảm ơn các bạn đã sử dụng");
		txtrPhnMmS.setBounds(17, 43, 514, 242);
		frame.getContentPane().add(txtrPhnMmS);
	}
}
