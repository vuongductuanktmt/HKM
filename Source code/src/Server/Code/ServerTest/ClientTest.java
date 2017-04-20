package Server;

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
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

public class ClientTest extends WebSocketClient {

	public ClientTest(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public ClientTest(URI serverURI) {
		super(serverURI);
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		log( "Opened connection");
		// if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage( String message ) {
		System.out.println( "received: " + message );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		String uri = "ws://192.168.14.128:9999";
		if (args.length > 0) {
			uri = args[0];
		}
		ClientTest client = new ClientTest(new URI(uri), new Draft_10());
		client.connect();
		
		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while (true) {
			String in = sysin.readLine();
			if (in.equals("exit")) {
				client.close();
				log("Closed connection");
				break;
			}
			else {
				client.send(in);
			}
		}
	}
	
	private static void log(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
   }
}
