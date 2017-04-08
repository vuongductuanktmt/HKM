package Database.HKM;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class jsoups {
	public org.jsoup.nodes.Document doc;
	public org.bson.Document seeddata = new org.bson.Document();
	public static String info = "";
	public static boolean check_update = false;

	public jsoups(String url) {
		try {
			doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

		} catch (Exception e) {
		}
	}

	public static Date getTime() throws ParseException {
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", new Locale("vi", "VN"));
		Date today = new Date(System.currentTimeMillis());
		String gettime = timeFormat.format(today.getTime());
		timeFormat.setTimeZone(TimeZone.getTimeZone("+7GMT"));
		return timeFormat.parse(gettime);
	}

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	// function lấy thông tin web
	public static void trandata(String website_name) throws IOException, ParseException {
		// --------------------------------------------------------------------------------------------------------------------------------
		// Khai báo biến string chứa các thông tin lấy được
		String __HomePage__; // <Địa chỉ trang chủ>
		String __Title__; // <Tên của sản phẩm hoặc sự kiện>
		String __LinkTitle__; // <Địa chỉ lúc sản phẩm hoặc sự kiện>
		String __LinkImage__; // <Địa chỉ của hình ảnh>
		String __CurrentPrice__; // <Gía sản phẩm hiện tại>
		String __OldPrice__; // <Gía sản phẩm cũ>
		String __Status__; // <Trạng thại là hàng giảm giá hay bán chạy hay là
		boolean __Delete__ = false;
		Date __DateInsert__; // <Ngày Thêm vào CSDL>
		String __CategoryChildName__; // <Tên của chỉ mục cha>
		String __CategoryChildLink__; // <Địa chỉ của chỉ mục con>
		long __ViewCount__; // <Số lượng View>
		long __LoveCount__; // <Số lượng Love>
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// khai báo các biến tạm cho quá trình lấy thông tin
		jsoups jsp;
		jsoups temp_jsp;
		Elements links;
		String temp;
		Pattern r;
		Matcher m;
		String pattern;
		boolean Check_Insert = false;
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// ----------------------------------------------------------------------------------------------------------------------------
		System.out.println("Đang lấy dữ liệu trên trang " + website_name + "...");
		MongoDB data = new MongoDB("TableWebInfo");
		MongoDB data_category = new MongoDB("CategoryChild");
		MongoDB data_error = new MongoDB("Error");
		switch (website_name) {
		case "tiki":// lấy dữ liệu ở trang tiki
			__HomePage__ = "https://tiki.vn";
			// ============================================================================================
			try {
				jsp = new jsoups("https://tiki.vn/hot");// connect website tiki
				links = jsp.doc.select(".broadcast-banner").select("a[rel=nofollow]");
				for (Element link : links) {
					__LinkTitle__ = link.attr("href");
					__Title__ = link.select(".lazy").attr("title");
					__LinkImage__ = link.select(".lazy").attr("data-src");
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__).append(
								"__Content__",
								"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null vào lúc " + getTime() + "")
								.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Event vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}

			// ============================================================================================
			try {
				jsp = new jsoups("https://tiki.vn/");
				temp_jsp = jsp;
				for (int j = 1; j <= 25; j++) {
					jsp = temp_jsp;
					links = jsp.doc.select("#home-bestseller-" + j).select(".swiper-wrapper").select(".swiper-slide")
							.select(".product-item").select("a");
					for (Element link : links) {
						__LinkTitle__ = link.attr("href");
						__Title__ = link.attr("title");
						Elements link1 = link.select("span[class=image]").select("img[class=lazy]");
						__LinkImage__ = link1.attr("src");
						if (__LinkImage__.isEmpty()) {
							__LinkImage__ = link1.attr("data-src");
						}
						Elements links2 = link.select(".price-sale");
						__CurrentPrice__ = links2.text().substring(0, links2.text().indexOf("₫") + 1);
						__OldPrice__ = links2.select(".price-regular").text();
						if (__OldPrice__.isEmpty()) {
							__OldPrice__ = __CurrentPrice__;
						}
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Selling";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
											"Có vẻ cấu trúc của Trang Selling đã thay đổi __Title__ = null vào lúc "
													+ getTime() + "")
											.append("__Status__", false));
						}
						if (__LinkTitle__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkTitle__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (__LinkImage__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkImage__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (__LinkImage__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Selling đã thay đổi __CurrentPrice__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (__LinkImage__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Selling đã thay đổi __OldPrice__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
							Check_Insert = true;
						} else {
							Check_Insert = false;
						}
						// ============================================================================================
						jsp = new jsoups(__LinkTitle__);
						Elements links_category = jsp.doc.select(".breadcrumb-wrap").select("ol[class=breadcrumb]")
								.select("a");
						for (Element categorys : links_category) {
							__CategoryChildName__ = categorys.text();
							if (__CategoryChildName__.equals("Trang chủ")) {
								__CategoryChildName__ = "Tiki";
							}
							__CategoryChildLink__ = "https://tiki.vn" + categorys.attr("href");
							if (!__Title__.equals(__CategoryChildName__)) {
								if (Check_Insert) {
									data_category.Collection.insertOne(
											new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
													.append("__CategoryChildLink__", __CategoryChildLink__)
													.append("__LinkTitle__", __LinkTitle__)
													.append("__CategoryParentName__", "Chung"));
								}
							}
						}

					}
					jsp = temp_jsp;
					links = jsp.doc.select("#home-discount-" + j).select(".swiper-wrapper").select(".swiper-slide")
							.select(".product-item").select("a");
					if (links.isEmpty()) {
					} else {
						for (Element link : links) {
							__LinkTitle__ = link.attr("href");
							__Title__ = link.attr("title");
							Elements link1 = link.select("span[class=image]").select("img[class=lazy]");
							__LinkImage__ = link1.attr("src");
							if (__LinkImage__.isEmpty()) {
								__LinkImage__ = link1.attr("data-src");
							}
							Elements links2 = link.select(".price-sale");
							__CurrentPrice__ = links2.text().substring(0, links2.text().indexOf("₫") + 1);
							__OldPrice__ = links2.select(".price-regular").text();
							if (__OldPrice__.isEmpty()) {
								__OldPrice__ = __CurrentPrice__;
							}
							__ViewCount__ = 0;
							__LoveCount__ = 0;
							__Status__ = "Promotion";
							__DateInsert__ = getTime();
							if (__Title__.isEmpty()) {
								data_error.Collection.insertOne(
										new org.bson.Document("__Website__", __HomePage__).append("__Content__",
												"Có vẻ cấu trúc của Trang Promotion đã thay đổi __Title__ = null vào lúc "
														+ getTime() + "")
												.append("__Status__", false));
							}
							if (__LinkTitle__.isEmpty()) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkTitle__ = null vào lúc "
														+ getTime() + "")
										.append("__Date__", getTime()).append("__Status__", false));
							}
							if (__LinkImage__.isEmpty()) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkImage__ = null vào lúc "
														+ getTime() + "")
										.append("__Date__", getTime()).append("__Status__", false));
							}
							if (__LinkImage__.isEmpty()) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang Promotion đã thay đổi __CurrentPrice__ = null vào lúc "
														+ getTime() + "")
										.append("__Date__", getTime()).append("__Status__", false));
							}
							if (__LinkImage__.isEmpty()) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang Promotion đã thay đổi __OldPrice__ = null vào lúc "
														+ getTime() + "")
										.append("__Date__", getTime()).append("__Status__", false));
							}
							if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
								data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
										.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
										.append("__LinkImage__", __LinkImage__)
										.append("__CurrentPrice__", __CurrentPrice__)
										.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
										.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
										.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
								Check_Insert = true;
							} else {
								Check_Insert = false;
							}
							// ============================================================================================
							jsp = new jsoups(__LinkTitle__);
							Elements links_category = jsp.doc.select(".breadcrumb-wrap").select("ol[class=breadcrumb]")
									.select("a");
							for (Element categorys : links_category) {
								__CategoryChildName__ = categorys.text();
								if (__CategoryChildName__.equals("Trang chủ")) {
									__CategoryChildName__ = "Tiki";
								}
								__CategoryChildLink__ = "https://tiki.vn" + categorys.attr("href");
								if (!__Title__.equals(__CategoryChildName__)) {
									if (Check_Insert) {
										data_category.Collection.insertOne(
												new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
														.append("__CategoryChildLink__", __CategoryChildLink__)
														.append("__LinkTitle__", __LinkTitle__)
														.append("__CategoryParentName__", "Chung"));
									}
								}
							}
						}
					}

				}
				// ============================================================================================
				info += "Lấy và thêm dữ liệu thành công Website: https://tiki.vn có.\n";
				System.out.println("Lấy và thêm dữ liệu thành công Website: https://tiki.vn");
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			// ============================================================================================
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "lazada":
			__HomePage__ = "http://www.lazada.vn";
			__LinkImage__ = null;
			// ============================================================================================
			try {
				jsp = new jsoups("http://www.lazada.vn/");
				temp_jsp = jsp;
				links = jsp.doc.select("div[data-tab-id=1]").select(".c-promo-grid__cell-content").select("a");
				pattern = "http(.*?)jpg";
				for (Element link : links) {
					__LinkTitle__ = link.attr("href");
					__Title__ = link.attr("data-title");
					temp = link.toString();
					// Tao mot doi tuong Pattern
					r = Pattern.compile(pattern);
					// Tao doi tuong matcher.
					m = r.matcher(temp);
					while (m.find())
						__LinkImage__ = m.group(0);
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__).append(
								"__Content__",
								"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null vào lúc " + getTime() + "")
								.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
				}
				// ============================================================================================
				jsp = temp_jsp;
				links = jsp.doc.select("div[data-tracking-bounder=5]").select(".c-mp-section__body")
						.select(".c-slider__wrapper").select(".c-slider__slide").select("a");
				for (Element link : links) {
					__LinkTitle__ = link.attr("href");
					__Title__ = link.attr("data-tracking-product-name");
					Elements link1 = link.select("a");
					temp = link1.toString();
					// Tao mot doi tuong Pattern
					r = Pattern.compile(pattern);
					// Tao doi tuong matcher.
					m = r.matcher(temp);
					while (m.find())
						__LinkImage__ = m.group(0);
					Elements link2 = link.select(".c-product-item__price");
					__CurrentPrice__ = link2.text().substring(0, link2.text().indexOf('D') + 1);
					__OldPrice__ = link.select(".c-product-item__price-old").text();
					if (__OldPrice__.isEmpty()) {
						__OldPrice__ = __CurrentPrice__;
					}
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Selling";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection
								.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __Title__ = null vào lúc "
												+ getTime() + "")
										.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __CurrentPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __OldPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
					// ============================================================================================

					jsp = new jsoups(__LinkTitle__);
					Elements links_category = jsp.doc.select(".header__breadcrumb").select("ul[class=breadcrumb__list]")
							.select("li[class=breadcrumb__item]").select("a[class=breadcrumb__item-anchor]");
					for (Element link_category : links_category) {
						__CategoryChildLink__ = link_category.attr("href");
						__CategoryChildName__ = link_category.attr("title");
						if (!__Title__.equals(__CategoryChildName__)) {
							if (Check_Insert) {
								data_category.Collection
										.insertOne(new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung"));
							}
						}
					}
					// =========================================================================================
				}
				// ============================================================================================
				jsp = temp_jsp;
				links = jsp.doc.select("div[data-tracking-bounder=2]").select(".c-mp-section__body")
						.select(".c-slider__wrapper").select(".c-slider__slide").select("a");
				for (Element link : links) {
					__LinkTitle__ = link.attr("href");
					__Title__ = link.attr("data-tracking-product-name");
					Elements link1 = link.select("a");
					temp = link1.toString();
					// Tao mot doi tuong Pattern
					r = Pattern.compile(pattern);
					// Tao doi tuong matcher.
					m = r.matcher(temp);
					while (m.find())
						__LinkImage__ = m.group(0);
					Elements link2 = link.select(".c-product-item__price");
					__CurrentPrice__ = link2.text().substring(0, link2.text().indexOf('D') + 1);
					__OldPrice__ = link.select(".c-product-item__price-old").text();
					if (__OldPrice__.isEmpty()) {
						__OldPrice__ = __CurrentPrice__;
					}
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Promotion";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection
								.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __Title__ = null vào lúc "
												+ getTime() + "")
										.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __CurrentPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __OldPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
					// ============================================================================================
					jsp = new jsoups(__LinkTitle__);
					Elements links_category = jsp.doc.select(".header__breadcrumb").select("ul[class=breadcrumb__list]")
							.select("li[class=breadcrumb__item]").select("a[class=breadcrumb__item-anchor]");
					for (Element link_category : links_category) {
						__CategoryChildLink__ = link_category.attr("href");
						__CategoryChildName__ = link_category.attr("title");
						if (!__Title__.equals(__CategoryChildName__)) {
							if (Check_Insert) {
								data_category.Collection
										.insertOne(new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung"));
							}
						}
					}
					// =========================================================================================
				}
				// ============================================================================================
				info += "Lấy và thêm dữ liệu thành công Website: http://www.lazada.vn.\n";
				System.out.println("Lấy và thêm dữ liệu thành công Website: http://www.lazada.vn");
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "sendo":
			__HomePage__ = "https://www.sendo.vn";
			try {
				jsp = new jsoups("https://www.sendo.vn/su-kien");
				links = jsp.doc.select(".block-event").first().select(".event-group-list").select(".event-item")
						.select("a[class=img]");
				for (Element link : links) {
					__LinkTitle__ = link.attr("href");
					Elements temp1 = link.select("img");
					__Title__ = temp1.attr("alt");
					__LinkImage__ = temp1.attr("src");
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__).append(
								"__Content__",
								"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null vào lúc " + getTime() + "")
								.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Event vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			// ============================================================================================
			try {
				jsp = new jsoups("https://www.sendo.vn/ban-chay");
				links = jsp.doc.select(".sale-products").select(".overflow_box");
				for (Element link : links) {
					Elements links1 = link.select("a[class=img_product]");
					__LinkTitle__ = links1.attr("href");
					Elements temp1 = link.select(".imgtodrag");
					__Title__ = temp1.attr("alt");
					__LinkImage__ = temp1.attr("src");
					if (__LinkImage__.isEmpty()) {
						__LinkImage__ = temp1.attr("data-original");
					}
					Elements links2 = link.select(".price-box");
					__CurrentPrice__ = links2.select(".current_price").text();
					__OldPrice__ = links2.select(".old_price").text();
					if (__OldPrice__.isEmpty()) {
						__OldPrice__ = __CurrentPrice__;
					}
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Selling";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection
								.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __Title__ = null vào lúc "
												+ getTime() + "")
										.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __CurrentPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __OldPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
					// ============================================================================================
					jsp = new jsoups(__LinkTitle__);
					Elements links_category = jsp.doc.select(".block-pro-attr").select(".detail-breadcrumb")
							.select("div[itemprop=itemListElement]").select("a[itemprop=item]");
					for (Element categorys : links_category) {
						__CategoryChildName__ = categorys.attr("title");
						__CategoryChildLink__ = categorys.attr("href");
						if (!__Title__.equals(__CategoryChildName__)) {
							if (Check_Insert) {
								data_category.Collection
										.insertOne(new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung"));
							}
						}
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			try {
				for (int page = 1; page < 6; page++) {
					jsp = new jsoups("https://www.sendo.vn/khuyen-mai/?p=" + page);
					links = jsp.doc.select(".sale-products").select(".box_product");
					for (Element promotional : links) {
						Elements links2 = promotional.select(".price-box");
						__CurrentPrice__ = links2.select(".current_price").text();
						__OldPrice__ = links2.select(".old_price").text();
						if (__OldPrice__.isEmpty()) {
							__OldPrice__ = __CurrentPrice__;
						}
						Elements links3 = promotional.select(".productPreview").select("a[class=img_product]");
						__LinkTitle__ = links3.attr("href");
						Elements promotinal_title = links3.select("img[width=250]");
						__Title__ = promotinal_title.attr("alt");
						__LinkImage__ = promotinal_title.attr("src");
						if (__LinkImage__.isEmpty()) {
							__LinkImage__ = promotinal_title.attr("data-original");
						}
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Promotion";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
											"Có vẻ cấu trúc của Trang Promotion đã thay đổi __Title__ = null vào lúc "
													+ getTime() + "")
											.append("__Status__", false));
						}
						if (__LinkTitle__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkTitle__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (__LinkImage__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkImage__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (__LinkImage__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Promotion đã thay đổi __CurrentPrice__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (__LinkImage__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Promotion đã thay đổi __OldPrice__ = null vào lúc "
													+ getTime() + "")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
							Check_Insert = true;
						} else {
							Check_Insert = false;
						}
						// ============================================================================================
						jsp = new jsoups(__LinkTitle__);
						Elements links_category = jsp.doc.select(".block-pro-attr").select(".detail-breadcrumb")
								.select("div[itemprop=itemListElement]").select("a[itemprop=item]");
						for (Element categorys : links_category) {
							__CategoryChildName__ = categorys.attr("title");
							__CategoryChildLink__ = categorys.attr("href");
							if (!__Title__.equals(__CategoryChildName__)) {
								if (Check_Insert) {
									data_category.Collection.insertOne(
											new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
													.append("__CategoryChildLink__", __CategoryChildLink__)
													.append("__LinkTitle__", __LinkTitle__)
													.append("__CategoryParentName__", "Chung"));
								}
							}
						}
					}

				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			// ============================================================================================
			info += "Lấy và thêm dữ liệu thành công Website: https://www.sendo.vn.\n";
			System.out.println("Lấy và thêm dữ liệu thành công Website: https://www.sendo.vn");
			// ============================================================================================
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "vatgia":
			__HomePage__ = "http://www.vatgia.com";
			try {
				jsp = new jsoups("http://www.vatgia.com/home/");
				links = jsp.doc.select("#carousel_event_promotions").select(".carousel-inner").select(".item")
						.select("a[target=_blank]");
				for (Element link : links) {
					r = Pattern.compile("http(.*)");
					__LinkTitle__ = link.attr("href");
					// Tao doi tuong matcher.
					m = r.matcher(__LinkTitle__);
					if (m.find()) {
						__LinkTitle__ = m.group(0);
					} else {
						__LinkTitle__ = "http://www.vatgia.com" + link.attr("href");
					}
					Elements temp1 = link.select("img[class=main_banner]");
					__Title__ = temp1.attr("alt");
					if (__Title__.isEmpty()) {
						temp_jsp = new jsoups(__LinkTitle__);
						__Title__ = temp_jsp.doc.title();
					}
					__LinkImage__ = temp1.attr("src");
					m = r.matcher(__LinkImage__);
					if (m.find()) {
						__LinkImage__ = m.group(0);
					} else {
						__LinkImage__ = "http://www.vatgia.com" + __LinkImage__;
					}
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__).append(
								"__Content__",
								"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null vào lúc " + getTime() + "")
								.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Event vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}

			try {
				jsp = new jsoups("http://vatgia.com/home/");
				temp_jsp = jsp;
				links = jsp.doc.select("#home_product_top_week").select(".load_content").select(".product_thumb_view")
						.select(".block");
				for (Element link : links) {
					Elements links1 = link.select("div[class=picture_main]").select("a[class=picture_link]");
					Elements temp1 = links1.select("img");
					__Title__ = temp1.attr("alt");
					if (__Title__.isEmpty()) {
						__Title__ = link.select(".name").select("a").attr("title");
						links1 = link.select("div[class=picture_main]").select("a");
						__LinkTitle__ = "http://vatgia.com" + link.select(".name").select("a").attr("href");
						__LinkImage__ = links1.attr("src");
						if (__LinkImage__.isEmpty()) {
							__LinkImage__ = links1.attr("data-original");
						}
					} else {
						__LinkTitle__ = "http://vatgia.com" + links1.attr("href");
						__LinkImage__ = temp1.attr("src");
						if (__LinkImage__.isEmpty()) {
							__LinkImage__ = temp1.attr("data-original");
						}
					}
					Elements links2 = link.select(".price");
					__CurrentPrice__ = links2.text();
					__OldPrice__ = __CurrentPrice__;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Selling";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection
								.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __Title__ = null vào lúc "
												+ getTime() + "")
										.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __CurrentPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __OldPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
					// ============================================================================================

					jsp = new jsoups(__LinkTitle__);
					Elements links_category = jsp.doc.select("#header_navigate").select(".container_width").select("a");
					for (Element categorys : links_category) {
						__CategoryChildName__ = categorys.text();
						__CategoryChildLink__ = "http://vatgia.com" + categorys.attr("href");
						if (!__Title__.equals(__CategoryChildName__)) {
							if (Check_Insert) {
								data_category.Collection
										.insertOne(new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung"));
							}
						}
					}
				}
				// ============================================================================================
				links = temp_jsp.doc.select("#product_promotion_carousel").select(".load_content")
						.select(".product_thumb_view").select(".block");
				for (Element link : links) {
					Elements links1 = link.select("div[class=picture_main]").select("a");
					__LinkImage__ = links1.attr("src");
					__LinkTitle__ = "http://www.vatgia.com" + links1.attr("href");
					if (__LinkImage__.isEmpty()) {
						__LinkImage__ = links1.attr("data-original");
					}
					if (__LinkImage__.isEmpty()) {
						links1 = link.select("div[class=picture_main]").select("a").select("img");
						__LinkImage__ = links1.attr("src");
						if (__LinkImage__.isEmpty()) {
							__LinkImage__ = links1.attr("data-original");
						}

					}
					Elements links2 = link.select(".price");
					__CurrentPrice__ = links2.text().substring(0, links2.text().indexOf("₫") + 1);
					__OldPrice__ = links2.select(".old_price").text();
					__Title__ = link.select(".name").select("a").text();
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Promotion";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection
								.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __Title__ = null vào lúc "
												+ getTime() + "")
										.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __CurrentPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __OldPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
					// ============================================================================================
					jsp = new jsoups(__LinkTitle__);
					Elements links_category = jsp.doc.select("#header_navigate").select(".container_width").select("a");
					for (Element categorys : links_category) {
						__CategoryChildName__ = categorys.text();
						__CategoryChildLink__ = "http://vatgia.com" + categorys.attr("href");
						if (!__Title__.equals(__CategoryChildName__)) {
							if (Check_Insert) {
								data_category.Collection
										.insertOne(new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung"));
							}
						}
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			// ============================================================================================
			info += "Lấy và thêm dữ liệu thành công Website: http://www.vatgia.com.\n";
			System.out.println("Lấy và thêm dữ liệu thành công Website: http://www.vatgia.com");
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "fptshop":
			__HomePage__ = "http://fptshop.com.vn";
			try {
				jsp = new jsoups("http://fptshop.com.vn/");
				temp_jsp = jsp;
				links = jsp.doc.select(".fm-homesl").select("div[class=swiper-slide]").select("a");
				for (Element link : links) {
					__LinkTitle__ = link.attr("href");
					Elements link1 = link.select("img");
					__Title__ = link1.attr("title");
					if (__Title__.isEmpty()) {
						__Title__ = link1.attr("alt");
					}
					__LinkImage__ = "http:" + link1.attr("data-original");
					if (__LinkImage__.equals("http:")) {
						__LinkImage__ = "http:" + link1.attr("data-src");
					}
					if (__LinkImage__.equals("http:")) {
						__LinkImage__ = "http:" + link1.attr("src");
					}
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__).append(
								"__Content__",
								"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null vào lúc " + getTime() + "")
								.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Event vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}

			// ============================================================================================================================
			try {
				jsp = new jsoups("http://fptshop.com.vn/bang-xep-hang");
				links = jsp.doc.select(".main-product").select(".bslr-item").select(".bslr-item-info");

				for (Element link : links) {
					Elements links1 = link.select(".bslr-item-image").select("a");
					__LinkTitle__ = "http://fptshop.com.vn" + links1.attr("href");
					__LinkImage__ = links1.select("img").attr("src");
					__Title__ = link.select(".bslr-item-content").text();
					__CurrentPrice__ = link.select(".bslr-item-price").text();
					__OldPrice__ = __CurrentPrice__;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Selling";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection
								.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __Title__ = null vào lúc "
												+ getTime() + "")
										.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __CurrentPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Selling đã thay đổi __OldPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}

					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
					// ============================================================================================
					jsp = new jsoups(__LinkTitle__);
					Elements links_category = jsp.doc.select("li > a:not([onclick^=ga])").select("a[href^=/]");
					for (Element categorys : links_category) {
						__CategoryChildName__ = categorys.text();
						__CategoryChildLink__ = "http://fptshop.com.vn" + links_category.attr("href");

						if (!__Title__.equals(__CategoryChildName__)) {
							if (Check_Insert) {
								data_category.Collection
										.insertOne(new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung"));
							}
						}
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			// ============================================================================================================================
			try {
				jsp = new jsoups("http://fptshop.com.vn/xa-hang");
				links = jsp.doc.select("div[class=xh-lpmain]").select(".xh-lpitem").select(".xh-lpbox")
						.select(".xh-lpiinfo");
				for (Element link : links) {
					Elements link1 = link.select("ul").select("li").last().select("a");
					__Title__ = link1.attr("data-name");
					__LinkImage__ = link1.attr("data-image");
					__LinkTitle__ = link.select("a").attr("href");
					__CurrentPrice__ = link.select(".xh-pribox").select(".xh-pri").first().text();
					__OldPrice__ = link.select(".xh-pribox").select(".xh-pri").last().text();
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Promotion";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection
								.insertOne(new org.bson.Document("__Website__", __HomePage__).append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __Title__ = null vào lúc "
												+ getTime() + "")
										.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __CurrentPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Promotion đã thay đổi __OldPrice__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
					// ============================================================================================
					jsp = new jsoups(__LinkTitle__);
					Elements links_category = jsp.doc.select("li > a:not([onclick^=ga])").select("a[href^=/]");
					for (Element categorys : links_category) {
						__CategoryChildName__ = categorys.text();
						__CategoryChildLink__ = "http://fptshop.com.vn" + links_category.attr("href");

						if (!__Title__.equals(__CategoryChildName__)) {
							if (Check_Insert) {
								data_category.Collection
										.insertOne(new org.bson.Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung"));
							}
						}
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			// ============================================================================================
			info += "Lấy và thêm dữ liệu thành công Website: http://fptshop.com.vn.\n";
			System.out.println("Lấy và thêm dữ liệu thành công Website: http://fptshop.com.vn");
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "dienmayxanh":
			__HomePage__ = "https://www.dienmayxanh.com";
			// ============================================================================================
			try {
				jsp = new jsoups("https://www.dienmayxanh.com/khuyen-mai");
				links = jsp.doc.select("ul[class=ul-list_newspro]").select("a[class=news clearfix]");
				for (Element link : links) {
					__LinkTitle__ = "https://www.dienmayxanh.com" + link.attr("href");
					__Title__ = link.attr("title");
					Elements temp1 = link.select(".news-img");
					__LinkImage__ = temp1.attr("data-original");
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__).append(
								"__Content__",
								"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null vào lúc " + getTime() + "")
								.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Event vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			// =============================================================================================
			info += "Lấy và thêm dữ liệu thành công Website: https://www.dienmayxanh.com.";
			System.out.println("Lấy và thêm dữ liệu thành công Website: https://www.dienmayxanh.com");
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "adayroi":
			__HomePage__ = "https://adayroi.com";
			try {
				jsp = new jsoups("https://deal.adayroi.com/?_ga=1.34851425.1703483956.1487772191");
				links = jsp.doc.select("#top_carousel").select("div[class=carousel-inner]").select(".item")
						.select("a:not(.carousel-control)");
				for (Element link : links) {
					__LinkTitle__ = "https://deal.adayroi.com/" + link.attr("href");
					jsp = new jsoups(__LinkTitle__);
					__Title__ = jsp.doc.title();
					Elements temp1 = link.select(".thumbnail").select("img[data-event=lazyload]");
					__LinkImage__ = temp1.attr("data-original");
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__).append(
								"__Content__",
								"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null vào lúc " + getTime() + "")
								.append("__Status__", false));
					}
					if (__LinkTitle__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}
					if (__LinkImage__.isEmpty()) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__",
										"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null vào lúc "
												+ getTime() + "")
								.append("__Date__", getTime()).append("__Status__", false));
					}

					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__));
						Check_Insert = true;
					} else {
						Check_Insert = false;
					}
				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Event vào lúc " + getTime() + "")
						.append("__Date__", getTime()).append("__Status__", false));
			}
			info += "Lấy và thêm dữ liệu thành công Website: https://adayroi.com.\n";
		default:
			break;
		}

	}
}
