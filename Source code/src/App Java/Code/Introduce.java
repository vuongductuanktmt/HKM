package App.HKM;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextArea;
import java.awt.Toolkit;

public class Introduce {

	JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (Login.cache_Login == null) {
						Introduce window = new Introduce();
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
	public Introduce() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Introduce");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Introduce.class.getResource("/App/HKM/image/Geocaching_48px_1.png")));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setBounds(100, 100, 583, 316);
		frame.getContentPane().setLayout(new MigLayout("", "[567px,grow]", "[26px][grow]"));
		JLabel lblGiiThiu = new JLabel("Giới thiệu");
		lblGiiThiu.setHorizontalAlignment(SwingConstants.CENTER);
		lblGiiThiu.setForeground(new Color(72, 61, 139));
		lblGiiThiu.setFont(new Font("Tahoma", Font.PLAIN, 21));
		frame.getContentPane().add(lblGiiThiu, "cell 0 0,growx,aligny top");
		
		JTextArea txtryLSn = new JTextArea();
		txtryLSn.setFont(new Font("Tahoma", txtryLSn.getFont().getStyle(), 15));
		txtryLSn.setForeground(new Color(0, 128, 128));
		txtryLSn.setTabSize(10);
		txtryLSn.setLineWrap(true);
		txtryLSn.setLineWrap(true);
		txtryLSn.setWrapStyleWord(true);
		txtryLSn.setOpaque(false);
		txtryLSn.setEditable(false);
		txtryLSn.setText("Đây là sản nhẩm của nhóm HKM lấy các thông tin khuyến mãi của các trang web bán hàng nổi tiếng và tổng hợp cho các bạn thuận tiện trong quá trình săn hàng khuyến mãi của các website cập nhật liên tục.");
		
		frame.getContentPane().add(txtryLSn, "cell 0 1,grow");
	}

}
