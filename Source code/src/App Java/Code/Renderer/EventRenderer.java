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

import App.HKM.Login;
import App.HKM.main;
import App.HKM.Data.Event;

public class EventRenderer extends JPanel implements ListCellRenderer<Event> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lbIcon = new JLabel();
	private JLabel lbName = new JLabel();
	private JLabel lbHref = new JLabel();
	private JLabel lbextend = new JLabel();
	private static final Color HOVER_COLOR = Color.YELLOW;
	private int hoverIndex = -1;
	private MouseAdapter handler;

	public EventRenderer() {
		setLayout(new BorderLayout(10, 10));
		JPanel panelText = new JPanel(new GridLayout(0, 1));
		JPanel panelImage = new JPanel();
		panelImage.add(lbIcon);
		panelText.add(lbName);
		panelText.add(lbHref);
		panelText.add(lbextend);
		add(panelImage, BorderLayout.WEST);
		add(panelText, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Event> list, Event event, int index,
			boolean isSelected, boolean cellHasFocus) {
		ImageIcon icon;
		try {
			if(Login.cache_load_image.equals("true")){
			icon = new ImageIcon(new URL(event.get__LinkImage__()));
			Image image = icon.getImage();
			// reduce by 50%
			image = image.getScaledInstance(150, 70, Image.SCALE_SMOOTH);
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
			lbHref.setBackground(list.getSelectionBackground());
			lbextend.setBackground(list.getSelectionBackground());
			lbIcon.setBackground(list.getSelectionBackground());
			setBackground(list.getSelectionBackground());
		} else { // when don't select
			lbName.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			lbHref.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			lbextend.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			lbIcon.setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
			setBackground(index == hoverIndex ? HOVER_COLOR : list.getBackground());
		}
		lbName.setText("<html><span style='color: #ffffff; background-color: #993366;border-radius: 4px;'>__"
				+ event.get__Title__() + "__</span></html>");
		lbHref.setText(
				"<html> HomePage : <a href=" + event.get__HomePage__() + ">" + event.get__HomePage__() + "</a></html>");
		lbextend.setText("<html><p><img src='" + main.class.getResource("/App/HKM/image/Like_20px.png")
				+ "' alt='embarassed' /><span style='color: #ff0000;'><strong>" + event.get__LoveCount__()
				+ "</strong></span>&nbsp;&nbsp; <img src='" + main.class.getResource("/App/HKM/image/Visible_20px.png")
				+ "' alt='laughing' /><span style='color: #008080;'>" + event.get__ViewCount__()
				+ "</span></p></html>");
		lbHref.setForeground(new Color(178, 34, 34));
		lbName.setOpaque(true);
		lbHref.setOpaque(true);
		lbextend.setOpaque(true);
		lbIcon.setOpaque(true);
		return this;
	}

	public MouseAdapter getHandler(JList<Event> list) {
		if (handler == null) {
			handler = new HoverMouseHandler(list);
		}
		return handler;
	}

	class HoverMouseHandler extends MouseAdapter {

		private final JList<Event> list;

		public HoverMouseHandler(JList<Event> list) {
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
				setHoverIndex(list.getCellBounds(index, index).contains(e.getPoint()) ? index : -1);
				this.list.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
