package App.HKM.Extend;

import java.awt.Cursor;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Extend {
	String letters[] = "0123456789ABCDEF".split("");
	int min = letters.length - (letters.length / 3);
	int max = letters.length;
	Random rnd = new Random(1000);
	String colorEx[] = new String[] { "00", "00", "00" };
	int colorChange = 0;
	int addColorChange = 1;

	public String getRandomColor() {
		final Random random = new Random();
		final String[] letters = "0123456789ABCDEF".split("");
		String color = "#";
		for (int i = 0; i < 6; i++) {
			color += letters[Math.round(random.nextFloat() * 15)];
		}
		return color;
	}

	public void CheckCursor(JLabel First_Btn, JLabel Previous_Btn, JLabel Next_Btn, JLabel Last_Btn, int pageMax,
			int pageNow) {
		if (pageNow == 1) {
			Previous_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			First_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Next_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			Last_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else if (pageNow == pageMax) {
			Next_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Last_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Previous_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			First_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		} else {
			Next_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			Last_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			Previous_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
			First_Btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		if (pageMax == 1) {
			Next_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Last_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			Previous_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			First_Btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public boolean CheckInternet() throws IOException {
		int timeout = 2000;
		try {
			InetAddress[] addresses = InetAddress.getAllByName("www.google.com");
			for (InetAddress address : addresses) {
				if (address.isReachable(timeout)) {
					System.out.printf("%s is reachable%n", address);
					return true;
				} else
					System.out.printf("%s could not be contacted%n", address);
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	public void ChangeTheme(String theme) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
		switch(theme){
		case "METAL":
			 javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme());
			 UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			break;
		case "FAST":
		com.jtattoo.plaf.luna.LunaLookAndFeel.setTheme("Default");
		UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
			break;
		case "SMART":
			com.jtattoo.plaf.smart.SmartLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
			break;
		case "ACRYL":
			com.jtattoo.plaf.acryl.AcrylLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
			break;
		case "AERO":
			com.jtattoo.plaf.aero.AeroLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
			break;
		case "BERNSTEIN":
			com.jtattoo.plaf.bernstein.BernsteinLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
			break;
		case "ALUMINIUM":
			 com.jtattoo.plaf.aluminium.AluminiumLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel(" com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
			break;
		case "MCWIN":
			com.jtattoo.plaf.mcwin.McWinLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			break;
		case "MINT":
			com.jtattoo.plaf.mint.MintLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
			break;
		case "HIFI":
			com.jtattoo.plaf.hifi.HiFiLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
			break;
		case "NOIRE":
			com.jtattoo.plaf.noire.NoireLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
			break;
		case "LUNA":
			com.jtattoo.plaf.luna.LunaLookAndFeel.setTheme("Default");
			UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
			break;
		default:
			break;
		
		}
	}
}
