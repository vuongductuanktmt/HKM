package Server.HKM;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.drafts.Draft_10;


public class Connectclient {
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
}
}
