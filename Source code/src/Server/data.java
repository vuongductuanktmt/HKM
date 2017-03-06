package Database.HKM;

public class data {
	private String website;
	private String title;
	private String even;
	private String image;

	public data(String Website, String Title, String Even, String Image) {
		this.setWebsite(Website);
		this.setTitle(Title);
		this.setEven(Even);
		this.setImage(Image);
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEven() {
		return even;
	}

	public void setEven(String even) {
		this.even = even;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


}
