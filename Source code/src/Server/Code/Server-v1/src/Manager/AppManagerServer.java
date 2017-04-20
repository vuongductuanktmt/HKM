package Manager;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.bson.Document;

import Server.HKM.MongoDB;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.Toolkit;

public class AppManagerServer {

	private JFrame frame;
	private JTextField textFieldHost;
	private JTextField textFieldPost;
	private JTextField textFieldPath;
	private String cache_path;
	private String cache_host;
	private int cache_port;
	private long count_user;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MongoDB data_config = new MongoDB("Config");
					if (data_config.CountRecords(new Document()) == 0) {
						data_config.Collection.insertOne(new Document("__HOST__", "localhost").append("__POST__", 8081)
								.append("__PATH__", "/home/vuongductuan/Documents/AppHKM").append("__Sum__", 0));
					}
					AppManagerServer window = new AppManagerServer();
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
	 * @throws InterruptedException
	 * @throws MessagingException
	 * @throws UnknownHostException
	 * @throws AddressException
	 */
	public AppManagerServer() throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws InterruptedException
	 * @throws MessagingException
	 * @throws UnknownHostException
	 * @throws AddressException
	 */
	private void initialize() throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(AppManagerServer.class.getResource("/App/HKM/image/Geocaching_48px_1.png")));
		frame.getContentPane().setForeground(Color.GREEN);
		frame.setBounds(100, 100, 519, 311);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int post;
		String host;
		String path;
		MongoDB data_config = new MongoDB("Config");
		MongoDB data_product = new MongoDB("TableWebInfo");
		host = data_config.GetOneRecordString(new Document(), "__HOST__");
		post = data_config.GetOneRecordInt(new Document(), "__POST__");
		path = data_config.GetOneRecordString(new Document(), "__PATH__");
		setCache_host(host);
		setCache_port(post);
		setCache_path(path);
		JButton btnServer = new JButton("Run Server");
		btnServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					Runtime.getRuntime().exec("java -jar " + getCache_path() + "/AppServer.jar");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		JButton btnCrawler = new JButton("Run Crawler");
		btnCrawler.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Runtime.getRuntime().exec("java -jar " + getCache_path() + "/AppCrawler.jar");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		textFieldHost = new JTextField();
		textFieldHost.setColumns(10);

		JLabel lblNewLabel = new JLabel("HOST");
		lblNewLabel.setIcon(new ImageIcon(AppManagerServer.class.getResource("/App/HKM/image/Add Database_20px.png")));
		lblNewLabel.setForeground(Color.RED);

		JLabel lblPort = new JLabel("PORT");
		lblPort.setIcon(
				new ImageIcon(AppManagerServer.class.getResource("/App/HKM/image/Selenium Test Automation_20px.png")));
		lblPort.setForeground(Color.RED);

		textFieldPost = new JTextField();
		textFieldPost.setColumns(10);

		textFieldPath = new JTextField();
		textFieldPath.setColumns(10);
		textFieldHost.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		textFieldPost.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		textFieldPath.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		textFieldHost.setText(host);
		textFieldPost.setText((String.valueOf(post)));
		textFieldPath.setText(path);
		JLabel lblPath = new JLabel("PATH");
		lblPath.setIcon(new ImageIcon(AppManagerServer.class.getResource("/App/HKM/image/Customize View_20px.png")));
		lblPath.setForeground(Color.RED);
		JLabel lblInfo = new JLabel("");
		JButton btnSave = new JButton("Save");
		Thread thread_change = new Thread(new Runnable() {
			@Override
			public void run() {
					btnSave.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent arg0) {
							if (textFieldPost.getText().isEmpty() || textFieldHost.getText().isEmpty()
									|| textFieldPath.getText().isEmpty()) {
								lblInfo.setText("Can't save because has input empty");
								lblInfo.setForeground(Color.RED);
							} else {
								if (!data_config.CheckExistsRecord(
										new Document("__POST__", Integer.parseInt(textFieldPost.getText())))
										|| !data_config
												.CheckExistsRecord(new Document("__HOST__", textFieldHost.getText()))
										|| !data_config
												.CheckExistsRecord(new Document("__PATH__", textFieldPath.getText()))) {
									data_config.UpdateRecord(new Document("__POST__", post),
											new Document("__POST__", Integer.parseInt(textFieldPost.getText())));
									data_config.UpdateRecord(new Document("__HOST__", host),
											new Document("__HOST__", textFieldHost.getText()));
									data_config.UpdateRecord(new Document("__PATH__", path),
											new Document("__PATH__", textFieldPath.getText()));
									setCache_host(textFieldHost.getText());
									setCache_port(Integer.parseInt(textFieldPost.getText()));
									setCache_path(textFieldPath.getText());
									lblInfo.setText("Update succses.");
									lblInfo.setForeground(Color.GREEN);
								} else {
									lblInfo.setText("Can't save because has input Exists");
									lblInfo.setForeground(Color.RED);
								}

							}
						}
					});
				}
		});
		thread_change.start();
		
		btnSave.setIcon(new ImageIcon(AppManagerServer.class.getResource("/App/HKM/image/Ok_20px_3.png")));
		lblInfo.setForeground(Color.GREEN);
		lblInfo.setIcon(new ImageIcon(AppManagerServer.class.getResource("/App/HKM/image/Info Squared_20px.png")));

		JLabel lbCountRecord = new JLabel("");
		lbCountRecord.setForeground(new Color(255, 69, 0));
		lbCountRecord
				.setIcon(new ImageIcon(AppManagerServer.class.getResource("/App/HKM/image/Change Theme_20px.png")));

		JLabel labelCountUser = new JLabel("");
		labelCountUser.setForeground(new Color(75, 0, 130));
		labelCountUser.setBackground(new Color(220, 20, 60));
		labelCountUser
				.setIcon(new ImageIcon(AppManagerServer.class.getResource("/App/HKM/image/Employee Card_20px.png")));
		Thread thread_count_product = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					lbCountRecord.setText(String.valueOf(data_product.CountRecords(new Document())));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread_count_product.start();
		Thread thread_count_user = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					labelCountUser.setText(String.valueOf(data_config.GetOneRecordInt(new Document(), "__Sum__")));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread_count_user.start();
		
		JButton btnResetConf = new JButton("Reset Conf");
		
		JButton btnDeleteAll = new JButton("Delete All");
		
		JButton btnDeleteUser = new JButton("Delete User");
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(30)
					.addComponent(lblInfo)
					.addPreferredGap(ComponentPlacement.RELATED, 318, Short.MAX_VALUE)
					.addComponent(btnSave)
					.addGap(59))
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(20)
							.addComponent(btnServer)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelCountUser, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
							.addComponent(lbCountRecord))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap(45, Short.MAX_VALUE)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblPath)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(textFieldPath, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblPort)
										.addGap(28)
										.addComponent(textFieldPost, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblNewLabel)
										.addGap(28)
										.addComponent(textFieldHost, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE))))))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(25)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(btnResetConf, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
								.addComponent(btnCrawler, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(26)
							.addComponent(btnDeleteUser, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeleteAll, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)))
					.addGap(27))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnServer)
						.addComponent(btnCrawler)
						.addComponent(labelCountUser, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(lbCountRecord, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnResetConf)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(textFieldHost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(2)
									.addComponent(lblPort))
								.addComponent(textFieldPost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPath)
								.addComponent(textFieldPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnDeleteAll)
							.addGap(5)
							.addComponent(btnDeleteUser, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)))
					.addGap(59)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnSave)
						.addComponent(lblInfo))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	System.exit(0);
		        }
		    }
		});
	}

	public String getCache_path() {
		return cache_path;
	}

	public void setCache_path(String cache_path) {
		this.cache_path = cache_path;
	}

	public String getCache_host() {
		return cache_host;
	}

	public void setCache_host(String cache_host) {
		this.cache_host = cache_host;
	}

	public int getCache_port() {
		return cache_port;
	}

	public void setCache_port(int cache_port) {
		this.cache_port = cache_port;
	}

	public long getCount_user() {
		return count_user;
	}

	public void setCount_user(long count_user) {
		this.count_user = count_user;
	}
}
