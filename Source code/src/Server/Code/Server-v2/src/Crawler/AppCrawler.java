package Crawler;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

import Manager.AppManagerServer;
import Server.HKM.OutputStreamServer;

public class AppCrawler extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The text area which is used for displaying logging information.
	 */
	private JTextArea textArea;

	private JButton buttonStart = new JButton("Start");
	private JButton buttonClear = new JButton("Restart");

	private PrintStream standardOut;

	public AppCrawler() throws UnsupportedEncodingException, AddressException, UnknownHostException, MessagingException, InterruptedException {
		super("Application Crawler");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AppManagerServer.class.getResource("/App/HKM/image/Geocaching_48px_1.png")));
		AppManagerServer ms = new AppManagerServer();
		textArea = new JTextArea(50, 10);
		textArea.setEditable(false);
		System.setOut(new PrintStream(new OutputStreamServer(textArea)));
		System.setErr(new PrintStream(new OutputStreamServer(textArea)));
		
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

		add(buttonStart, constraints);

		constraints.gridx = 1;
		add(buttonClear, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		
		
		add(new JScrollPane(textArea), constraints);

		// adds event handler for button Start
		buttonStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				printLog();
			}
		});

		// adds event handler for button Clear
		buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				// clears the text area
				try {
					
					Runtime.getRuntime().exec("java -jar "+ms.getCache_path()+"/AppCrawler.jar");
					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(480, 320);
		setLocationRelativeTo(null);	// centers on screen
	}

	/**
	 * Prints log statements for testing in a thread
	 */
	private void printLog() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					ThucThi ex = new ThucThi();
					try {
						ex.Crawler();
					} catch (UnknownHostException | InterruptedException | ParseException | MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
					new AppCrawler().setVisible(true);
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
}