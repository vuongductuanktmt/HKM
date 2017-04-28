package App.HKM.Data;

public class Category {
	private String __Title__;
	private String __Href__;
	private String __CategoryParentName__;

	public Category(String __Title__, String __Href__, String __CategoryParentName__) {
		super();
		this.__Title__ = __Title__;
		this.__Href__ = __Href__;
		this.__CategoryParentName__ = __CategoryParentName__;
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

	public String get__CategoryParentName__() {
		return __CategoryParentName__;
	}

	public void set__CategoryParentName__(String __CategoryParentName__) {
		this.__CategoryParentName__ = __CategoryParentName__;
	}
}
