package App.HKM.Extend;

import java.awt.Cursor;
import java.io.IOException;
import java.util.Random;
import java.util.TimeZone;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

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
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		boolean temp = false;
		String[] hosttemps = { "lo" };
		while (interfaces.hasMoreElements()) {

			NetworkInterface networkInterface = interfaces.nextElement();

			System.out.println("Network Interface Name : [" + networkInterface.getDisplayName() + "]");
			for (String hosttemp : hosttemps) {
				if (!(networkInterface.getDisplayName().trim().equals(hosttemp))) {
					temp = networkInterface.isUp();
				}
			}
			System.out.println("Is It connected? : [" + networkInterface.isUp() + "]");
			for (InterfaceAddress i : networkInterface.getInterfaceAddresses()) {

				System.out.println("Host Name : " + i.getAddress().getCanonicalHostName());

				System.out.println("Host Address : " + i.getAddress().getHostAddress());

			}
			System.out.println("----------------------");
		}
		return temp;
	}

	public void ChangeTheme(String theme) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		switch (theme) {
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
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
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

	public String Token(String message) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");

			md.update(message.getBytes());
			byte[] mb = md.digest();
			String out = "";
			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				s = s.substring(s.length() - 2);
				out += s;
			}
			System.out.println(out.length());
			System.out.println("CRYPTO: " + out);
			return out;

		} catch (NoSuchAlgorithmException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return null;
	}
	public static Date getTime() throws ParseException {
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", new Locale("vi", "VN"));
			Date today = new Date(System.currentTimeMillis());
			String gettime = timeFormat.format(today.getTime());
			timeFormat.setTimeZone(TimeZone.getTimeZone("+7GMT"));
			return timeFormat.parse(gettime);
		} catch (Exception e) {
			System.out.println("Can't get info Date");
		}
		return null;

	}
}
