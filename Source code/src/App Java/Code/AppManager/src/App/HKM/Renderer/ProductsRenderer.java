package App.HKM.Renderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import App.HKM.Login;
import App.HKM.main;
import App.HKM.Data.Products;
import App.HKM.Extend.Extend;

public class ProductsRenderer extends JPanel implements ListCellRenderer<Products> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lbIcon = new JLabel();
	private JLabel lbName = new JLabel();
	private JLabel lbPrice = new JLabel();
	private JLabel lbextend = new JLabel();
	private JLabel lbhashtag = new JLabel();
	private static final Color HOVER_COLOR = Color.lightGray;
	private int hoverIndex = -1;
	private MouseAdapter handler;

	public ProductsRenderer() {
		setLayout(new BorderLayout(10, 10));
		JPanel panelText = new JPanel(new GridLayout(0, 1));
		panelText.setBorder(new EmptyBorder(0, 0, 0, 0));
		JPanel panelImage = new JPanel();
		panelImage.setBorder(new EmptyBorder(0, 0, 0, 0));
		panelImage.add(lbIcon);
		panelText.add(lbName);
		panelText.add(lbPrice);
		panelText.add(lbhashtag);
		panelText.add(lbextend);
		add(panelImage, BorderLayout.WEST);
		add(panelText, BorderLayout.CENTER);
	}

	public Component getListCellRendererComponent(JList<? extends Products> list, Products product, int index,
			boolean isSelected, boolean cellHasFocus) {
		ImageIcon icon;
		try {
			if(Login.cache_load_image.equals("true")){
			icon = new ImageIcon(new URL(product.get__LinkImage__()));
			Image image = icon.getImage();
			// reduce by 50%
			image = image.getScaledInstance(170, 100, Image.SCALE_SMOOTH);
			icon.setImage(image);
			lbIcon.setIcon(icon);
			}else{
				lbIcon.setIcon(null);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// when select item
		if (isSelected) {
			lbName.setBackground(list.getSelectionBackground());
			lbIcon.setBackground(list.getSelectionBackground());
			lbextend.setBackground(list.getSelectionBackground());
			lbPrice.setBackground(list.getSelectionBackground());
			lbhashtag.setBackground(list.getSelectionBackground());
			setBackground(list.getSelectionBackground());
		} else { // when don't select
			lbName.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			lbIcon.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			lbextend.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			lbPrice.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			lbhashtag.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
		}
		lbName.setText("<html><span style='color: #ffffff; background-color: #6A5ACD;border-radius: 4px;'>__"
				+ product.get__Title__() + "__</span></html>");
			if (!product.get__CurrentPrice__().equals(product.get__OldPrice__())) {
				lbPrice.setText("<html><p><span style='color:red'>" + product.get__CurrentPrice__() + "&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "</span><span><strike>" + product.get__OldPrice__() + "</strike><span></p></html>");
			} else {
				lbPrice.setText(
						"<html><p><span style='color:red'>" + product.get__CurrentPrice__() + "  " + "</span></p></html>");
			}
		
		String Hashtag = "";
		for (int i = 0; i < product.get__Category__().size(); i++) {
			Extend color = new Extend();
			try {
				color.ChangeTheme(Login.cache_theme);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Hashtag += "<span style='color: " + color.getRandomColor() + "'>" + "#"
					+ product.get__Category__().getElementAt(i).get__Title__().toUpperCase() + "</span>";
		}

		lbextend.setText("<html><p><img src='" + main.class.getResource("/App/HKM/image/Like_20px.png")
				+ "' alt='embarassed' /><span style='color: #ff0000;'><strong>"+product.get__LoveCount__()+"</strong></span>&nbsp;&nbsp; <img src='"
				+ main.class.getResource("/App/HKM/image/Visible_20px.png")
				+ "' alt='laughing' /><span style='color: #008080;'>"+product.get__ViewCount__()+"</span></p></html>");
		lbhashtag.setText("<html><p><strong><span>" + Hashtag + "</span></strong></p></html>");
		lbName.setOpaque(true);
		lbextend.setOpaque(true);
		lbPrice.setOpaque(true);
		lbhashtag.setOpaque(true);
		lbIcon.setOpaque(true);
		return this;
	}

	public MouseAdapter getHandler(JList<Products> list) {
		if (handler == null) {
			handler = new HoverMouseHandler(list);
		}
		return handler;
	}

	class HoverMouseHandler extends MouseAdapter {

		private final JList<Products> list;

		public HoverMouseHandler(JList<Products> list) {
			this.list = list;
		}

		@Override
		public void mouseExited(MouseEvent e) {
			setHoverIndex(-1);
			this.list.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			try {
				int index = list.locationToIndex(e.getPoint());
				this.list.setCursor(new Cursor(Cursor.HAND_CURSOR));
				setHoverIndex(list.getCellBounds(index, index).contains(e.getPoint()) ? index : -1);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		private void setHoverIndex(int index) {
			if (hoverIndex == index)
				return;
			hoverIndex = index;
			list.repaint();
		}
	}
}
