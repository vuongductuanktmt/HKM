package Server.HKM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.websocket.server.ServerEndpoint;

import org.bson.Document;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

import Manager.AppManagerServer;
import jline.internal.Log;

@ServerEndpoint(value = "/websocket/{clientId}")
public class server extends WebSocketServer {
	public static String id = null;

	public server(int port) {
		super(new InetSocketAddress(port));
	}

	public server(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		log("New connection from " + conn.getRemoteSocketAddress());
		try {
			MongoDB data_config = new MongoDB("Config");
			data_config.UpdateRecord(new Document("__Sum__", data_config.GetOneRecordInt(new Document(), "__Sum__")),
					new Document("__Sum__", data_config.GetOneRecordInt(new Document(), "__Sum__") + 1));

		} catch (UnknownHostException | MessagingException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		log("Connection from " + conn.getRemoteSocketAddress() + " is closed");
		try {
			MongoDB data_config = new MongoDB("Config");
			data_config.UpdateRecord(new Document("__Sum__", data_config.GetOneRecordInt(new Document(), "__Sum__")),
					new Document("__Sum__", data_config.GetOneRecordInt(new Document(), "__Sum__") - 1));

		} catch (UnknownHostException | MessagingException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		try {
			log(message + " from " + conn.getRemoteSocketAddress());
			Extend Extendc = new Extend();
			JSONObject obj = new JSONObject(message);
			String request = obj.getString("__Request__").trim();
			String authorities = obj.getString("__Authorities__");
			System.out.println("request:" + request);
			System.out.println("authorities:" + authorities);
			if (authorities.equals("admin")) {
				switch (request) {
				case "update":
					conn.send(Extendc.Update(message));
					break;
				case "update_categary_parent":
					conn.send(Extendc.UpdateCategoryParent(message));
					break;
				case "update_category_child":
					conn.send(Extendc.UpdateAllChild(message));
					break;
				case "remove_category_parent":
					conn.send(Extendc.RemoveCategoryParent(message));
					break;
				case "remove_all_category_parent":
					conn.send(Extendc.RemoveAllCategoryParent(message));
					break;
				case "create":
					conn.send(Extendc.Create(message));
					break;
				case "first_login":

					id = Extendc.Firstlogin(message);
					conn.send(id);
					break;
				case "last_login":
					String value = Extendc.LastLogin(message, Integer.parseInt(id));
					if (value.equals("true")) {
						MongoDB data_user = new MongoDB("User");
						data_user.UpdateRecord(new Document(), new Document("__LastLogin__", Extend.getTime()));
						data_user.UpdateRecord(new Document(), new Document("__IP__", conn.getRemoteSocketAddress()
								.toString().substring(1, conn.getRemoteSocketAddress().toString().indexOf(":"))));
					}
					conn.send(value);
					break;
				case "delete":
					conn.send(Extendc.Delete(message));
					break;
				case "check_exist_record":
					conn.send(Extendc.CheckExistRecord(message));
				case "get_pagination":
					conn.send(Extendc.GetPagination(message));
					break;
				case "max_page":
					conn.send(Extendc.PageMax(message));
					break;
				case "get_record_string":
					conn.send(Extendc.GetOneRecordString(message));
					break;
				case "get_record_boolean":
					conn.send(Extendc.GetOneRecordBoolean(message));
					break;
				case "get_record_int":
					conn.send(Extendc.GetOneRecordInt(message));
					break;
				case "get_record_long":
					conn.send(Extendc.GetOneRecordLong(message));
					break;
				case "get_record_date":
					conn.send(Extendc.GetOneRecordDate(message));
					break;
				case "get_all_record":
					conn.send(Extendc.Query(message));
					break;
				case "get_all_record_delete":
					conn.send(Extendc.Query(message));
					break;
				case "get_distinct":
					conn.send(Extendc.Distinct(message));
					break;
				case "get_error":
					conn.send(Extendc.GetError(message));
					break;
				case "get_count_record":
					conn.send(Extendc.CountRecord(message));
					break;
				default:
					CloseConnection(conn);
					break;

				}
			} else if (authorities.equals("user")) {
				switch (request) {
				case "get_all_data":
					conn.send(Extendc.GetAllData(message));
					break;
				case "create_history":
					conn.send(Extendc.CreateHistoryUserClient(message));
					break;
				case "register_user":
					conn.send(Extendc.CreateUserClient(message));
					break;
				case "get_pagination":
					conn.send(Extendc.GetPagination(message));
					break;
				case "get_history_user":
					conn.send(Extendc.GetHistory(message));
					break;
				case "get_category_parent":
					conn.send(Extendc.GetCategoryParent(message));
					break;
				case "get_top_most":
					conn.send(Extendc.GetTopMost(message));
					break;
				default:
					CloseConnection(conn);
					break;
				}
			}
		} catch (Exception e) { /*
								 * Close connection if message has wrong format
								 */
			CloseConnection(conn);
		}

	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		log("error " + ex.getMessage());
	}

	public static void main(String[] args)
			throws InterruptedException, IOException, AddressException, MessagingException {
		String host = "localhost";
		int port = 8081;
		MongoDB data_config = new MongoDB("Config");
		MongoDB data_products = new MongoDB("TableWebInfo");
		MongoDB data_parents = new MongoDB("CategoryParents");
		MongoDB data_user = new MongoDB("UserClient");
		MongoDB data_user_ = new MongoDB("User");
		if (data_config.CountRecords(new Document()) == 0) {
			data_config.Collection.insertOne(new Document("__HOST__", "localhost").append("__POST__", 8081)
					.append("__PATH__", "/home/vuongductuan/Documents/AppHKM").append("__Sum__", 0));
		}
		host = data_config.GetOneRecordString(new Document(), "__HOST__");
		port = data_config.GetOneRecordInt(new Document(), "__POST__");
		if (args.length > 0) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		WebSocketServer server = new server(new InetSocketAddress(host, port));
		server.start();

		log("Server is running on port " + server.getPort());

		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print(">>");
			String in = sysin.readLine();
			if (in.equals("exit")) {
				server.stop();
				log("Server is stop");
				break;
			}
			switch (in) {
			case "change_host":
				System.out.print(">>");
				System.out.print("Host:");
				String in1 = sysin.readLine();
				data_config.UpdateRecord(new Document(), new Document("__HOST__", in1));
				Log.info("Change __HOST__ succses, __HOST__ now is:" + in1);
				break;
			case "change_port":
				System.out.print(">>");
				System.out.print("Port:");
				String in2 = sysin.readLine();
				data_config.UpdateRecord(new Document(), new Document("__POST__", Integer.parseInt(in2)));
				Log.info("Change __PORT__ succses, __PORT__ now is:" + Integer.parseInt(in2));
				break;
			case "reset_count_user":
				data_config.UpdateRecord(new Document(), new Document("__Sum__", 0));
				Log.info("Change __Sum__ succses, __Sum__ now is:" + 0);
				break;
			case "sum_record":
				System.out.println("-----------------------------------------------");
				Log.info("Total:\n");
				System.out.println("Event: " + data_products.CountRecords(new Document("__Status__", "Event")));
				System.out.println("Selling: " + data_products.CountRecords(new Document("__Status__", "Selling")));
				System.out.println("Promotion: " + data_products.CountRecords(new Document("__Status__", "Promotion")));
				System.out.println("Category Parents: " + data_parents.CountRecords(new Document()));
				System.out.println("User: " + data_user.CountRecords(new Document()));
				System.out.println("-----------------------------------------------");
				break;
			case "admin":
				System.out.println(data_user_.GetOneRecordString(new Document(), "__User__"));
				break;
			case "drop_host":
				data_parents.dropDatabase("hosted");
				if (data_config.CountRecords(new Document()) == 0) {
					data_config.Collection.insertOne(new Document("__HOST__", "localhost").append("__POST__", 8081)
							.append("__PATH__", "/home/vuongductuan/Documents/AppHKM").append("__Sum__", 0));
				}
				break;
			case "":
				System.out.println("You should not be blank!!");
				break;
			default:
				System.out.println("Don't understand you want??");
				break;
			}
		}
	}

	public static void CloseConnection(WebSocket conn) {
		log("Disconnect with client." + conn.getRemoteSocketAddress());
		conn.close();
	}

	private static void log(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
	}

}