package ServerManagerData;

import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONObject;

public class ClientToServer {
	private String Request;
	private String User;
	private String Password;
	private String Securitys;
	private int PageSize;
	private int PageNumber;
	private String Var;
	private String Value;
	private String Condition;
	private String Authorities;
	private String Table;
	private String Search;
	private String Status;
	// dùng để login vào hệ thông 1000 bài hát thiếu nhi
	public ClientToServer(String request,String authorities, String user, String password, String securitys) {
		super();
		this.Request = request;
		this.User = user;
		this.Password = password;
		this.Securitys = securitys;
		this.Authorities = authorities;
	}
	//dùng để lấy dữ liệu dãng phân trang
	public ClientToServer(String request, int pageSize, int pageNumber, String var, String condition,
			String authorities, String table, String search, String status) {
		super();
		this.Request = request;
		this.PageSize = pageSize;
		this.PageNumber = pageNumber;
		this.Var = var;
		this.Condition = condition;
		this.Authorities = authorities;
		this.Table = table;
		this.Search = search;
		this.Status = status;
	}
	public ClientToServer(String request, int pageSize,
			String authorities, String table, String search, String status) {
		super();
		this.Request = request;
		this.PageSize = pageSize;
		this.Authorities = authorities;
		this.Table = table;
		this.Search = search;
		this.Status = status;
	}
	//dùng để thay đổi thông tin, tạo.
	public ClientToServer(String request,String authorities, String table,String var, String value, String condition,int search) {
		super();
		this.Search = String.valueOf(search);
		this.Var = var;
		this.Value = value;
		this.Condition = condition;
		this.Authorities = authorities;
		this.Request = request;
		this.Table = table;
	}
	//
	
	//dùng để thay đổi thông tin, tạo.
		public ClientToServer(String request,String authorities, String table,String var, String value, String condition,String search_s,int search) {
			super();
			this.Search = search_s;
			this.Var = var;
			this.Value = value;
			this.Condition = condition;
			this.Authorities = authorities;
			this.Request = request;
			this.Table = table;
		}
	public String getValueRequests() throws URISyntaxException, IOException{
		JSONObject request = new JSONObject();
		request.put("__Request__", this.getRequest()); // thông tin reuest lên server
		request.put("__User__",this.User);			// thông tin user
		request.put("__Password__",this.Password); // thông tin password của người dùng
		request.put("__Security__",this.Securitys); // Thông tin bảo mật
		request.put("__PageSize__",this.PageSize); //Số lượng record của 1 trang
		request.put("__PageNumber__",this.PageNumber); // Số trang hiện tại
		request.put("__Var__", this.Var); // tên biến
		request.put("__Value__",this.Value); // giá tri của biến
		request.put("__Condition__",this.Condition); // điều kiện để lọc
		request.put("__Authorities__",this.Authorities); // quyền sử dụng
		request.put("__Table__",this.Table); // collection truy vấn
		request.put("__Search__",this.Search); // giá trị cần tìm
		request.put("__Status__",this.Status); // thuộc loại sản phẩm nào
		return Connectclient.SendtoServer(request.toString());
	}
	
	public String getRequest() {
		return Request;
	}
	public void setRequest(String request) {
		Request = request;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getSecuritys() {
		return Securitys;
	}
	public void setSecuritys(String securitys) {
		Securitys = securitys;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public int getPageNumber() {
		return PageNumber;
	}
	public void setPageNumber(int pageNumber) {
		PageNumber = pageNumber;
	}
	public String getVar() {
		return Var;
	}
	public void setVar(String var) {
		this.Var = var;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getCondition() {
		return Condition;
	}
	public void setCondition(String condition) {
		Condition = condition;
	}
	public String getAuthorities() {
		return Authorities;
	}
	public void setAuthorities(String authorities) {
		Authorities = authorities;
	}
	
}
