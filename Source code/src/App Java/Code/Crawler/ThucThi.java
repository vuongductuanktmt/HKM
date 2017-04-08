package Database.HKM;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import App.HKM.Extend.Extend;

public class ThucThi implements Runnable { // Thực thi đa luồng
	public String Website;

	public ThucThi(String website) {
		this.Website = website;
	}

	@Override
	public void run() {
		try {
			switch (this.Website) {
			case "sendo":
				jsoups.trandata("sendo");
				break;
			case "lazada":
				jsoups.trandata("lazada");
				break;
			case "tiki":
				jsoups.trandata("tiki");
				break;
			case "fptshop":
				jsoups.trandata("fptshop");
				break;
			case "dienmayxanh":
				jsoups.trandata("dienmayxanh");
				break;
			case "vatgia":
				jsoups.trandata("vatgia");
				break;
			case "adayroi":
				jsoups.trandata("adayroi");
				break;
			default:
				break;
			}
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException, UnknownHostException, ParseException {
		System.out.println("Đợi lấy dữ liệu...");
		String[] WebName = { "tiki", "lazada", "vatgia", "sendo", "adayroi", "dienmayxanh", "fptshop" };
		MongoDB data = new MongoDB("TableWebInfo");
		MongoDB data_error = new MongoDB("Error");
		MongoDB data_extend = new MongoDB("Extend");
		MongoDB data_category = new MongoDB("Extend");
		MongoDB data_user = new MongoDB("User");
		MongoDB data_category_parent = new MongoDB("CategoryParents");
		Extend extend = new Extend();
		long date = jsoups.getTime().getTime();
		List<Document> documents = new ArrayList<Document>();
		documents = data.Query(new Document(), new Document());
		/*
		 * Log user:
		 * InfoLog : 	lấy thông tin thay đổi của người dùng
		 * DateChange :	lấy ngày thay đổi của người dùng
		 * UserInfo :	tên user
		 * LastLogin  : lần truy cập gần nhất
		 * 
		 */
		/*
		 * Custom CategoryChild include:
		 * + LinkProduct: The LinkUrls
		 * Custom User include:
		 * + Log user
		 */
		/*
		 * CategoryParents:
		 * + Add
		 * + Edit
		 * + Delete
		 */
		/*
		 * CategoryChild:
		 * + liên kết giữa categorychild với categoryparents
		 */
		int j = 0;
		if(data_category_parent.CountRecords(new Document())==0){
			data_category_parent.Collection.insertOne(new Document("__CategoryParentName__","Chung"));
		}
		if(data_extend.CountRecords(new Document())==0){
			data_extend.Collection.insertOne(new Document("__Name__", "DateDelete").append("__Value__", 7)
					.append("__Note__", "Cho phép thay đổi Số ngày làm mới sản phẩm"));
			data_extend.Collection.insertOne(new Document("__Name__", "Theme").append("__Value__", "METAL")
					.append("__Note__", "Cho phép thay đổi theme"));
			data_extend.Collection.insertOne(new Document("__Name__", "Image").append("__Value__", "false")
					.append("__Note__", "Cho phép load image hay không"));
			data_extend.Collection.insertOne(new Document("__Name__", "PageSize").append("__Value__", 10)
					.append("__Note__", "Cho phép thay đổi số record của 1 trang"));
			data_user.Collection.insertOne(new Document("__User__", "admin").append("__Password__", extend.MD5("admin123")));
		}
		for (Document document : documents) {
			long date1 = ((java.util.Date) document.get("__DateInsert__")).getTime();
			if (Math.abs((date - date1) / (1000 * 60 * 60 * 24)) >= data_extend
					.GetOneRecordInt(new Document("__Name__", "DateDelete"), "__Value__")) {
				j++;
				data.RemoveRecord(new Document("__LinkTilte__", document.get("__LinkTilte__")));
				data_category.RemoveRecord(new Document("__LinkTilte__", document.get("__LinkTilte__")));
			}
		}
		if (j > 0) {
			data_error.Collection.insertOne(new Document("__Title__", "By System")
					.append("__Content__", "Xóa thành công " + j + " vào lúc " + jsoups.getTime() + "")
					.append("__Date__", jsoups.getTime()));
		}
		System.out.println();
		for (int i = 0; i < WebName.length; i++) {
			new Thread(new ThucThi(WebName[i])).start();
		}
	}
}