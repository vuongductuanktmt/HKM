package Database.HKM;

import java.util.ArrayList;
import java.util.List;
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
	public  MongoCollection <Document> Collection;
	public  MongoDatabase db;
	private MongoClient mongoClient;
	public static String phuongthuc = "offline";
	public database(String Collection,String phuongthuc) {
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
		this.Collection = db.getCollection(Collection);
		
	}

	public List<Document> Query_MongoDB(MongoCursor<Document> cursor) {//this.coll.find(whereQuery).iterator();Document whereQuery = new Document("website", website);
		List<Document> documents = new ArrayList<>();
		try {
			if (cursor.hasNext()) {
				while(cursor.hasNext()){
					documents.add(cursor.next());
				}
				System.out.println(documents);
			} else {
				System.out.println("No data!!");
			}
		} finally {
			cursor.close();
		}
		return documents;
	}
	public boolean CheckExistsRecord(Document query){
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();;
		return cursor.hasNext();
	}
    public void fullTextSearch(String query, boolean caseSensitive, boolean diacriticSensitive) {
        try {
            MongoCursor<Document> cursor = null;
            cursor = this.Collection.find(new Document("$text", new Document("$search", query).append("$caseSensitive", new Boolean(caseSensitive)).append("$diacriticSensitive", new Boolean(diacriticSensitive)))).iterator();
 
            while (cursor.hasNext()) {
                Document article = cursor.next();
                System.out.println(article);
            }
 
            cursor.close();
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }
 
    }
	
}
