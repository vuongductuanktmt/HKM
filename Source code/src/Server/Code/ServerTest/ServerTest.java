package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ServerTest extends WebSocketServer {
	public ServerTest(int port) {
		super(new InetSocketAddress(port));
	}

	public ServerTest(InetSocketAddress address) {
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
		log(message + " from " + conn.getRemoteSocketAddress());
		conn.send("Sent");
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		log("error " + ex.getMessage());
	}

	public static void main(String[] args) throws InterruptedException , IOException {
		String host = "localhost";
		int port = 9999;
		
		if (args.length > 0) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		WebSocketServer server = new ServerTest(new InetSocketAddress(host, port));
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
	
	private static void log(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
   }
}
