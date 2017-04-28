package App.HKM;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.management.MBeanException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import App.HKM.Extend.FocusTraversalOnArray;
import App.HKM.Data.Event;
import App.HKM.Data.Products;
import App.HKM.Extend.Extend;
import App.HKM.Extend.Message;
import App.HKM.Extend.TextBubbleBorder;
import App.HKM.Renderer.EventRenderer;
import App.HKM.Renderer.ProductsRenderer;
import App.HKM.View.ModelView;
import ServerManagerData.ClientToServer;

public class main {
	JFrame frame;
	public static boolean Check_Bnt = false;
	DefaultListModel<Event> model_event = new DefaultListModel<>();
	JList<Event> list_event = new JList<Event>();
	DefaultListModel<Products> model_promotion = new DefaultListModel<>();
	JList<Products> list_promotion = new JList<Products>();
	DefaultListModel<Products> model_selling = new DefaultListModel<>();
	JList<Products> list_selling = new JList<Products>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (Login.cache_Login != null) {
						main window = new main();
						window.frame.setVisible(true);
					} else {
						new Message("Bạn cần login để sử dụng.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws UnknownHostException
	 * @throws InterruptedException
	 */
	public main() {
		try {
			initialize();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws UnknownHostException
	 */
	/**
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void initialize() throws InterruptedException, URISyntaxException, IOException {
		ModelView modelview = new ModelView();
		Extend extend = new Extend();
		frame = new JFrame();
		frame.setIconImage(
				Toolkit.getDefaultToolkit().getImage(main.class.getResource("/App/HKM/image/Geocaching_48px_1.png")));
		frame.getContentPane().setForeground(new Color(65, 105, 225));
		frame.setBounds(100, 100, 1298, 660);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		JMenuBar menuBar = new JMenuBar();
		JTextField txtSearch = new JTextField("Search...");
		frame.setJMenuBar(menuBar);
		JLabel First_Event_Btn = new JLabel("");
		JLabel Previous_Event_Btn = new JLabel("");
		JLabel Next_Event_Btn = new JLabel("");
		JLabel Last_Event_Btn = new JLabel("");
		JLabel First_Promotion_Btn = new JLabel("");
		JLabel Next_Promotion_Btn = new JLabel("");
		JLabel Last_Promotion_Btn = new JLabel("");
		JLabel Previous_Promotion_Btn = new JLabel("");

		JLabel First_Selling_Btn = new JLabel("");
		JLabel Next_Selling_Btn = new JLabel("");
		JLabel Last_Selling_Btn = new JLabel("");
		JLabel Previous_Selling_Btn = new JLabel("");
		int max_selling;
		int max_event;
		int max_promotion;
		ClientToServer CtS = new ClientToServer("max_page", Login.page_size, "admin", "TableWebInfo",
				txtSearch.getText(), "Event");
		extend.CheckCursor(First_Event_Btn, Previous_Event_Btn, Next_Event_Btn, Last_Event_Btn,
				max_event = Integer.parseInt(CtS.getValueRequests()), ModelView.pageNumer__Event);
		CtS = new ClientToServer("max_page", Login.page_size, "admin", "TableWebInfo", txtSearch.getText(), "Selling");
		extend.CheckCursor(First_Selling_Btn, Previous_Selling_Btn, Next_Selling_Btn, Last_Selling_Btn,
				max_selling = Integer.parseInt(CtS.getValueRequests()), ModelView.pageNumer__Selling);
		CtS = new ClientToServer("max_page", Login.page_size, "admin", "TableWebInfo", txtSearch.getText(),
				"Promotion");
		extend.CheckCursor(First_Promotion_Btn, Previous_Promotion_Btn, Next_Promotion_Btn, Last_Promotion_Btn,
				max_promotion = Integer.parseInt(CtS.getValueRequests()), ModelView.pageNumer__Promotion);

		JMenu mnSetting = new JMenu("Setting");
		mnSetting.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Gears_20px.png")));
		menuBar.add(mnSetting);
		txtSearch.setBorder(new TextBubbleBorder(new Color(135, 206, 235), 2, 4, 0));
		txtSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		txtSearch.setHorizontalAlignment(SwingConstants.CENTER);
		txtSearch.setForeground(new Color(255, 255, 255));
		txtSearch.setBackground(new Color(135, 206, 235));
		txtSearch.setColumns(10);
		txtSearch.setMinimumSize(new Dimension(150, 34));

		JMenu mnUser = new JMenu("User");
		mnUser.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/User_20px.png")));
		mnSetting.add(mnUser);

		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChangeUser changeuser = new ChangeUser();
				changeuser.frame.setVisible(true);
			}
		});
		mntmChangePassword.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Privacy_20px.png")));
		mnUser.add(mntmChangePassword);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Login.cache_Login = null;
				Login FrameLogin = null;
				try {
					FrameLogin = new Login();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | MBeanException
						| UnsupportedLookAndFeelException | IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.dispose();
				FrameLogin.frame.setVisible(true);

			}
		});
		mntmLogout.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Logout Rounded Up_20px.png")));
		mnUser.add(mntmLogout);

		JMenu mnView_1 = new JMenu("View");
		mnView_1.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Customize View_20px.png")));
		mnSetting.add(mnView_1);

		JMenu mnCategory = new JMenu("Category");
		mnCategory.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Category_20px.png")));
		mnView_1.add(mnCategory);

		JMenuItem mntmCategoryParents = new JMenuItem("Category Parents");
		mntmCategoryParents.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Ok_20px_3.png")));
		mntmCategoryParents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							modelview.LoadCategoryParents();
							new AddCategoryParent(modelview.columnNamesCategoryParent, ModelView.rows,
									"Category Parent");
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		mnCategory.add(mntmCategoryParents);

		JMenuItem mntmCategoryChilds = new JMenuItem("Category Childs");
		mntmCategoryChilds.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Ok_20px_3.png")));
		mnCategory.add(mntmCategoryChilds);
		mntmCategoryChilds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView modelview = new ModelView();
						try {
							modelview.LoadCategoryChilds();
							new AddCategoryChild(modelview.columnNamesCategoryChild, modelview.rows,
									"Manager Category Childs");
						} catch (URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread.start();

			}
		});
		JMenuItem mntmShop = new JMenuItem("Shop");
		mntmShop.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Shopping Bag_20px.png")));
		mnView_1.add(mntmShop);

		JMenuItem mntmTheme = new JMenuItem("Theme");
		mntmTheme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Theme theme;
				try {
					theme = new Theme();
					theme.frame.setVisible(true);
				} catch (HeadlessException | UnknownHostException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		mntmTheme.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Change Theme_20px.png")));
		mnView_1.add(mntmTheme);

		JMenu mnConnect = new JMenu("Connect");
		mnConnect.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Connection Sync_20px.png")));
		mnSetting.add(mnConnect);

		JMenuItem mntmEdit = new JMenuItem("Edit");
		mntmEdit.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Edit Property_20px.png")));
		mnConnect.add(mntmEdit);

		JMenuItem mntmTest = new JMenuItem("Test");
		mntmTest.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Selenium Test Automation_20px.png")));
		mnConnect.add(mntmTest);

		JMenuItem mntmAdd = new JMenuItem("Add");
		mntmAdd.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Add Database_20px.png")));
		mnConnect.add(mntmAdd);

		JMenu mnNotification = new JMenu("Notification");
		mnNotification.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Push Notifications_20px.png")));
		mnSetting.add(mnNotification);

		JMenuItem mntmEdit_1 = new JMenuItem("Edit");
		mntmEdit_1.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Edit Property_20px.png")));
		mnNotification.add(mntmEdit_1);

		JMenuItem mntmAdd_1 = new JMenuItem("Error");
		mntmAdd_1.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Close Window_20px.png")));
		mnNotification.add(mntmAdd_1);
		mntmAdd_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView modelview = new ModelView();
						try {
							modelview.LoadError();
							new ListError(modelview.columnNamesError, modelview.rows, "List Error");
						} catch (URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread.start();

			}
		});

		JMenu mnView = new JMenu("View");
		mnView.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Customize View_20px.png")));
		menuBar.add(mnView);

		JMenuItem mntmStatistics = new JMenuItem("Statistics");
		mntmStatistics.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Statistics_20px.png")));
		mnView.add(mntmStatistics);

		JMenuItem mntmNewMenuItem = new JMenuItem("Record");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread_record = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Properties props = new Properties();
							props.put("logoString", "my company");
							props.put("textShadow", "off");
							props.put("systemTextFont", "Arial PLAIN 13");
							props.put("controlTextFont", "Arial PLAIN 13");
							props.put("menuTextFont", "Arial PLAIN 13");
							props.put("userTextFont", "Arial PLAIN 13");
							props.put("subTextFont", "Arial PLAIN 12");
							props.put("windowTitleFont", "Arial BOLD 13");
							// start application
							// modelview.LoadTableProduct(columnNames,data);
							modelview.LoadTableProduct();
							new TableProducts(modelview.columnNamesProducts, modelview.rows, "Manager Products");

						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				});
				thread_record.start();

			}
		});
		mntmNewMenuItem.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Report Card_20px_1.png")));
		mnView.add(mntmNewMenuItem);

		JMenuItem mntmRecycel = new JMenuItem("Recycle");
		mntmRecycel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						Properties props = new Properties();
						props.put("logoString", "my company");
						props.put("textShadow", "off");
						props.put("systemTextFont", "Arial PLAIN 13");
						props.put("controlTextFont", "Arial PLAIN 13");
						props.put("menuTextFont", "Arial PLAIN 13");
						props.put("userTextFont", "Arial PLAIN 13");
						props.put("subTextFont", "Arial PLAIN 12");
						props.put("windowTitleFont", "Arial BOLD 13");
						// start application
						// modelview.LoadTableProduct(columnNames,data);
						try {
							modelview.LoadTableProductRecycle();
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						new TableProductsRecycle(modelview.columnNamesProducts, modelview.rows,
								"Manager recover deleted products");

					}
				});
				thread.start();

			}
		});
		mntmRecycel.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Trash_20px.png")));
		mnView.add(mntmRecycel);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Question_20px.png")));
		menuBar.add(mnHelp);

		JMenuItem mntmIntroduction = new JMenuItem("Introduction");
		mntmIntroduction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Introduce FrameIntroduce = new Introduce();
				FrameIntroduce.frame.setVisible(true);
			}
		});
		mntmIntroduction.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Employee Card_20px.png")));
		mnHelp.add(mntmIntroduction);

		JMenuItem mntmGuide = new JMenuItem("Guide");
		mntmGuide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Guide window1 = new Guide();
				window1.frame.setVisible(true);
			}
		});
		mntmGuide.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/User Manual_20px.png")));
		mnHelp.add(mntmGuide);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		mntmExit.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Close Window_20px.png")));
		mnHelp.add(mntmExit);

		JDesktopPane desktopPane = new JDesktopPane();

		JInternalFrame internalFrame_extend = new JInternalFrame("Extend");
		internalFrame_extend.setFrameIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Filter_20px.png")));
		internalFrame_extend.setBackground(new Color(230, 230, 250));
		internalFrame_extend.setVisible(true);

		JInternalFrame internalFrame_selling = new JInternalFrame("Selling");
		internalFrame_selling
				.setFrameIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Best Seller_20px_1.png")));
		internalFrame_selling.setVisible(true);

		JInternalFrame internalFrame_promotion = new JInternalFrame("Promotion");
		internalFrame_promotion.setFrameIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Sale_20px.png")));
		internalFrame_promotion.setVisible(true);

		JInternalFrame internalFrame_event = new JInternalFrame("Event");
		internalFrame_event.setFrameIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Today_20px.png")));
		internalFrame_event.setVisible(true);
		JSpinner spinner_selling = new JSpinner();
		spinner_selling.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Selling = (int) spinner_selling.getValue();
						try {
							modelview.ReloadJlist(list_selling, model_selling, "Selling", txtSearch.getText());
							extend.CheckCursor(First_Selling_Btn, Previous_Selling_Btn, Next_Selling_Btn,
									Last_Selling_Btn, max_selling, ModelView.pageNumer__Selling);
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread.start();

			}
		});
		spinner_selling.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(max_selling), new Integer(1)));
		spinner_selling.setBackground(SystemColor.activeCaption);
		spinner_selling.setForeground(SystemColor.activeCaption);

		JSpinner spinner_promotion = new JSpinner();
		spinner_promotion.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(max_promotion), new Integer(1)));
		spinner_promotion.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Promotion = (int) spinner_promotion.getValue();
						try {
							modelview.ReloadJlist(list_promotion, model_promotion, "Promotion", txtSearch.getText());
							extend.CheckCursor(First_Promotion_Btn, Previous_Promotion_Btn, Next_Promotion_Btn,
									Last_Promotion_Btn, max_promotion, ModelView.pageNumer__Promotion);
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread.start();

			}
		});
		spinner_promotion.setForeground(SystemColor.activeCaption);
		spinner_promotion.setBackground(SystemColor.activeCaption);

		JSpinner spinner_event = new JSpinner();
		spinner_event.setModel(
				new SpinnerNumberModel(new Integer(1), new Integer(1), new Integer(max_event), new Integer(1)));
		spinner_event.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Event = (int) spinner_event.getValue();
						try {
							modelview.ReloadJlist(list_event, model_event, "Event", txtSearch.getText());
							extend.CheckCursor(First_Event_Btn, Previous_Event_Btn, Next_Event_Btn, Last_Event_Btn,
									max_event, ModelView.pageNumer__Event);
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		spinner_event.setForeground(SystemColor.activeCaption);
		spinner_event.setBackground(SystemColor.activeCaption);

		First_Promotion_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Promotion = 1;
						spinner_promotion.setValue(ModelView.pageNumer__Promotion);
						try {
							modelview.ReloadJlist(list_promotion, model_promotion, "Promotion", txtSearch.getText());
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		First_Promotion_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/First_30px.png")));

		Previous_Promotion_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						if (ModelView.pageNumer__Promotion != 1) {
							ModelView.pageNumer__Promotion--;
							spinner_promotion.setValue(ModelView.pageNumer__Promotion);
							try {
								modelview.ReloadJlist(list_promotion, model_promotion, "Promotion",
										txtSearch.getText());
							} catch (InterruptedException | URISyntaxException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
				thread.start();

			}
		});
		Previous_Promotion_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Circled Left_30px.png")));

		Next_Promotion_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						if (ModelView.pageNumer__Promotion < max_promotion) {
							ModelView.pageNumer__Promotion++;
							spinner_promotion.setValue(ModelView.pageNumer__Promotion);
							try {
								modelview.ReloadJlist(list_promotion, model_promotion, "Promotion",
										txtSearch.getText());
							} catch (InterruptedException | URISyntaxException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});
				thread.start();

			}
		});
		Next_Promotion_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Circled Right_30px.png")));

		Last_Promotion_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Promotion = max_promotion;
						spinner_promotion.setValue(ModelView.pageNumer__Promotion);
						try {
							modelview.ReloadJlist(list_promotion, model_promotion, "Promotion", txtSearch.getText());
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		Last_Promotion_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Last_30px.png")));

		First_Selling_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Selling = 1;
						spinner_selling.setValue(ModelView.pageNumer__Selling);
						try {
							modelview.ReloadJlist(list_selling, model_selling, "Selling", txtSearch.getText());
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		First_Selling_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/First_30px.png")));

		Previous_Selling_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						if (ModelView.pageNumer__Selling != 1) {
							ModelView.pageNumer__Selling--;
							spinner_selling.setValue(ModelView.pageNumer__Selling);
							try {
								modelview.ReloadJlist(list_selling, model_selling, "Selling", txtSearch.getText());
							} catch (InterruptedException | URISyntaxException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});
				thread.start();

			}
		});
		Previous_Selling_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Circled Left_30px.png")));

		Next_Selling_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						if (ModelView.pageNumer__Selling < max_selling) {
							ModelView.pageNumer__Selling++;
							spinner_selling.setValue(ModelView.pageNumer__Selling);
							try {
								modelview.ReloadJlist(list_selling, model_selling, "Selling", txtSearch.getText());
							} catch (InterruptedException | URISyntaxException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});
				thread.start();

			}
		});
		Next_Selling_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Circled Right_30px.png")));

		Last_Selling_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Selling = max_selling;
						spinner_selling.setValue(ModelView.pageNumer__Selling);
						try {
							modelview.ReloadJlist(list_selling, model_selling, "Selling", txtSearch.getText());
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		Last_Selling_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Last_30px.png")));
		First_Event_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Event = 1;
						spinner_event.setValue(ModelView.pageNumer__Event);
						try {
							modelview.ReloadJlist(list_event, model_event, "Event", txtSearch.getText());
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		First_Event_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/First_30px.png")));

		Previous_Event_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						if (ModelView.pageNumer__Event != 1) {
							ModelView.pageNumer__Event--;
							spinner_event.setValue(ModelView.pageNumer__Event);
							try {
								modelview.ReloadJlist(list_event, model_event, "Event", txtSearch.getText());
							} catch (InterruptedException | URISyntaxException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});
				thread.start();

			}
		});
		Previous_Event_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Circled Left_30px.png")));

		Next_Event_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						if (ModelView.pageNumer__Event < max_event) {
							ModelView.pageNumer__Event++;
							spinner_event.setValue(ModelView.pageNumer__Event);
							try {
								modelview.ReloadJlist(list_event, model_event, "Event", txtSearch.getText());
							} catch (InterruptedException | URISyntaxException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});
				thread.start();

			}
		});
		Next_Event_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Circled Right_30px.png")));

		Last_Event_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						ModelView.pageNumer__Event = max_event;
						spinner_event.setValue(ModelView.pageNumer__Event);
						try {
							modelview.ReloadJlist(list_event, model_event, "Event", txtSearch.getText());
						} catch (InterruptedException | URISyntaxException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
				thread.start();

			}
		});
		Last_Event_Btn.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Last_30px.png")));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(internalFrame_extend, GroupLayout.DEFAULT_SIZE, 1292, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(internalFrame_selling, GroupLayout.PREFERRED_SIZE, 433,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(internalFrame_promotion, GroupLayout.PREFERRED_SIZE, 422,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(internalFrame_event, GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup().addGap(125).addComponent(First_Selling_Btn).addGap(18)
						.addComponent(Previous_Selling_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(Next_Selling_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(17)
						.addComponent(Last_Selling_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(desktopPane, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE).addGap(40)
						.addComponent(spinner_selling, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(201).addComponent(First_Promotion_Btn).addGap(18)
						.addComponent(Previous_Promotion_Btn, GroupLayout.PREFERRED_SIZE, 30,
								GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(Next_Promotion_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(Last_Promotion_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(41)
						.addComponent(spinner_promotion, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 172, Short.MAX_VALUE).addComponent(First_Event_Btn)
						.addGap(18)
						.addComponent(Previous_Event_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(Next_Event_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(18)
						.addComponent(Last_Event_Btn, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addGap(38)
						.addComponent(spinner_event, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addGap(57)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(internalFrame_extend, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout
								.createParallelGroup(Alignment.LEADING, false)
								.addComponent(internalFrame_event, 0, 0, Short.MAX_VALUE)
								.addComponent(internalFrame_selling, 0, 0, Short.MAX_VALUE)
								.addComponent(internalFrame_promotion, GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(groupLayout
								.createParallelGroup(Alignment.LEADING).addComponent(desktopPane,
										GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(First_Selling_Btn)
										.addComponent(Previous_Selling_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(Next_Selling_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(Last_Selling_Btn, GroupLayout.PREFERRED_SIZE, 30,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(spinner_selling, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(First_Promotion_Btn)
										.addComponent(Previous_Promotion_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(Next_Promotion_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(Last_Promotion_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(First_Event_Btn)
										.addComponent(Previous_Event_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(Next_Event_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(Last_Event_Btn, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(spinner_event, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(spinner_promotion, GroupLayout.PREFERRED_SIZE, 30,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		// ----------------------------------------------------------------------------------------------------------------
		model_event = modelview.ListEvents(txtSearch.getText());
		list_event = new JList<Event>(model_event);
		EventRenderer renderer_event = new EventRenderer();
		list_event.setCellRenderer(renderer_event);
		list_event.addMouseListener(renderer_event.getHandler(list_event));
		list_event.addMouseMotionListener(renderer_event.getHandler(list_event));
		list_event.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// build your address / link
				URI uri;
				try {
					uri = new URI(
							list_event.getModel().getElementAt(list_event.locationToIndex(e.getPoint())).get__Href__());
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(uri);
						} catch (IOException e1) {
							/* TODO: error handling */
						}
					} else {
						/* TODO: error handling */ }
				} catch (URISyntaxException x) {
					// TODO Auto-generated catch block
					x.printStackTrace();
				}

				// see below
			}
		});
		list_event.setFont(new Font("Tahoma", Font.PLAIN, 11));
		JScrollPane scrollpane_event = new JScrollPane(list_event);
		scrollpane_event.setPreferredSize(new Dimension(frame.getWidth() / 3, frame.getHeight() - 200));
		internalFrame_event.getContentPane().add(scrollpane_event, BorderLayout.NORTH);
		list_event.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					JList<?> l = (JList<?>) e.getSource();
					l.setToolTipText(list_event.getModel().getElementAt(list_event.locationToIndex(e.getPoint()))
							.get__Title__());
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
		model_promotion = modelview.ListProducts("Promotion", txtSearch.getText());
		list_promotion = new JList<Products>(model_promotion);
		ProductsRenderer renderer_promotion = new ProductsRenderer();
		list_promotion.setCellRenderer(renderer_promotion);
		list_promotion.addMouseListener(renderer_promotion.getHandler(list_promotion));
		list_promotion.addMouseMotionListener(renderer_promotion.getHandler(list_promotion));
		list_promotion.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					JList<?> l = (JList<?>) e.getSource();
					l.setToolTipText(list_promotion.getModel()
							.getElementAt(list_promotion.locationToIndex(e.getPoint())).get__Title__());
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
		list_promotion.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// build your address / link
				URI uri;
				try {
					uri = new URI(list_promotion.getModel().getElementAt(list_promotion.locationToIndex(e.getPoint()))
							.get__Href__());
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(uri);
						} catch (IOException e1) {
							/* TODO: error handling */
						}
					} else {
						/* TODO: error handling */ }
				} catch (URISyntaxException x) {
					// TODO Auto-generated catch block
					x.printStackTrace();
				}

				// see below
			}
		});
		list_promotion.setFont(new Font("Tahoma", Font.PLAIN, 11));
		list_promotion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollpane_1 = new JScrollPane(list_promotion);
		scrollpane_1.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight() - 200));
		internalFrame_promotion.getContentPane().add(scrollpane_1, BorderLayout.NORTH);

		model_selling = modelview.ListProducts("Selling", txtSearch.getText());
		System.out.println(txtSearch.getText());
		list_selling = new JList<Products>(model_selling);
		ProductsRenderer renderer_selling = new ProductsRenderer();
		list_selling.setCellRenderer(renderer_selling);
		list_selling.addMouseListener(renderer_selling.getHandler(list_selling));
		list_selling.addMouseMotionListener(renderer_selling.getHandler(list_selling));
		list_selling.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				try {
					JList<?> l = (JList<?>) e.getSource();
					l.setToolTipText(list_selling.getModel().getElementAt(list_selling.locationToIndex(e.getPoint()))
							.get__Title__());
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		list_selling.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// build your address / link
				try {
					URI uri;
					uri = new URI(list_selling.getModel().getElementAt(list_selling.locationToIndex(e.getPoint()))
							.get__Href__());
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().browse(uri);
						} catch (IOException e1) {
							/* TODO: error handling */
						}
					} else {
						/* TODO: error handling */ }
				} catch (URISyntaxException x) {
					// TODO Auto-generated catch block
					x.printStackTrace();
				}

				// see below
			}
		});
		list_selling.setFont(new Font("Tahoma", Font.PLAIN, 11));
		list_selling.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollpane_selling = new JScrollPane(list_selling);
		scrollpane_selling.setPreferredSize(new Dimension(frame.getWidth() / 2, frame.getHeight() - 200));
		internalFrame_selling.getContentPane().add(scrollpane_selling, BorderLayout.NORTH);

		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					ModelView.pageNumer__Event = 1;
					ModelView.pageNumer__Selling = 1;
					ModelView.pageNumer__Promotion = 1;
					extend.CheckCursor(First_Event_Btn, Previous_Event_Btn, Next_Event_Btn, Last_Event_Btn, max_event,
							ModelView.pageNumer__Event);
					extend.CheckCursor(First_Selling_Btn, Previous_Selling_Btn, Next_Selling_Btn, Last_Selling_Btn,
							max_selling, ModelView.pageNumer__Selling);
					extend.CheckCursor(First_Promotion_Btn, Previous_Promotion_Btn, Next_Promotion_Btn,
							Last_Promotion_Btn, max_promotion, ModelView.pageNumer__Promotion);
					if (max_selling != 0) {
						spinner_selling.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
								new Integer(max_selling), new Integer(1)));
					} else {
						spinner_selling.setModel(
								new SpinnerNumberModel(new Integer(0), new Integer(0), new Integer(0), new Integer(0)));

					}
					if (max_promotion != 0) {
						spinner_promotion.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
								new Integer(max_promotion), new Integer(1)));
					} else {
						spinner_promotion.setModel(
								new SpinnerNumberModel(new Integer(0), new Integer(0), new Integer(0), new Integer(0)));

					}
					if (max_event != 0) {
						spinner_event.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1),
								new Integer(max_event), new Integer(0)));
					} else {
						spinner_event.setModel(
								new SpinnerNumberModel(new Integer(0), new Integer(0), new Integer(0), new Integer(0)));

					}
					try {
						modelview.ReloadJlist(list_event, model_event, "Event", txtSearch.getText());
						modelview.ReloadJlist(list_selling, model_selling, "Selling", txtSearch.getText());
						modelview.ReloadJlist(list_promotion, model_promotion, "Promotion", txtSearch.getText());
					} catch (InterruptedException | URISyntaxException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			}
		});
		txtSearch.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (txtSearch.getText().equals("Search...")) {
					txtSearch.setText("");
					txtSearch.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (txtSearch.getText().isEmpty()) {
					txtSearch.setForeground(new Color(255, 255, 255));
					txtSearch.setText("Search...");
				}
			}
		});

		JComboBox Category_Combobox = new JComboBox();
		Category_Combobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(Category_Combobox.getSelectedItem());
			}
		});
		Category_Combobox.setModel(new DefaultComboBoxModel(new String[] { "Shop - Lazada", "Shop - Tiki",
				"Shop - Sendo", "Category - Công nghệ", "Category - Thời trang" }));
		Category_Combobox.setBackground(new Color(240, 248, 255));
		Category_Combobox.setForeground(new Color(30, 144, 255));
		Category_Combobox.setToolTipText("Shop");
		frame.getContentPane()
				.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { txtSearch, Category_Combobox }));

		JComboBox Money_Comobox = new JComboBox();
		Money_Comobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(Money_Comobox.getSelectedItem());
			}
		});
		Money_Comobox.setModel(new DefaultComboBoxModel(new String[] { "0-100.000đ", "100.000đ-200.000đ",
				"200.000đ-500.000đ", "500.000đ-1.000.000đ", ">1.000.000đ" }));
		Money_Comobox.setToolTipText("Shop");
		Money_Comobox.setForeground(new Color(0, 128, 0));
		Money_Comobox.setBackground(new Color(240, 248, 255));

		JComboBox Hobby_Combobox = new JComboBox();
		Hobby_Combobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(Hobby_Combobox.getSelectedItem());
			}
		});
		Hobby_Combobox.setModel(new DefaultComboBoxModel(new String[] { "Yêu thích", "Ghé thăm" }));
		Hobby_Combobox.setToolTipText("Shop");
		Hobby_Combobox.setForeground(new Color(250, 128, 114));
		Hobby_Combobox.setBackground(new Color(240, 248, 255));

		JLabel Icon_Category_Combobox = new JLabel("");
		Icon_Category_Combobox.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Tree Structure_30px.png")));

		JLabel Icon_Money_Combobox = new JLabel("");
		Icon_Money_Combobox.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Money Bag_40px.png")));

		JLabel Icon_Hobby_Combobox = new JLabel("");
		Icon_Hobby_Combobox.setIcon(new ImageIcon(main.class.getResource("/App/HKM/image/Hearts_35px.png")));
		GroupLayout groupLayout_1 = new GroupLayout(internalFrame_extend.getContentPane());
		groupLayout_1.setHorizontalGroup(groupLayout_1.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout_1
				.createSequentialGroup()
				.addComponent(Icon_Category_Combobox, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(Category_Combobox, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
				.addGap(143).addComponent(Icon_Money_Combobox).addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(Money_Comobox, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE).addGap(160)
				.addComponent(Icon_Hobby_Combobox, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(Hobby_Combobox, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE).addGap(244)
				.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE).addGap(60)));
		groupLayout_1.setVerticalGroup(groupLayout_1.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout_1
				.createSequentialGroup()
				.addGroup(groupLayout_1.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING,
								groupLayout_1.createSequentialGroup().addContainerGap().addComponent(Category_Combobox,
										GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, groupLayout_1.createSequentialGroup().addContainerGap()
								.addGroup(groupLayout_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(Money_Comobox, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(Hobby_Combobox, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)))
						.addComponent(Icon_Category_Combobox, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 31,
								Short.MAX_VALUE)
						.addComponent(Icon_Money_Combobox, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 31,
								Short.MAX_VALUE)
						.addComponent(Icon_Hobby_Combobox, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 31,
								Short.MAX_VALUE)
						.addComponent(txtSearch, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
				.addContainerGap()));
		internalFrame_extend.getContentPane().setLayout(groupLayout_1);
		frame.getContentPane().setLayout(groupLayout);
	}

}
