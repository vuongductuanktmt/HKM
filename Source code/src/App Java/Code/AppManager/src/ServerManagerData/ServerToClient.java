package ServerManagerData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

import App.HKM.Data.Category;
import App.HKM.Data.CategoryParents;
import App.HKM.Data.Products;

public class ServerToClient {
	private String __Title__;
	private String __Href__;
	private String __HomePage__;
	private String __LinkImage__;
	private long __ViewCount__;
	private long __LoveCount__;
	private java.util.Date __DateInsert__;
	private boolean __Delete__;
	private String __CurrentPrice__;
	private String __OldPrice__;
	private String __Status__;
	private DefaultListModel<Category> __Category__ = new DefaultListModel<>();
	public ServerToClient(String __Title__, String __Href__, String __HomePage__, String __LinkImage__,
			long __ViewCount__, long __LoveCount__, Date __DateInsert__, boolean __Delete__, String __CurrentPrice__,
			String __OldPrice__, String __Status__, DefaultListModel<Category> __Category__) {
		super();
		this.__Title__ = __Title__;
		this.__Href__ = __Href__;
		this.__HomePage__ = __HomePage__;
		this.__LinkImage__ = __LinkImage__;
		this.__ViewCount__ = __ViewCount__;
		this.__LoveCount__ = __LoveCount__;
		this.__DateInsert__ = __DateInsert__;
		this.__Delete__ = __Delete__;
		this.__CurrentPrice__ = __CurrentPrice__;
		this.__OldPrice__ = __OldPrice__;
		this.__Status__ = __Status__;
		this.__Category__ = __Category__;
	}
	public static Document getDataProducts(String data){
		Document product = Document.parse(data);
		return product;
	}
	@SuppressWarnings("unchecked")
	public static List<Document> getDataListProducts(String data){
		Document product = Document.parse(data);
		List<Document> courses = (List<Document>) product.get("data");
		return courses;
	}
	public static ArrayList<Products> GetListArrayProducts(String data) {
		ArrayList<Products> list = new ArrayList<Products>();
		List<Document> documents = new ArrayList<>();
		documents = getDataListProducts(data);
				Products p;
				for (Document document : documents) {
					p = new Products(document.getString("__Title__"), document.getString("__LinkTitle__"),
							document.getString("__HomePage__"), document.getString("__LinkImage__"),
							document.getLong("__ViewCount__"), document.getLong("__ViewCount__"),
							document.getString("__CurrentPrice__"), document.getString("__OldPrice__"),
							document.getDate("__DateInsert__"), document.getBoolean("__Delete__"),
							document.getString("__Status__"), null);
					list.add(p);
				}
					
		return list;
	}
	public static ArrayList<CategoryParents> GetListArrayCategoryParents(String data) {
		ArrayList<CategoryParents> list = new ArrayList<CategoryParents>();
		List<Document> documents = new ArrayList<>();
		documents = getDataListProducts(data);
				CategoryParents p;
				for (Document document : documents) {
					p = new CategoryParents(document.getString("__CategoryParentName__"));
					list.add(p);
				}
					
		return list;
	}
	public String get__Title__() {
		return __Title__;
	}
	public void set__Title__(String __Title__) {
		this.__Title__ = __Title__;
	}
	public String get__Href__() {
		return __Href__;
	}
	public void set__Href__(String __Href__) {
		this.__Href__ = __Href__;
	}
	public String get__HomePage__() {
		return __HomePage__;
	}
	public void set__HomePage__(String __HomePage__) {
		this.__HomePage__ = __HomePage__;
	}
	public String get__LinkImage__() {
		return __LinkImage__;
	}
	public void set__LinkImage__(String __LinkImage__) {
		this.__LinkImage__ = __LinkImage__;
	}
	public long get__ViewCount__() {
		return __ViewCount__;
	}
	public void set__ViewCount__(long __ViewCount__) {
		this.__ViewCount__ = __ViewCount__;
	}
	public long get__LoveCount__() {
		return __LoveCount__;
	}
	public void set__LoveCount__(long __LoveCount__) {
		this.__LoveCount__ = __LoveCount__;
	}
	public java.util.Date get__DateInsert__() {
		return __DateInsert__;
	}
	public void set__DateInsert__(java.util.Date __DateInsert__) {
		this.__DateInsert__ = __DateInsert__;
	}
	public boolean is__Delete__() {
		return __Delete__;
	}
	public void set__Delete__(boolean __Delete__) {
		this.__Delete__ = __Delete__;
	}
	public String get__CurrentPrice__() {
		return __CurrentPrice__;
	}
	public void set__CurrentPrice__(String __CurrentPrice__) {
		this.__CurrentPrice__ = __CurrentPrice__;
	}
	public String get__OldPrice__() {
		return __OldPrice__;
	}
	public void set__OldPrice__(String __OldPrice__) {
		this.__OldPrice__ = __OldPrice__;
	}
	public String get__Status__() {
		return __Status__;
	}
	public void set__Status__(String __Status__) {
		this.__Status__ = __Status__;
	}
	public DefaultListModel<Category> get__Category__() {
		return __Category__;
	}
	public void set__Category__(DefaultListModel<Category> __Category__) {
		this.__Category__ = __Category__;
	}
}
