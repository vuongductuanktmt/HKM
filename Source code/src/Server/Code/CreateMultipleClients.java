package Server;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.drafts.Draft_10;
import org.json.simple.JSONObject;

import Database.HKM.Extend;

public class CreateMultipleClients {
	public static int i;
	public  static String   SendtoServer(String Dataxx )throws URISyntaxException, IOException {
		String uri = "ws://104.198.199.19:9999";
		//String uri = "ws://localhost:9999";
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
	public static void main(String[] args) throws InterruptedException {
//		JSONObject request = new JSONObject();
//		request.put("__Request__", "get_all_data");
//		request.put("__User__","57e165bf6e9dcfe6c401f080660f5566607bfae010031ee545a97a1e531e79c64c8b31ea994dcf1064ea647b4fd2a1a3492f4e45db8db690449afc3339aa19f3");
//		request.put("__Password__","");
//		request.put("__Security__","");
//		request.put("__PageSize__","");
//		request.put("__PageNumber__","");
//		request.put("__Var__", "");
//		request.put("__Value__","");
//		request.put("__Condition__","");
//		request.put("__Authorities__","user");
		
		JSONObject item = new JSONObject();
		item.put("__Action__", "Register");
		item.put("__Link__", "emty");
		item.put("__TokenUser__", "50e165bf6e9dcfe6c401f080660f5566607bfae010031ee545a97a1e531e79c64c8b31ea994dcf1064ea647b4fd2a1a3492f4e45db8db690449afc3339aa19f3");
	
			
			
		for( i = 0 ; i < 400 ; i++){
			Thread thread_count_user = new Thread(){
				public void run() {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						try {
							System.out.println( "Received from server:"+Client.SendtoServer(item.toJSONString()));	
					//		System.out.println( "CLient" + i + "Received from server:"+SendtoServer(request.toJSONString()));
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	
					}
			};
			thread_count_user.start();
	}
	}
}
