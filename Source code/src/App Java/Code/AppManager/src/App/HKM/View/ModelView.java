package App.HKM.View;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import org.bson.Document;

import App.HKM.Login;
import App.HKM.Data.Category;
import App.HKM.Data.CategoryParents;
import App.HKM.Data.Event;
import App.HKM.Data.Error;
import App.HKM.Data.Products;
import ServerManagerData.ClientToServer;
import ServerManagerData.ServerToClient;

public class ModelView {
	public static int pageNumer__Selling = 1, pageNumer__Event = 1, pageNumer__Promotion = 1;
	public static Object[][] rows = null;
	public String[] columnNamesProducts = { "STT", "Title", "Current Price", "Old Price", "Date Insert", "Status" };
	public String[] columnNamesCategoryParent = { "STT", "Name", "SumCategoryChild" };
	public String[] columnNamesCategoryChild = { "STT", "Name", "Parent" };
	public String[] columnNamesError = { "STT", "Error", "HomePage", "Date" };
	public static String[] rows_parent;

	public DefaultListModel<Event> ListEvents(String Search)
			throws InterruptedException, URISyntaxException, IOException {
		DefaultListModel<Event> model_event = new DefaultListModel<>();
		List<Document> documents = new ArrayList<Document>();
		ClientToServer CtS = new ClientToServer("get_pagination", Login.page_size, pageNumer__Event, " ", " ", "admin",
				"TableWebInfo", Search, "Event");
		String data = CtS.getValueRequests();
		documents = ServerToClient.getDataListProducts(data);
		for (Document document : documents) {
			model_event.addElement(new Event(document.getString("__Title__"), document.getString("__LinkTitle__"),
					document.getString("__HomePage__"), document.getString("__LinkImage__"),
					document.getLong("__LoveCount__"), document.getLong("__ViewCount__"),
					document.getDate("__DateInsert__"), document.getBoolean("__Delete__")));
			System.out.println(document.getString("__LinkImage__"));
		}

		return model_event;
	}

