package Database.HKM;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import App.HKM.Data.Event;
import App.HKM.Data.Products;

public class MongoDB {
	public MongoCollection<Document> Collection;
	public static MongoDatabase db;
	private static final String HOST = "localhost";
	private static final int PORT = 27017;
	private static final String STATUS = "offline";
	private static final String USERNAME = "mgdb";
	private static final String PASSWORD = "1234";
	public static final String COLLECTION_NAME = "hosted";

	public MongoDB(String Collection) throws UnknownHostException {
		Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
		mongoLogger.setLevel(Level.SEVERE);
		// Kết nối tới MongoDB.
		MongoClient mongoClient = getMongoClient();
		// Kết nối tới Database
		// (Không nhất thiết DB này phải tồn tại sẵn
		// nó sẽ được tự động tạo ra nếu chưa tồn tại).
		if(STATUS.equals("offline")){db = mongoClient.getDatabase(COLLECTION_NAME);}
		this.Collection = db.getCollection(Collection);
		db.getCollection("TableWebInfo").createIndex(
				new Document("__Title__", "text").append("__HomePage__", "text").append("__Status__", "text"));
		db.getCollection("CategoryChild")
				.createIndex(new Document("__CategoryChildName__", "text").append("__CategoryChildLink__", "text"));

	}

