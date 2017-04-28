package Server.HKM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
public class Client extends WebSocketClient {
	public String message;

	public Client(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public Client(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		log("Opened connection");
		// if you plan to refuse connection based on ip or httpfields overload:
		// onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage(String message) {
		this.message = message;
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// The codecodes are documented in class
		// org.java_websocket.framing.CloseFrame
		System.out.println("Connection closed by " + (remote ? "Server" : "Us"));
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	public String getMessage() {
		return this.message;
	}

	public  static String   SendtoServer(String Dataxx )throws URISyntaxException, IOException {
		String uri = "ws://104.198.199.19:9999";
		Client client = new Client(new URI(uri), new Draft_10());
		client.connect();
		//BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while (true) {
		//	String in = sysin.readLine();
			
			if (Dataxx.equals("exit")) {
				client.close();
				System.out.println("Closed connection");
				break;
			}
			else {
				try {
					 client.send(Dataxx);
					 break;
				} catch (Exception e) {
					
				}
			}
		}    
	while (true)
	{	     try {
		Thread.sleep(1);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			if(client.getMessage()!=null){
			//System.out.println(client.getMessage());
			return client.getMessage();
			}
			
	}

	private static void log(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
	}
}