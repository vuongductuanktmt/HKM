package Database.HKM;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class database {
	private MongoClient client;
	public MongoClientURI uri;
	public  MongoCollection <Document> coll;
	public  MongoDatabase db;
	private MongoClient mongoClient;
	public static String phuongthuc = "offline";
	public database(String collc,String phuongthuc) {
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE);
		if(phuongthuc=="online"){
			uri = new MongoClientURI("mongodb://admin:123456@ds056979.mlab.com:56979/hosted");
			client = new MongoClient(uri);
			db = client.getDatabase(uri.getDatabase());
		}else{
			 mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			  db = mongoClient.getDatabase("hosted");
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
		coll = db.getCollection(collc);
		
	}

	public ArrayList<data>laydulieu(String website) {
		ArrayList<data> list = new ArrayList<data>();
		Document whereQuery = new Document("website", website);
		String web;
		MongoCursor<Document> cursor;
		cursor = (website == "all") ? this.coll.find().iterator() : this.coll.find(whereQuery).iterator();
		try {
			if (cursor.hasNext()) {
				web = cursor.next().getString("website");
				data p;
				while (cursor.hasNext()) {
					Document temp = cursor.next();
					p = new data(web, temp.getString("title"), temp.getString("link_even"),
							temp.getString("link_image"));
					list.add(p);
				}
			} else {
				System.out.println("Không lấy được dữ liệu");
			}
		} finally {
			cursor.close();
		}
		return list;
	}
}