	private static MongoClient getMongoClient_1() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		return mongoClient;
	}

	// Cách kết nối vào DB MongoDB có bảo mật.
	private static MongoClient getMongoClient_2() throws UnknownHostException {
		MongoClientURI uri = new MongoClientURI("mongodb://admin:123456@ds056979.mlab.com:56979/hosted");
		MongoClient mongoClient = new MongoClient(uri);
		db = mongoClient.getDatabase(uri.getDatabase());
		return mongoClient;
	}

	public static MongoClient getMongoClient() throws UnknownHostException {
		switch (STATUS) {
		case "offline":
			// Kết nối vào MongoDB không bắt buộc bảo mật.
			return getMongoClient_1();
		default:
			// Bạn có thể thay thế bởi getMongoClient_2()
			// trong trường hợp kết nối vào MongoDB có bảo mật.
			return getMongoClient_2();
		}
	}

	public void ping() throws UnknownHostException {
		MongoClient mongoClient = getMongoClient();
		System.out.println("List all DB:");
		// Danh sách các DB Names.
		@SuppressWarnings("deprecation")
		List<String> dbNames = mongoClient.getDatabaseNames();
		for (String dbName : dbNames) {
			System.out.println("+ DB Name: " + dbName);
		}

		System.out.println("Done!");
	}

	public List<Document> Query(Document query, Document OrderBy) {// this.coll.find(whereQuery).iterator();Document
																	// whereQuery
																	// = new
																	// Document("website",
																	// website);
		List<Document> documents = new ArrayList<>();
		try {
			MongoCursor<Document> cursor = this.Collection.find(query).sort(OrderBy).iterator();
			if (cursor.hasNext()) {
				while (cursor.hasNext()) {
					documents.add(cursor.next());
				}
			} else {
				System.out.println("No data!!");
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documents;
	}

	public void UpdateRecord(Document queryFind, Document queryReplace) {
		try {
			Bson updateOperationDocument = new Document("$set", queryReplace);
			this.Collection.updateOne(queryFind, updateOperationDocument);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<Document> Pagination(Document OrderBy, String Status, String Search, int pageNumber, int pageSize) {
		List<Document> documents = new ArrayList<>();
		MongoCursor<Document> cursor = null;
		try {

			if (!Search.equals("Search...")) {
				cursor = this.Collection
						.find(new Document("$text",
								new Document("$search", Search).append("$caseSensitive", new Boolean(false))
										.append("$diacriticSensitive", new Boolean(false))).append("__Status__", Status)
												.append("__Delete__", false))
						.sort(OrderBy).skip(pageSize * (pageNumber - 1)).limit(pageSize).iterator();
			} else {
				cursor = this.Collection.find(new Document("__Status__", Status).append("__Delete__", false))
						.sort(OrderBy).skip(pageSize * (pageNumber - 1)).limit(pageSize).iterator();
			}
			if (Search.equals("*")) {
				cursor = this.Collection.find(new Document("__Status__", Status).append("__Delete__", false))
						.sort(OrderBy).skip(pageSize * (pageNumber - 1)).limit(pageSize).iterator();
			}
			if (cursor.hasNext()) {
				while (cursor.hasNext()) {
					documents.add(cursor.next());
				}
				System.out.println(documents);
			} else {
				System.out.println("No data!!");
				return (new ArrayList<>());
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documents;
	}

	public long PageMax(Document query, long PageSize, String Search) {
		Document temp = new Document();
		if (!Search.equals("Search...")) {
			temp = new Document("$text",
					new Document("$search", Search).append("$caseSensitive", new Boolean(false))
							.append("$diacriticSensitive", new Boolean(false)))
									.append("__Status__", query.getString("__Status__")).append("__Delete__", false);
		} else {
			temp = query;
		}
		if (Search.equals("*")) {
			temp = query;
		}
		double tempreal = (double) this.Collection.count(temp) / (double) PageSize;
		long templong = this.Collection.count(temp) / PageSize;
		return (tempreal > (double) templong) ? templong + 1 : templong;
	}

	public String GetOneRecordString(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getString(key);
		}
		return null;
	}

	public int GetOneRecordInt(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getInteger(key);
		}
		return 0;
	}

	public boolean CheckExistsRecord(Document query) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		return cursor.hasNext();
	}

	public long CountRecords(Document query) {
		return this.Collection.count(query);
	}

	public void RemoveRecord(Document query) {
		this.Collection.deleteOne(query);
	}

	public void fullTextSearch(String query, boolean caseSensitive, boolean diacriticSensitive) {
		try {
			MongoCursor<Document> cursor = null;
			cursor = this.Collection
					.find(new Document("$text",
							new Document("$search", query).append("$caseSensitive", new Boolean(caseSensitive))
									.append("$diacriticSensitive", new Boolean(diacriticSensitive))))
					.sort(new Document()).skip(1 * (1 - 1)).limit(2).iterator();

			while (cursor.hasNext()) {
				Document article = cursor.next();
				System.out.println(article);
			}

			cursor.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Products> GetListArrayProducts(Document query) {
		ArrayList<Products> list = new ArrayList<Products>();
		MongoCursor<Document> cursor;
		cursor = this.Collection.find(query).sort(new Document("__Status__", 1)).iterator();
		try {
			if (cursor.hasNext()) {
				Products p;
				while (cursor.hasNext()) {
					Document document = cursor.next();
					p = new Products(document.getString("__Title__"), document.getString("__LinkTitle__"),
							document.getString("__HomePage__"), document.getString("__LinkImage__"),
							document.getLong("__ViewCount__"), document.getLong("__ViewCount__"),
							document.getString("__CurrentPrice__"), document.getString("__OldPrice__"),
							document.getDate("__DateInsert__"), document.getBoolean("__Delete__"),
							document.getString("__Status__"), null);
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

	public ArrayList<Event> GetListArrayEvent(Document query) {
		ArrayList<Event> list = new ArrayList<Event>();
		MongoCursor<Document> cursor;
		cursor = this.Collection.find(query).iterator();
		try {
			if (cursor.hasNext()) {
				Event p;
				while (cursor.hasNext()) {
					Document document = cursor.next();
					p = new Event(document.getString("__Title__"), document.getString("__LinkTitle__"),
							document.getString("__HomePage__"), document.getString("__LinkImage__"),
							document.getLong("__ViewCount__"), document.getLong("__ViewCount__"),
							document.getDate("__DateInsert"), document.getBoolean("__Delete__"));
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

	public ArrayList<App.HKM.Data.CategoryParents> GetListArrayCategoryParents(Document query) {
		ArrayList<App.HKM.Data.CategoryParents> list = new ArrayList<App.HKM.Data.CategoryParents>();
		MongoCursor<Document> cursor;
		cursor = this.Collection.find(query).iterator();
		try {
			if (cursor.hasNext()) {
				App.HKM.Data.CategoryParents p;
				while (cursor.hasNext()) {
					Document document = cursor.next();
					p = new App.HKM.Data.CategoryParents(document.getString("__CategoryParentName__"));
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
