package App.HKM;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.AggregationOutput;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.DistinctIterable;
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
	public static final String COLLECTION_NAME = "hosted";
	public MongoClient mongoClient;
	public MongoDB(String Collection) throws UnknownHostException, AddressException, MessagingException, InterruptedException {
		try {
			Logger mongoLogger = Logger.getLogger("org.mongodb.driver");
			mongoLogger.setLevel(Level.SEVERE);
			// Kết nối tới MongoDB.
			 this.mongoClient = getMongoClient();
			if (STATUS.equals("offline")) {
				db = this.mongoClient.getDatabase(COLLECTION_NAME);
			}
			this.Collection = db.getCollection(Collection);
			db.getCollection("TableWebInfo").createIndex(
					new Document("__Title__", "text").append("__HomePage__", "text").append("__Status__", "text"));
		} catch (Exception e) {
			new SendMail(e.getMessage());
			wait();
		}
	}

	/*
	 * Function getMongoClient_1 Là cách kết nối vào DB MongoDB không có bảo mật
	 * Sử dụng cách kết nối offline
	 */
	private static MongoClient getMongoClient_1() throws UnknownHostException {
		MongoClient mongoClient = new MongoClient(HOST, PORT);
		return mongoClient;
	}

	/*
	 * Function getMongoClient_2 Là cách kết nối vào DB MongoDB có bảo mật Sử
	 * dụng cách kết nối online
	 */
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

	/*
	 * Function ping lấy ra danh sách các database trên hệ thống
	 */
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
	public void dropDatabase(String database){
		this.mongoClient.dropDatabase(database);
	}
	/*
	 * Function Query dùng để lấy 1 danh sách các record cần tìm query là
	 * document query để tìm kiếm trên database ví dụ
	 * "new Document("name","Tuan")" OrderBy là sắp xếp tăng lên hoặc giảm xuống
	 * của một danh sách tìm được ví dụ như "new Document("age",-1)" sẽ xếp tăng
	 * dần của biến name Resoul sẽ là danh sách các name = "Tuan" mà sắp xếp
	 * tăng dần theo tuổi
	 */
	public List<Document> Query(Document query, Document OrderBy) {
		List<Document> documents = new ArrayList<>();
		try {
			MongoCursor<Document> cursor = this.Collection.find(query).sort(OrderBy).limit(10).iterator();
			if (cursor.hasNext()) {
				while (cursor.hasNext()) {
					documents.add(cursor.next());
				}
			} else {
				//System.out.println("No data!!");
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return documents;
	}

	/*
	 * Function UpdateRecord cho phép update giá trị nào đó thành 1 giá trị khác
	 * trong database queryFind là query cần tìm ví dụ như
	 * "new Document("name","Tuan")" queryReplace là query cần thay thế ví dụ
	 * như "new Document("name","DucTuan")"
	 */
	public void UpdateRecord(Document queryFind, Document queryReplace) {
		try {
			Bson updateOperationDocument = new Document("$set", queryReplace);
			this.Collection.updateOne(queryFind, updateOperationDocument);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void UpdateRecordAll(Document queryFind, Document queryReplace) {
		try {
			Bson updateOperationDocument = new Document("$set", queryReplace);
			this.Collection.updateMany(queryFind, updateOperationDocument);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/*
	 * Function lấy số dạng int từ database query là document query cần truyền
	 * vào ví dụ như "new Document("name","Tuan") key là String cần truyền vào
	 * để lấy key giá trị ở trong database ví dụ như key = "age" Result = 21
	 */
	public String GetOneRecordString(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getString(key);
		}
		return null;
	}

	/*
	 * Function lấy số dạng int từ database query là document query cần truyền
	 * vào ví dụ như "new Document("name","Tuan") key là String cần truyền vào
	 * để lấy key giá trị ở trong database ví dụ như key = "age" Result = 21
	 */
	public int GetOneRecordInt(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getInteger(key);
		}
		return 0;
	}
	/*
	 * Function lấy số dạng long từ database query là document query cần truyền
	 * vào ví dụ như "new Document("name","Tuan") key là String cần truyền vào
	 * để lấy key giá trị ở trong database ví dụ như key = "age" Result = 21
	 */

	public long GetOneRecordLong(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getLong(key);
		}
		return 0;
	}

	/*
	 * Function lấy số dạng double từ database query là document query cần
	 * truyền vào ví dụ như "new Document("name","Tuan") key là String cần
	 * truyền vào để lấy key giá trị ở trong database ví dụ như key = "height"
	 * Result = 1.65
	 */
	public double GetOneRecordDouble(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getDouble(key);
		}
		return 0.0;
	}

	/*
	 * Function lấy ngày từ database query là document query cần truyền vào ví
	 * dụ như "new Document("name","Tuan") key là String cần truyền vào để lấy
	 * key giá trị ở trong database ví dụ như key = "birthday" Result =
	 * 1996-09-19 00:00:00.000Z
	 */
	public Date GetOneRecordDate(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getDate(key);
		}
		return null;
	}

	/*
	 * Function lấy giá trị true false từ database query là document query cần
	 * truyền vào ví dụ như "new Document("name","Tuan") key là String cần
	 * truyền vào để lấy key giá trị ở trong database ví dụ như key = "handsome"
	 * Result = true
	 */
	public boolean GetOneRecordBoolean(Document query, String key) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		if (cursor.hasNext()) {
			return cursor.next().getBoolean(key);
		}
		return false;
	}

	/*
	 * Function CheckExistsRecord kiểm tra sự tồn tại của record trong hệ thống
	 * query là document query cần truyền vào ví dụ như
	 * "new Document("name","Tuan") Result sẽ là true nếu tồn tại false nếu
	 * không tồn tại
	 */
	public boolean CheckExistsRecord(Document query) {
		MongoCursor<Document> cursor = this.Collection.find(query).iterator();
		return cursor.hasNext();
	}

	/*
	 * Function CountRecords đếm số record có trong database qua query query là
	 * document query cần truyền vào ví dụ như "new Document("name","Tuan")
	 * Result sẽ là số record trong database
	 */
	public long CountRecords(Document query) {
		return this.Collection.count(query);
	}

	/*
	 * Function RemoveRecord xóa 1 record đầu tiên suất hiện trên database query
	 * là document query cần truyền vào ví dụ như "new Document("name","Tuan")
	 */
	public void RemoveRecordOne(Document query) {
		this.Collection.deleteOne(query);
	}

	/*
	 * Function RemoveRecord hết các record suất hiện trên database query là
	 * document query cần truyền vào ví dụ như "new Document("name","Tuan")
	 */
	public void RemoveRecordAll(Document query) {
		this.Collection.deleteMany(query);
	}
	public void DistinctCount(Document query, String key,String class_){
		
		
	}
	/*
	 * Fucntion Pagination dùng để phân trang OrderBy là sắp xếp tăng lên hoặc
	 * giảm xuống của một danh sách tìm được ví dụ như "new Document("name",-1)"
	 * sẽ xếp tăng dần của biến name Status là trạng thái của product là Event
	 * hay Selling hay Promotion Search là từ khóa tìm kiếm pageNumber số trang
	 * hiện tại pageSize là số record của 1 trang
	 */
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

	/*
	 * Function lấy giá trị trả về là long số trang tối cua của query tìm kiếm
	 * trên database query là document query để tìm kiếm trên database ví dụ
	 * "new Document("name","Tuan")" PageSize là số record của 1 trang dùng cho
	 * phân trang ví dụ PageSize = 10 thì 1 trang sẽ có 10 record Search là từ
	 * khóa tìm kiếm dùng cho tìm kiếm ví dụ như Search = "21" Result trả về là
	 * 1 chuỗi json gồm tất cả các thuộc tính cha con và anh chị của nó
	 */
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

	/*
	 * Function lấy ra một mảng các sản phẩm query là document query cần truyền
	 * vào ví dụ như "new Document("__Status__","Promotion") Result sẽ là một
	 * mảng các sản phẩm Promotion
	 */
	public ArrayList<Products> GetListArrayProducts(Document query) {
		ArrayList<Products> list = new ArrayList<Products>();
		MongoCursor<Document> cursor;
		cursor = this.Collection.find(query).sort(new Document("__Title__", 1)).iterator();
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

	/*
	 * Function lấy ra một mảng các sản phẩm query là document query cần truyền
	 * vào ví dụ như "new Document("__Status__","Event") Result sẽ là một mảng
	 * các sản phẩm Event
	 */
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

	/*
	 * Function GetListArrayCategoryParents lấy ra danh sách các category parent
	 * query là document query cần truyền vào ví dụ như
	 * "new Document("__CategoryParentName__","Chung")
	 */
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
