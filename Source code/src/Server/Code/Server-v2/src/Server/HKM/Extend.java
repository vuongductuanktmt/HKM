package Server.HKM;

import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import org.bson.Document;
import org.json.JSONObject;
import jline.internal.Log;

public class Extend {
	public static String value[] = { "Ai Yeu Bac Ho Chi Minh", "Bac Ho Cho Em Tat Ca", "Ban Tay Me",
			"Minh La Gi Cua Nhau" };

	public String CreateHistoryUserClient(String data)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(data);
		String token_user = obj.getString("__TokenUser__");
		String link = obj.getString("__Value__");
		String action = obj.getString("__Var__");
		String table = obj.getString("__Table__");
		if (CheckTokenUser(token_user)) {
			MongoDB data_history = new MongoDB(table);
			MongoDB data_products = new MongoDB("TableWebInfo");
			data_history.Collection.insertOne(
					new Document("__TokenUser__", token_user).append("__Action__", action).append("__Link__", link));
			Document toIncrement = new Document(action, 1);
			Document update = new Document("$inc", toIncrement);
			data_products.Collection.updateOne(new Document("__LinkTitle__", link), update);
			return "true";
		}
		return "false";
	}

	public String CreateUserClient(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String user_token = obj.getString("__TokenUser__");
		String table = obj.getString("__Table__");
		MongoDB data_user_client = new MongoDB(table);
		if (!data_user_client.CheckExistsRecord(new Document("__TokenUser__", user_token))
				|| data_user_client.CountRecords(new Document()) == 0) {
			data_user_client.Collection.insertOne(new Document("__TokenUser__", user_token).append("__Status__", true));
			Log.info("Create user successful.");
			return "true";
		} else {
			Log.info("Create user failure.");
			return "false";
		}
	}

	public String GetAllData(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String user_token = obj.getString("__TokenUser__");
		String table = obj.getString("__Table__");
		String temptxxx = null;
		Document DATA = new Document();
		MongoDB data_products = new MongoDB(table);
		MongoDB data_historys = new MongoDB("HistoryUser");
		MongoDB data_parents = new MongoDB("CategoryParents");
		if (CheckTokenUser(user_token)) {
			Log.info("Accept user client.");
			DATA.append("Product", data_products.Query(new Document(), new Document()))
					.append("History", data_historys.Query(new Document("__TokenUser__", user_token), new Document()))
					.append("CategoryParents", data_parents.Query(new Document(), new Document()));
			Document doc = new Document("data", DATA);
			String values = doc.toJson();
			// System.out.println(value.substring(com.mongodb.util.JSON.serialize(doc).indexOf(":")+2,
			// com.mongodb.util.JSON.serialize(doc).length()-1));
			return values;
		} else {
			Log.info("Can not find user.");
			temptxxx = "Not found user";
		}
		return temptxxx;
	}

	public String Query(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String search = obj.getString("__Search__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		List<Document> documents = new ArrayList<>();
		if (var.equals("null") && value.equals("null")) {
			documents = data.Query(new Document(), new Document(condition, Integer.parseInt(search)));
		} else {
			if (var.equals("__Delete__")) {

				documents = data.Query(new Document(var, Boolean.valueOf(value)),
						new Document(condition, Integer.parseInt(search)));
			} else {
				documents = data.Query(new Document(var, value), new Document(condition, Integer.parseInt(search)));
			}
		}
		Document doc = new Document("data", documents);
		String values = doc.toJson();
		// System.out.println(value.substring(com.mongodb.util.JSON.serialize(doc).indexOf(":")+2,
		// com.mongodb.util.JSON.serialize(doc).length()-1));
		return values;
	}
	public String Distinct(String json) throws AddressException, UnknownHostException, MessagingException, InterruptedException{
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		List<Document> documents = new ArrayList<>();
		if(var.equals("null")){
			documents = data.DistinctCategory(new Document(), condition);
		}else{
			documents = data.DistinctCategory(new Document(var,value), condition);
		}
		
		Document doc = new Document("data", documents);
		String values = doc.toJson();
		return values;
	}
	public String Firstlogin(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String user = obj.getString("__User__");
		String password = obj.getString("__Password__");
		MongoDB user_admin = new MongoDB("User");
		if (user_admin.CheckExistsRecord(new Document("__User__", user).append("__Password__", password))) {
			if (user_admin.GetOneRecordBoolean(new Document("__User__", user).append("__Password__", password),
					"__Status__")) {
				Random rand = new Random();
				int n = rand.nextInt(value.length);
				return String.valueOf(n);
			} else {
				return "Locked";
			}
		} else {
			return "false";
		}
	}

	public String LastLogin(String json, int number) {
		JSONObject obj = new JSONObject(json);
		String security = obj.getString("__Security__");
		if (value[number].equals(security)) {
			return "true";
		}
		return "false";
	}

	public String Update(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String condition = obj.getString("__Condition__");
		String search = obj.getString("__Search__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		if (!var.equals("null")) {
			data.UpdateRecordAll(new Document(var, value), new Document(condition,
					(search.equals("true") || search.equals("false")) ? Boolean.valueOf(search) : search));
		} else {
			data.UpdateRecordAll(new Document(), new Document(condition,
					search.equals("true") || search.equals("false") ? Boolean.valueOf(search) : search));
		}

		return "true";
	}
	public String UpdateAllChild(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String condition = obj.getString("__Condition__");
		String search = obj.getString("__Search__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
			data.UpdateRecordAllChild(new Document(var, value), new Document(condition, search));
			System.out.println("OK");
		return "true";
	}
	public String Create(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		data.Collection.insertOne(new Document(var, value));
		return "true";
	}

	public String Delete(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		data.RemoveRecordAll(new Document(var, condition));
		return "true";
	}

	public String GetPagination(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String table = obj.getString("__Table__");
		String athorities = obj.getString("__Authorities__");
		String status = athorities.equals("user") ? obj.getString("__Var__") : obj.getString("__Status__");
		String search = obj.getString("__Search__");
		int pagenumber = obj.getInt("__PageNumber__");
		int pagesize = obj.getInt("__PageSize__");
		MongoDB data = new MongoDB(table);
		List<Document> documents = new ArrayList<>();
		documents = data.Pagination(new Document("__DateInsert__", -1), status, search, pagenumber, pagesize);
		Document doc = new Document("data", documents);
		String value = doc.toJson();
		// System.out.println(value.substring(com.mongodb.util.JSON.serialize(doc).indexOf(":")+2,
		// com.mongodb.util.JSON.serialize(doc).length()-1));
		return value;

	}

	public String PageMax(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String table = obj.getString("__Table__");
		String status = obj.getString("__Status__");
		String search = obj.getString("__Search__");
		int pagesize = obj.getInt("__PageSize__");
		MongoDB data = new MongoDB(table);
		return String.valueOf(data.PageMax(new Document("__Status__", status), pagesize, search));

	}

	public String CountRecord(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		return String.valueOf(data.CountRecords(new Document(var, condition)));
	}

	public String GetOneRecordString(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String search = obj.getString("__Search__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		return data.GetOneRecordString(new Document(var, condition), search);
	}

	public String GetOneRecordInt(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String search = obj.getString("__Search__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		return String.valueOf(data.GetOneRecordInt(new Document(var, condition), search));
	}

	public String GetOneRecordLong(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String search = obj.getString("__Search__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		return String.valueOf(data.GetOneRecordLong(new Document(var, condition), search));
	}

	public String GetOneRecordBoolean(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String search = obj.getString("__Search__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		return String.valueOf(data.GetOneRecordBoolean(new Document(var, condition), search));
	}

	public String GetOneRecordDate(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String search = obj.getString("__Search__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		return String.valueOf(data.GetOneRecordDate(new Document(var, condition), search));
	}

	public String CheckExistRecord(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		boolean temp = data.CheckExistsRecord(new Document(var, value));
		return temp ? "true" : "false";
	}

	public String GetTopMost(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String token_user = obj.getString("__TokenUser__");
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String table = obj.getString("__Table__");
		if (CheckTokenUser(token_user)) {
			MongoDB data = new MongoDB(table);
			Document doc = new Document("data", data.TopMost(var, Integer.parseInt(value)));
			String values = doc.toJson();
			return values;
		}
		return "false";
	}

	public String GetHistory(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String token_user = obj.getString("__TokenUser__");
		String table = obj.getString("__Table__");
		MongoDB data_history = new MongoDB(table);
		if (CheckTokenUser(token_user)) {
			Document doc = new Document("data",
					data_history.Query(new Document("__TokenUser__", token_user), new Document()));
			String values = doc.toJson();
			return values;
		}
		return null;
	}

	public String GetCategoryParent(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String token_user = obj.getString("__TokenUser__");
		String table = obj.getString("__Table__");
		if (CheckTokenUser(token_user)) {
			MongoDB data_category = new MongoDB(table);
			Document doc = new Document("data", data_category.Query(new Document(), new Document()));
			String values = doc.toJson();
			return values;
		}
		return null;
	}
public String GetError(String json) throws AddressException, UnknownHostException, MessagingException, InterruptedException{
	JSONObject obj = new JSONObject(json);
	String table = obj.getString("__Table__");
	MongoDB data_error = new MongoDB(table);
	Document doc = new Document("data", data_error.Query(new Document("__Status__",false), new Document("__Date__",1)));
	String values = doc.toJson();
	return values;
}
	public String Token(String message) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-512");

			md.update(message.getBytes());
			byte[] mb = md.digest();
			String out = "";
			for (int i = 0; i < mb.length; i++) {
				byte temp = mb[i];
				String s = Integer.toHexString(new Byte(temp));
				while (s.length() < 2) {
					s = "0" + s;
				}
				s = s.substring(s.length() - 2);
				out += s;
			}
			System.out.println("CRYPTO: " + out);
			return out;

		} catch (NoSuchAlgorithmException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		return null;
	}

	public boolean CheckTokenUser(String token)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		MongoDB data_user = new MongoDB("UserClient");
		return data_user.CheckExistsRecord(new Document("__TokenUser__", token));
	}

	public String UpdateCategoryParent(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String condition = obj.getString("__Condition__");
		String search = obj.getString("__Search__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		data.UpdateRecordAll(new Document(var, value), new Document(condition, search));
		MongoDB data_products = new MongoDB("TableWebInfo");
		data_products.UpdateRecordAllChild(new Document("__Category__.__CategoryParentName__", value),
				new Document("__CategoryParentName__", search));
		return "true";
	}

	public String RemoveCategoryParent(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		data.RemoveRecordAll(new Document(var, value));
		MongoDB data_products = new MongoDB("TableWebInfo");
		data_products.UpdateRecordAllChild(new Document("__Category__.__CategoryParentName__", value),
				new Document("__CategoryParentName__", "Chung"));
		return "true";
	}

	public String RemoveAllCategoryParent(String json)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(json);
		String table = obj.getString("__Table__");
		MongoDB data_parents = new MongoDB(table);
		MongoDB data_products = new MongoDB("TableWebInfo");
		List<Document> parents = new ArrayList<>();
		parents = data_parents.Query(new Document(), new Document());
		for (Document parent : parents) {
			System.out.println("Delete");
			if (parent.getString("__CategoryParentName__").equals("Chung")) {
				continue;
			}
			data_parents.RemoveRecordAll(new Document("__CategoryParentName__", parent.getString("__CategoryParentName__")));
			List<Document> products = new ArrayList<>();
			products  = data_products.Query(new Document(), new Document());
			if(data_products.CountRecords(new Document("__Category__.__CategoryParentName__",parent))!=0){
			for (Document document : products) {
				data_products.UpdateRecordAllChild(new Document("__LinkTitle__", document.getString("__LinkTitle__")),
						new Document("__CategoryParentName__", "Chung"));
			}
			}
		}
		return "true";
	}
	public static Date getTime() throws ParseException {
		try {
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", new Locale("vi", "VN"));
			Date today = new Date(System.currentTimeMillis());
			String gettime = timeFormat.format(today.getTime());
			timeFormat.setTimeZone(TimeZone.getTimeZone("+7GMT"));
			return timeFormat.parse(gettime);
		} catch (Exception e) {
			System.out.println("Can't get info Date");
		}
		return null;

	}
}
