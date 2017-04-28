package App.HKM.Data;

import java.util.Date;

public class Error {
	private String __Content__;
	private String __HomePage__;
	private Date __DateError__;

	public Error(String content, String homePage, Date dateError) {
		super();
		__Content__ = content;
		__HomePage__ = homePage;
		__DateError__ = dateError;
	}

	public String getContent() {
		return __Content__;
	}

	public void setContent(String content) {
		__Content__ = content;
	}

	public String getHomePage() {
		return __HomePage__;
	}

	public void setHomePage(String homePage) {
		__HomePage__ = homePage;
	}

	public Date getDateError() {
		return __DateError__;
	}

	public void setDateError(Date dateError) {
		__DateError__ = dateError;
	}
}
