package Server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.mail.MessagingException;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import Database.HKM.Extend;
/* TimeZone
 * EST - -05:00
 * HST - -10:00
 * MST - -07:00
 * ACT - Australia/Darwin
 * AET - Australia/Sydney
 * AGT - America/Argentina/Buenos_Aires
 * ART - Africa/Cairo
 * AST - America/Anchorage
 * BET - America/Sao_Paulo
 * BST - Asia/Dhaka
 * CAT - Africa/Harare
 * CNT - America/St_Johns
 * CST - America/Chicago
 * CTT - Asia/Shanghai
 * EAT - Africa/Addis_Ababa
 * ECT - Europe/Paris
 * IET - America/Indiana/Indianapolis
 * IST - Asia/Kolkata
 * JST - Asia/Tokyo
 * MIT - Pacific/Apia
 * NET - Asia/Yerevan
 * NST - Pacific/Auckland
 * PLT - Asia/Karachi
 * PNT - America/Phoenix
 * PRT - America/Puerto_Rico
 * PST - America/Los_Angeles
 * SST - Pacific/Guadalcanal
 * VST - Asia/Ho_Chi_Minh
 */
public class server extends WebSocketServer {
	public server(int port) {
		super(new InetSocketAddress(port));
	}

	public server(InetSocketAddress address) {
		super(address);	
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		log("New connection from " + conn.getRemoteSocketAddress());
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		log("Connection from " + conn.getRemoteSocketAddress() + " is closed");
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		try {
		log(message + " from " + conn.getRemoteSocketAddress());
		String dataxxx = message;
		JSONObject obj = new JSONObject(dataxxx);
		String Token,dataxx = null;
		Extend Extendc =new Extend();
		Token = obj.getString("__TokenUser__");
		switch (obj.getString("__Action__")) {
		case "GetData":
			try {
				 dataxx = Extendc.GetAllData(Token);
			} catch (UnknownHostException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.send(dataxx);
			} catch (Exception e) {
				// TODO: handle exception
			}
			CloseConnection(conn);
			break;
		case "Register":
			try {
				 dataxx = Extendc.CreateUserClient(Token);
			} catch (UnknownHostException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				conn.send(dataxx);
			} catch (Exception e) {
				// TODO: handle exception
			}
			CloseConnection(conn);
			break;
		case "CreateHistoryUser":
			try {
				Extendc.CreateHistoryUserClient(dataxxx);
			} catch (UnknownHostException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	CloseConnection(conn);
			break;
		case "UpDate": //xxxx//
			conn.send("Function not yet available (\"-\") ");
		default:
			CloseConnection(conn);
			break;	
	        	}
		}
		catch (Exception e) { /* Close connection if message has wrong format */
			CloseConnection(conn);
		}
}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		log("error " + ex.getMessage());
	}

	public static void main(String[] args) throws InterruptedException , IOException {
		String host = "localhost";
		int port = 8081;
		
		if (args.length > 0) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		WebSocketServer server = new server(new InetSocketAddress(host, port));
		server.start();
		
		log("Server is running on port " + server.getPort());
		
		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while (true) {
			String in = sysin.readLine();
			if(in.equals("exit")) {
				server.stop();
				log("Server is stop");
				break;
			}
		}
	}
	public static void CloseConnection(WebSocket conn) {
	log("Disconnect with client." + conn.getRemoteSocketAddress());
	conn.close();
}
	private static void log(String message) {
		 Date date = new Date();
		 DateFormat firstFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a zzz");
	     TimeZone firstTime = TimeZone.getTimeZone("VST"	);
	     firstFormat.setTimeZone(firstTime);
		System.out.println(firstFormat.format(date) + ": " + message);
   }
	
}