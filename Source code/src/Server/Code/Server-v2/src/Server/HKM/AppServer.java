package Server.HKM;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.java_websocket.server.WebSocketServer;
import Manager.AppManagerServer;
import jline.internal.Log;

public class AppServer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The text area which is used for displaying logging information.
	 */
	private JTextArea textAreaServer;
	private long count_user;
	private JButton buttonStartServer = new JButton("Start");
	private JButton buttonClearServer = new JButton("Restart");
	AppManagerServer ms = new AppManagerServer();
	private PrintStream standardOut;
	 WebSocketServer server = new server(new InetSocketAddress(ms.getCache_host(), ms.getCache_port()));
	public AppServer() throws UnsupportedEncodingException, AddressException, UnknownHostException, MessagingException, InterruptedException {
		super("Application Server");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AppManagerServer.class.getResource("/App/HKM/image/Geocaching_48px_1.png")));
		textAreaServer = new JTextArea(50, 10);
		textAreaServer.setEditable(false);
		System.setOut(new PrintStream(new OutputStreamServer(textAreaServer)));
		System.setErr(new PrintStream(new OutputStreamServer(textAreaServer)));
		// keeps reference of standard output stream
		standardOut = System.out;

		// re-assigns standard output stream and error output stream
		

		// creates the GUI
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.anchor = GridBagConstraints.WEST;

		add(buttonStartServer, constraints);

		constraints.gridx = 1;
		add(buttonClearServer, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		
		add(new JScrollPane(textAreaServer), constraints);

		// adds event handler for button Start
		buttonStartServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					printLog();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		

		// adds event handler for button Clear
		buttonClearServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				//textAreaServer.getDocument().remove(0,
//textAreaServer.getDocument().getLength());
				try {
					server.stop();
					Runtime.getRuntime().exec("java -jar "+ms.getCache_path()+"/AppServer.jar");
					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(null, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	try {
						server.stop();
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            System.exit(0);
		        }
		    }
		});
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(480, 320);
		setLocationRelativeTo(null);	// centers on screen
	}

	/**
	 * Prints log statements for testing in a thread
	 * @throws InterruptedException 
	 */
	private void printLog() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					server.start();
					Log.info("Server is running on port " + server.getPort());
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		});
		thread.start();  
	}
	/**
	 * Runs the program
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new AppServer().setVisible(true);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public long getCount_user() {
		return count_user;
	}

	public void setCount_user(long count_user) {
		this.count_user = count_user;
	}
}