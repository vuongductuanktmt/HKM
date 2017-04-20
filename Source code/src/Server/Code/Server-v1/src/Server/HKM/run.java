package Server.HKM;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.bson.Document;

import Crawler.ThucThi;

public class run {
	public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, AddressException, MessagingException {
	MongoDB data = new MongoDB("TableWebInfo");
	System.out.println(data.Pagination(new Document(), "Event", "Search...", 2, 5))	;
	}
}
