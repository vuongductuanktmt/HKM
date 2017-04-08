package App.HKM.View;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import org.bson.Document;

import App.HKM.Login;
import App.HKM.Data.Category;
import App.HKM.Data.Event;
import App.HKM.Data.Products;
import Database.HKM.MongoDB;

public class ModelView {
	public static int pageNumer__Selling = 1, pageNumer__Event = 1, pageNumer__Promotion = 1;
	public Object[][] rows = null;
	public String[] columnNamesProducts = { "STT", "Title", "Link", "Current Price", "Old Price", "Date Insert",
			"Status", "Delete" };
	public String[] columnNamesCategoryParent = {"STT", "Name", "SumCategoryChild","Delete"};

	public DefaultListModel<Event> ListEvents(String Search) throws UnknownHostException {
		DefaultListModel<Event> model_event = new DefaultListModel<>();
		List<org.bson.Document> documents = new ArrayList<org.bson.Document>();
		MongoDB data = new MongoDB("TableWebInfo");
		documents = data.Pagination(new org.bson.Document("__DateInsert__", 1), "Event", Search, pageNumer__Event,
				Login.page_size);
		for (org.bson.Document document : documents) {
			model_event.addElement(new Event(document.getString("__Title__"), document.getString("__LinkTitle__"),
					document.getString("__HomePage__"), document.getString("__LinkImage__"),
					document.getLong("__ViewCount__"), document.getLong("__ViewCount__"),
					document.getDate("__DateInsert"), document.getBoolean("__Delete__")));
		}
		return model_event;
	}

	public DefaultListModel<Products> ListProducts(String Status, String Search) throws UnknownHostException {
		DefaultListModel<Products> model_product = new DefaultListModel<>();
		try {
			List<org.bson.Document> documents = new ArrayList<org.bson.Document>();
			MongoDB data = new MongoDB("TableWebInfo");
			MongoDB data_category_child = new MongoDB("CategoryChild");
			int pageNumer = (Status.equals("Selling")) ? pageNumer__Selling : pageNumer__Promotion;
			documents = data.Pagination(new org.bson.Document("__DateInsert__", 1), Status, Search, pageNumer,
					Login.page_size);
			for (org.bson.Document document : documents) {
				DefaultListModel<Category> category = new DefaultListModel<>();
				List<org.bson.Document> documents_categorys = new ArrayList<org.bson.Document>();
				documents_categorys = data_category_child.Query(
						new org.bson.Document("__LinkTitle__", document.getString("__LinkTitle__")),
						new org.bson.Document("__LinkTitle__", 1));
				for (org.bson.Document documents_category : documents_categorys) {
					category.addElement(new Category(documents_category.getString("__CategoryChildName__"),
							documents_category.getString("__CategoryChildLink__"),documents_category.getString("__CategoryParentName__")));
				}
				model_product.addElement(new Products(document.getString("__Title__"),
						document.getString("__LinkTitle__"), document.getString("__HomePage__"),
						document.getString("__LinkImage__"), document.getLong("__ViewCount__"),
						document.getLong("__ViewCount__"), document.getString("__CurrentPrice__"),
						document.getString("__OldPrice__"), document.getDate("__DateInsert__"),
						document.getBoolean("__Delete__"), document.getString("__Status__"), category));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return model_product;
	}

	public void ReloadJlist(JList list, DefaultListModel<?> model, String Status, String Search) {
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

	public void LoadTableProduct() throws UnknownHostException {
		MongoDB data = new MongoDB("TableWebInfo");
		ArrayList<Products> products = new ArrayList<Products>();
		List<String> list = new ArrayList<String>();
		list.add("Promotion");
		list.add("Selling");
		list.add("Event");
		products = data.GetListArrayProducts(
				new Document("__Status__", new Document("$in", list)).append("__Delete__", false));
		this.rows = new Object[products.size()][8];
		for (int i = 0; i < products.size(); i++) {
			this.rows[i][0] = i + 1;
			this.rows[i][1] = products.get(i).get__Title__();
			this.rows[i][2] = products.get(i).get__Href__();
			this.rows[i][3] = products.get(i).get__CurrentPrice__();
			this.rows[i][4] = (products.get(i).get__OldPrice__() != null)
					? "<html><b><span><strike>" + products.get(i).get__OldPrice__() + "</strike><span></b></html>" : "";
			this.rows[i][5] = products.get(i).get__DateInsert__();
			this.rows[i][6] = products.get(i).get__Status__();
			this.rows[i][7] = products.get(i).is__Delete__();
		}
	}

	public void LoadTableProductRecycle() throws UnknownHostException {
		MongoDB data = new MongoDB("TableWebInfo");
		ArrayList<Products> products = new ArrayList<Products>();
		List<String> list = new ArrayList<String>();
		list.add("Promotion");
		list.add("Selling");
		list.add("Event");
		products = data
				.GetListArrayProducts(new Document("__Status__", new Document("$in", list)).append("__Delete__", true));
		this.rows = new Object[products.size()][8];
		for (int i = 0; i < products.size(); i++) {
			this.rows[i][0] = i + 1;
			this.rows[i][1] = products.get(i).get__Title__();
			this.rows[i][2] = products.get(i).get__Href__();
			this.rows[i][3] = products.get(i).get__CurrentPrice__();
			this.rows[i][4] = (products.get(i).get__OldPrice__() != null)
					? "<html><b><span><strike>" + products.get(i).get__OldPrice__() + "</strike><span></b></html>" : "";
			this.rows[i][5] = products.get(i).get__DateInsert__();
			this.rows[i][6] = products.get(i).get__Status__();
			this.rows[i][7] = products.get(i).is__Delete__();
		}
	}
	
	public void LoadCategoryParents() throws UnknownHostException {
		MongoDB data = new MongoDB("CategoryParents");
		ArrayList<App.HKM.Data.CategoryParents> category_parent = new ArrayList<App.HKM.Data.CategoryParents>();
		List<String> list = new ArrayList<String>();
		category_parent = data.GetListArrayCategoryParents(new Document());
		this.rows = new Object[category_parent.size()][3];
		for (int i = 0; i < category_parent.size(); i++) {
			this.rows[i][0] = i + 1;
			this.rows[i][1] = category_parent.get(i).get__CategoryParentName__();
					MongoDB datacategory = new MongoDB("TableWebInfo");
			this.rows[i][2] = datacategory.CountRecords(new Document("__CategoryParentName__",this.rows[i][1]));
			
		}
	}

}
