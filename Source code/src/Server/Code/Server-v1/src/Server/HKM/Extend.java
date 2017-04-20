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

import com.mongodb.client.MongoCursor;

import jline.internal.Log;

public class Extend {
	public static String value[] = { "Ai Yeu Bac Ho Chi Minh", "Bac Ho Cho Em Tat Ca", "Ban Tay Me",
			"Minh La Gi Cua Nhau" };

	public void CreateHistoryUserClient(String data)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		JSONObject obj = new JSONObject(data);
		String token_user = obj.getString("__TokenUser__");
		String action = obj.getString("__Action__");
		String link = obj.getString("__Link__");
		MongoDB data_products = new MongoDB("TableWebInfo");
		MongoDB data_history = new MongoDB("HistoryUser");
		MongoDB data_user_client = new MongoDB("UserClient");
		if (data_user_client.CheckExistsRecord(new Document("__TokenUser__", token_user))) {
			data_history.Collection.insertOne(
					new Document("__TokenUser__", token_user).append("__Action__", action).append("__Link__", link));
			Document toIncrement = new Document(action, 1);
			Document update = new Document("$inc", toIncrement);
			data_products.UpdateRecord(new Document("__LinkTitle__", link), update);
		}
	}

	public String CreateUserClient(String data)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		MongoDB data_user_client = new MongoDB("UserClient");
		if (!data_user_client.CheckExistsRecord(new Document("__TokenUser__", data))) {
			data_user_client.Collection.insertOne(new Document("__TokenUser__", data).append("__Status__", true));
			Log.info("Create user successful.");
			return "Create user successful ";
		}
		Log.info("Create user failure.");
		return "Create user failure";
	}

	public String GetAllData(String token_user)
			throws AddressException, UnknownHostException, MessagingException, InterruptedException {
		String temptxxx = null;
		Document DATA = new Document();
		MongoDB data_products = new MongoDB("TableWebInfo");
		MongoDB data_historys = new MongoDB("HistoryUser");
		MongoDB data_parents = new MongoDB("CategoryParents");
		MongoDB data_user_client = new MongoDB("UserClient");
		if (data_user_client.CheckExistsRecord(new Document("__TokenUser__", token_user))) {
			Log.info("Accept user client.");
			DATA.append("Product", data_products.Query(new Document(), new Document()))
					.append("History", data_historys.Query(new Document(), new Document()))
					.append("CategoryParents", data_parents.Query(new Document(), new Document()));
			temptxxx = com.mongodb.util.JSON.serialize(DATA);
		} else {
			Log.info("Can not find user.");
			temptxxx = "Not found user";
		}
		return temptxxx;
	}
	public String Query(String json) throws AddressException, UnknownHostException, MessagingException, InterruptedException{
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String search = obj.getString("__Search__");
		String condition = obj.getString("__Condition__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		List<Document> documents = new ArrayList<>();
		if(var.equals("null")&&value.equals("null")){
			documents = data.Query(new Document(),new Document(condition, Integer.parseInt(search)));
		}else if(var.equals("__Delete__")){
			documents = data.Query(new Document(var, Boolean.valueOf(value)),new Document(condition, Integer.parseInt(search)));
		}
		else{
			documents = data.Query(new Document(var, value),new Document(condition, Integer.parseInt(search)));
		}
		
		Document doc = new Document("data", documents);
		String values = doc.toJson();
		//System.out.println(value.substring(com.mongodb.util.JSON.serialize(doc).indexOf(":")+2, com.mongodb.util.JSON.serialize(doc).length()-1));
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
		data.UpdateRecordAll(new Document(var, value), new Document(condition, search));
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
		String status = obj.getString("__Status__");
		String search = obj.getString("__Search__");
		int pagenumber = obj.getInt("__PageNumber__");
		int pagesize = obj.getInt("__PageSize__");
		MongoDB data = new MongoDB(table);
		List<Document> documents = new ArrayList<>();
		documents = data.Pagination(new Document(), status, search, pagenumber,pagesize);
		Document doc = new Document("data", documents);
		String value = doc.toJson();
		//System.out.println(value.substring(com.mongodb.util.JSON.serialize(doc).indexOf(":")+2, com.mongodb.util.JSON.serialize(doc).length()-1));
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
	public String CheckExistRecord(String json) throws AddressException, UnknownHostException, MessagingException, InterruptedException{
		JSONObject obj = new JSONObject(json);
		String var = obj.getString("__Var__");
		String value = obj.getString("__Value__");
		String table = obj.getString("__Table__");
		MongoDB data = new MongoDB(table);
		boolean temp = data.CheckExistsRecord(new Document(var,value));
		return temp?"true":"false";
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
