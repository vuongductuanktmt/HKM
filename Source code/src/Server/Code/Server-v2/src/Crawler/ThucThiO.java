package Crawler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.bson.Document;

import Server.HKM.Extend;
import Server.HKM.MongoDB;

public class ThucThiO implements Runnable { // Thực thi đa luồng
	public String Website;

	public ThucThiO() {
	}

	public ThucThiO(String website) {
		this.Website = website;
	}

	@Override
	public void run() {
		try {
			switch (this.Website) {
			case "sendo":
				jsoups.trandata("sendo");
				new ThucThiO(this.Website);
				break;
			case "lazada":
				jsoups.trandata("lazada");
				new ThucThiO(this.Website);
				break;
			case "tiki":
				jsoups.trandata("tiki");
				new ThucThiO(this.Website);
				break;
			case "fptshop":
				jsoups.trandata("fptshop");
				new ThucThiO(this.Website);
				break;
			case "dienmayxanh":
				jsoups.trandata("dienmayxanh");
				new ThucThiO(this.Website);
				break;
			case "vatgia":
				jsoups.trandata("vatgia");
				new ThucThiO(this.Website);
				break;
			case "adayroi":
				jsoups.trandata("adayroi");
				new ThucThiO(this.Website);
				break;
			default:
				break;
			}
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
			throws InterruptedException, UnknownHostException, ParseException, AddressException, MessagingException {
		MongoDB data = new MongoDB("TableWebInfo");
		MongoDB data_error = new MongoDB("Error");
		MongoDB data_extend = new MongoDB("Extend");
		MongoDB data_user = new MongoDB("User");
		MongoDB data_category_parent = new MongoDB("CategoryParents");
		Extend extend = new Extend();
		System.out.println("Đợi lấy dữ liệu...");
		String[] WebName = { "tiki", "lazada", "vatgia", "sendo", "adayroi", "dienmayxanh", "fptshop" };

		Thread myThreads[] = new Thread[WebName.length];
		new Thread(new ThucThiO("ConfigDelete"));
		for (int i = 0; i < WebName.length; i++) {
			myThreads[i] = new Thread(new ThucThiO(WebName[i]));
			myThreads[i].start();
			Thread.sleep(100);
		}
		while (true) {
			long date = jsoups.getTime().getTime();
			List<Document> documents = new ArrayList<Document>();
			documents = data.Query(new Document(), new Document());
			int j = 0;
			if (data_category_parent.CountRecords(new Document()) == 0) {
				data_category_parent.Collection.insertOne(new Document("__CategoryParentName__", "Chung"));
			}
			if (data_extend.CountRecords(new Document()) == 0) {
				data_extend.Collection.insertOne(new Document("__Name__", "DateDelete").append("__Value__", 7)
						.append("__Note__", "Cho phép thay đổi Số ngày làm mới sản phẩm"));
				data_user.Collection
						.insertOne(new Document("__User__", "admin").append("__Password__", extend.Token("admin123"))
								.append("__Status__", true).append("__IP__", "0.0.0.0").append("__LastLogin__", jsoups.getTime()));
			}
			for (Document document : documents) {
				long date1 = ((java.util.Date) document.get("__DateInsert__")).getTime();
				if (Math.abs((date - date1) / (1000 * 60 * 60 * 24)) >= data_extend
						.GetOneRecordInt(new Document("__Name__", "DateDelete"), "__Value__")) {
					j++;
					data.RemoveRecordOne(new Document("__LinkTilte__", document.get("__LinkTilte__")));
				}
			}
			if (j > 0) {
				data_error.Collection.insertOne(new Document("__Title__", "By System")
						.append("__Content__", "Xóa thành công " + j + " vào lúc " + jsoups.getTime() + "")
						.append("__Date__", jsoups.getTime()));
			}
			for (int i = 0; i < WebName.length; i++) {
				try {
					if (!myThreads[i].isAlive()) {
						myThreads[i] = new Thread(new ThucThiO(WebName[i]));
						myThreads[i].start();
						Thread.sleep(1000);
						myThreads[i].join();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

		}

	}

}