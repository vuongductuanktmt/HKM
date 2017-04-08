package App.HKM;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoUtils {

	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String STATUS = "offline";
	private DB db = null;
	public DBCollection Collection;
	private static final String USERNAME = "mgdb";
	private static final String PASSWORD = "1234";
	public static final String COLLECTION_NAME = "hosted";
	@SuppressWarnings("deprecation")
	MongoUtils(String DB_NAME, String collection) throws UnknownHostException {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		// Kết nối tới MongoDB.
		MongoClient mongoClient = getMongoClient(STATUS);
		// Kết nối tới Database
		// (Không nhất thiết DB này phải tồn tại sẵn
		// nó sẽ được tự động tạo ra nếu chưa tồn tại).
		db = mongoClient.getDB(DB_NAME);
		// Lấy ra Collection với tên Department.
		// Không nhất thiết Collection này phải tồn tại trong DB.
		Collection = db.getCollection(collection);
	}

	@SuppressWarnings("deprecation")
	MongoUtils(String DB_NAME) throws UnknownHostException {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		// Kết nối tới MongoDB.
		MongoClient mongoClient = getMongoClient(STATUS);
		// Kết nối tới Database
		// (Không nhất thiết DB này phải tồn tại sẵn
		// nó sẽ được tự động tạo ra nếu chưa tồn tại).
		db = mongoClient.getDB(DB_NAME);
	}

	// Cách kết nối vào MongoDB không bắt buộc bảo mật.
	private static MongoClient getMongoClient_1() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		return mongoClient;
	}

	// Cách kết nối vào DB MongoDB có bảo mật.
	private static MongoClient getMongoClient_2() throws UnknownHostException {
		MongoCredential credential = MongoCredential.createMongoCRCredential(USERNAME, MyConstants.DB_NAME,
				PASSWORD.toCharArray());

		MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT), Arrays.asList(credential));
		return mongoClient;
	}

	public static MongoClient getMongoClient(String Status) throws UnknownHostException {
		switch (Status) {
		case "offline":
			// Kết nối vào MongoDB không bắt buộc bảo mật.
			return getMongoClient_1();
		default:
			// Bạn có thể thay thế bởi getMongoClient_2()
			// trong trường hợp kết nối vào MongoDB có bảo mật.
			return getMongoClient_2();
		}
	}

	static void ping() throws UnknownHostException {
		MongoClient mongoClient = getMongoClient(STATUS);

		System.out.println("List all DB:");

		// Danh sách các DB Names.
		@SuppressWarnings("deprecation")
		List<String> dbNames = mongoClient.getDatabaseNames();
		for (String dbName : dbNames) {
			System.out.println("+ DB Name: " + dbName);
		}

		System.out.println("Done!");
	}

	public List<DBObject> Query_Table(String table){
		List<DBObject> docnument = new ArrayList<DBObject>();
		
		DBCursor cursor = this.Collection.find(new BasicDBObject(), new BasicDBObject(table,""));
		while (cursor.hasNext()) {
			docnument.add(cursor.next());
		}
		return docnument;
	}
	public boolean 	EmptyCollection(String collection) {
		return this.db.collectionExists(collection);
	}

	public void setCollection(String collection) {
		this.Collection = this.db.getCollection(collection);
	}
	public boolean CheckExistsRecord(BasicDBObject query){
		DBCursor cursor = this.Collection.find(query);
		return cursor.hasNext();
	}
	public List<DBObject> Query(BasicDBObject query){
		List<DBObject> docnument = new ArrayList<DBObject>();
		DBCursor cursor = this.Collection.find(query);
		while (cursor.hasNext()) {
			docnument.add(cursor.next());
		}
		return docnument;
	}
	
	public class MyConstants {
		// Có thể Database này không tồn tại trên MongoDB của bạn.
		// Nhưng nó sẽ được tự động tạo ra.
		// (Bạn không cần phải thay đổi trường này).
		public static final String DB_NAME = "MyStudyDB";
	}
}
