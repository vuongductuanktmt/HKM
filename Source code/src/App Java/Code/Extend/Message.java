package App.HKM.Extend;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Message extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 public Message(String message) {
		JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
	}
	 public Message(long message) {
			JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
		}
	 public Message(Double message) {
			JOptionPane.showMessageDialog(null, message, "Thông báo", JOptionPane.WARNING_MESSAGE);
		}
}
