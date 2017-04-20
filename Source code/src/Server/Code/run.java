package Server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

public class run {
	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
		JSONObject item = new JSONObject();
		item.put("__Action__", "Register");
		item.put("__Link__", "emty");
		item.put("__TokenUser__", "50e165bf6e9dcfe6c401f080660f5566607bfae010031ee545a97a1e531e79c64c8b31ea994dcf1064ea647b4fd2a1a3492f4e45db8db690449afc3339aa19f3");
	
			System.out.println( "Received from server:"+Connectclient.SendtoServer(item.toJSONString()));
	
	
	}
}