	@SuppressWarnings("unchecked")
	public DefaultListModel<Products> ListProducts(String Status, String Search) throws UnknownHostException {
		DefaultListModel<Products> model_product = new DefaultListModel<>();
		try {
			List<Document> documents = new ArrayList<Document>();
			int pageNumer = (Status.equals("Selling")) ? pageNumer__Selling : pageNumer__Promotion;
			ClientToServer CtS = new ClientToServer("get_pagination", Login.page_size, pageNumer, "", "", "admin",
					"TableWebInfo", Search, Status);
			String data = CtS.getValueRequests();
			documents = ServerToClient.getDataListProducts(data);
			for (Document document : documents) {
				DefaultListModel<Category> category = new DefaultListModel<>();
				List<Document> documents_categorys = (List<Document>) document.get("__Category__");
				for (Document documents_category : documents_categorys) {
					category.addElement(new Category(documents_category.getString("__CategoryChildName__"),
							documents_category.getString("__CategoryChildLink__"),
							documents_category.getString("__CategoryParentName__")));
				}
				model_product.addElement(new Products(document.getString("__Title__"),
						document.getString("__LinkTitle__"), document.getString("__HomePage__"),
						document.getString("__LinkImage__"), document.getLong("__LoveCount__"),
						document.getLong("__ViewCount__"), document.getString("__CurrentPrice__"),
						document.getString("__OldPrice__"), document.getDate("__DateInsert__"),
						document.getBoolean("__Delete__"), document.getString("__Status__"), category));
				System.out.println(document.getString("__Title__"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return model_product;
	}

	public void ReloadJlist(JList list, DefaultListModel<?> model, String Status, String Search)
			throws InterruptedException, URISyntaxException, IOException {
		System.out.println("Clear");
		model.clear();
		try {
			System.out.println(list.getName());
			switch (Status) {
			case "Event":
				model = ListEvents(Search);
				break;
			case "Selling":
				model = ListProducts(Status, Search);
				break;
			case "Promotion":
				model = ListProducts(Status, Search);
				break;
			default:
				break;
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		list.setModel(model);
	}

	public void LoadTableProduct() throws InterruptedException, URISyntaxException, IOException {
		ArrayList<Products> products = new ArrayList<Products>();
		ClientToServer CtS = new ClientToServer("get_all_record", "admin", "TableWebInfo", "__Delete__", "false",
				"__Title__", 1);
		String data = CtS.getValueRequests();
		products = ServerToClient.GetListArrayProducts(data);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm  (dd-MM-yyyy)", new Locale("vi", "VN"));
		timeFormat.setTimeZone(TimeZone.getTimeZone("+7GMT"));
		rows = new Object[products.size()][6];
		for (int i = 0; i < products.size(); i++) {
			rows[i][0] = i + 1;
			rows[i][1] = products.get(i).get__Title__();
			rows[i][2] = products.get(i).get__CurrentPrice__();
			rows[i][3] = (products.get(i).get__OldPrice__() != null)
					? "<html><b><span><strike>" + products.get(i).get__OldPrice__() + "</strike><span></b></html>" : "";
			String gettime = timeFormat.format(products.get(i).get__DateInsert__());
			rows[i][4] = gettime;
			rows[i][5] = products.get(i).get__Status__();
		}
	}

	public void LoadTableProductRecycle() throws InterruptedException, URISyntaxException, IOException {
		ArrayList<Products> products = new ArrayList<Products>();
		ClientToServer CtS = new ClientToServer("get_all_record", "admin", "TableWebInfo", "__Delete__", "true",
				"__Title__", 1);
		String data = CtS.getValueRequests();
		products = ServerToClient.GetListArrayProducts(data);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm  (dd-MM-yyyy)", new Locale("vi", "VN"));
		timeFormat.setTimeZone(TimeZone.getTimeZone("+7GMT"));
		rows = new Object[products.size()][6];
		for (int i = 0; i < products.size(); i++) {
			rows[i][0] = i + 1;
			rows[i][1] = products.get(i).get__Title__();
			rows[i][2] = products.get(i).get__CurrentPrice__();
			rows[i][3] = (products.get(i).get__OldPrice__() != null)
					? "<html><b><span><strike>" + products.get(i).get__OldPrice__() + "</strike><span></b></html>" : "";
			String gettime = timeFormat.format(products.get(i).get__DateInsert__());
			rows[i][4] = gettime;
			rows[i][5] = products.get(i).get__Status__();
		}
	}

	public void LoadCategoryParents() throws InterruptedException, URISyntaxException, IOException {
		ArrayList<CategoryParents> category_parent = new ArrayList<CategoryParents>();
		ClientToServer CtS = new ClientToServer("get_all_record", "admin", "CategoryParents", "null", "null",
				"__CategoryParentName__", 1);
		String data = CtS.getValueRequests();
		category_parent = ServerToClient.GetListArrayCategoryParents(data);
		rows = new Object[category_parent.size()][3];
		for (int i = 0; i < category_parent.size(); i++) {
			rows[i][0] = i + 1;
			rows[i][1] = category_parent.get(i).get__CategoryParentName__();
			CtS = new ClientToServer("get_count_record", "admin", "TableWebInfo", "__Category__.__CategoryParentName__",
					"", category_parent.get(i).get__CategoryParentName__(), 0);
			data = CtS.getValueRequests();
			rows[i][2] = Integer.parseInt(data);

		}
	}

	public void LoadCategoryChilds() throws URISyntaxException, IOException {
		ArrayList<Category> category_child = new ArrayList<Category>();
		ClientToServer CtS = new ClientToServer("get_distinct", "admin", "TableWebInfo", "null", "null",
				"__Category__.__CategoryChildLink__", "__Category__", 0);
		String request = CtS.getValueRequests();
		category_child = ServerToClient.GetListArrayCategoryChilds(request);
		rows = new Object[category_child.size()][3];
		for (int i = 0; i < category_child.size(); i++) {
			rows[i][0] = i + 1;
			rows[i][1] = category_child.get(i).get__Title__();
			rows[i][2] = category_child.get(i).get__CategoryParentName__();
		}

	}

	public void LoadError() throws URISyntaxException, IOException {
		ArrayList<Error> error = new ArrayList<Error>();
		ClientToServer CtS = new ClientToServer("get_error", "admin", "Error", "null", "null", "null", "null", 0);
		String request = CtS.getValueRequests();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm  (dd-MM-yyyy)", new Locale("vi", "VN"));
		timeFormat.setTimeZone(TimeZone.getTimeZone("+7GMT"));
		error = ServerToClient.GetListArrayError(request);
		rows = new Object[error.size()][4];
		for (int i = 0; i < error.size(); i++) {
			rows[i][0] = i + 1;
			rows[i][1] = error.get(i).getContent();
			rows[i][2] = error.get(i).getHomePage();
			String gettime = timeFormat.format(error.get(i).getDateError());
			rows[i][3] = gettime;
		}
	}

}
