package ServerManagerData;

import java.io.IOException;
import java.net.URISyntaxException;

import test.client.ClientToServer_V1;

public class test {
public static void main(String[] args) throws URISyntaxException, IOException {
	/*List<Document> documents = new ArrayList<Document>();
	ClientToServer CtS = new ClientToServer("")
	documents = ServerToClient.getDataListProducts(data);
	System.out.println(documents);*/
	/*
	 * tạo user
	 * 	ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","register_user","UserClient");
		System.out.println(Connectclient.SendtoServer(CtS.getJsonOblect()));
	 */
	/*
	 * get_all_data
	 * 	ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","get_all_data","TableWebInfo");
		System.out.println(Connectclient.SendtoServer(CtS.getJsonOblect()));
	 */
	/*
	 * get_history_ user
	 * ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","get_history_user","HistoryUser");
		System.out.println(Connectclient.SendtoServer(CtS.getJsonOblect()));
	 * 
	 */
	
	/*
	 * get_category_parent
	 * ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","get_category_parent","CategoryParents");
	   System.out.println(Connectclient.SendtoServer(CtS.getJsonOblect()));
	 * 
	 */
	/*
	 * create_history
	 * ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","create_history","HistoryUser","__LoveCount__","https://www.dienmayxanh.com/khuyen-mai/khai-truong-sieu-thi-dien-may-xanh-binh-nhi-tien-g-972826");
	   System.out.println(Connectclient.SendtoServer(CtS.getJsonOblect()));
	 */
	
	/*
	 * get_top_most
	 * ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","get_top_most","TableWebInfo","__LoveCount__","10");
	   System.out.println(Connectclient.SendtoServer(CtS.getJsonOblect()));
	 */
	/*
	 * get_pagination
	 * ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","get_pagination","TableWebInfo","Event","*",10,1);
		System.out.println(Connectclient.SendtoServer(CtS.getJsonOblect()));
	 */
	
	ClientToServer_V1 CtS = new ClientToServer_V1("VuongDucTuan","user","get_all_data","TableWebInfo");
}
}
