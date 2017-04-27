package Crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import Server.HKM.MongoDB;
import jline.internal.Log;

public class jsoups {
	public org.jsoup.nodes.Document doc;
	public static List<String> info = new ArrayList<String>();
	public static boolean check_update = false;

	public jsoups(String url) throws MalformedURLException, IOException {
		try {
			doc = Jsoup.connect(url)
					.userAgent(
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					.postDataCharset("UTF-8").timeout(10000).get();
		} catch (Exception e) {
			doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
		}
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

	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	// function lấy thông tin web
	public static void trandata(String website_name)
			throws IOException, ParseException, AddressException, MessagingException, InterruptedException {
		// --------------------------------------------------------------------------------------------------------------------------------
		// Khai báo biến string chứa các thông tin lấy được
		String __HomePage__; // <Địa chỉ trang chủ>
		String __Title__; // <Tên của sản phẩm hoặc sự kiện>
		String __LinkTitle__; // <Địa chỉ lúc sản phẩm hoặc sự kiện>
		String __LinkImage__; // <Địa chỉ của hình ảnh>
		String __CurrentPrice__; // <Gía sản phẩm hiện tại>
		String __OldPrice__; // <Gía sản phẩm cũ>
		String __Status__; // <Trạng thại là hàng giảm giá hay bán chạy hay là
		boolean __Delete__ = false; //
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
		String[] moneys = { "đ", "VND", "VNĐ" };
		String moneytrue = "₫";
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// ----------------------------------------------------------------------------------------------------------------------------
		System.out.println("Đang lấy dữ liệu trên trang " + website_name + "...");
		MongoDB data = new MongoDB("TableWebInfo");
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
					__Title__ = link.select("img").attr("alt");
					__LinkImage__ = link.select("img").attr("data-src");
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();

					if (__Title__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty())) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true).append("__CheckNew__", true));
						 
						Log.warn("............................." + __HomePage__ + ".............................\n");
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
					} else {
						 
					}
				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Event").append("__Error__", e.getMessage())
							.append("__Date__", getTime()).append("__Status__", false));
				}
			}

			// ============================================================================================
			try {
				jsp = new jsoups("https://tiki.vn/");
				temp_jsp = jsp;
				for (int j = 1; j <= 25; j++) {
					try {
						jsp = temp_jsp;
						links = jsp.doc.select("#home-bestseller-" + j).select(".swiper-wrapper")
								.select(".swiper-slide").select(".product-item").select("a");
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
							for (String money : moneys) {
								__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
										.replaceAll(",", ".");
								__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
										.replaceAll(",", ".");
							}
							__ViewCount__ = 0;
							__LoveCount__ = 0;
							__Status__ = "Selling";
							__DateInsert__ = getTime();
							if (__Title__.isEmpty()) {
								if (!data_error.CheckExistsRecord(new Document("__Content__",
										"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__LinkTitle__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __LinkTitle__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__LinkImage__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __LinkImage__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __LinkImage__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__CurrentPrice__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __CurrentPrice__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
								__CurrentPrice__ = "Đang cập nhật";
							}
							if (__OldPrice__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __OldPrice__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __OldPrice__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
								__OldPrice__ = "Đang cập nhật";
							}
							if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
									&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
											|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
								ArrayList<Document> category = new ArrayList<Document>();
								jsp = new jsoups(__LinkTitle__);
								Elements links_category = jsp.doc.select(".breadcrumb-wrap")
										.select("ol[class=breadcrumb]").select("a");
								for (Element categorys : links_category) {
									__CategoryChildName__ = categorys.text();
									if (__CategoryChildName__.equals("Trang chủ")) {
										continue;
									}
									__CategoryChildLink__ = "https://tiki.vn" + categorys.attr("href");
									if (!__Title__.equals(__CategoryChildName__)) {
					
											System.out.println("............................." + __HomePage__
													+ ".............................");
											category.add(new Document("__CategoryChildName__", __CategoryChildName__)
													.append("__CategoryChildLink__", __CategoryChildLink__)
													.append("__LinkTitle__", __LinkTitle__)
													.append("__CategoryParentName__", "Chung")
													.append("__Status__", false));
											System.out.println(
													".............................Category......................................");
											System.out.println("__CategoryChildName__: " + __CategoryChildName__);
											System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);

									}
								}
								data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
										.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
										.append("__LinkImage__", __LinkImage__)
										.append("__CurrentPrice__", __CurrentPrice__)
										.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
										.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
										.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
										.append("__CheckNew__", true).append("__Category__", category));
								System.out.println("__Status__: " + __Status__);
								System.out.println("__Title__: " + __Title__);
								System.out.println("__LinkTitle__: " + __LinkTitle__);
								System.out.println("__LinkImage__: " + __LinkImage__);
								System.out.println("__CurrentPrice__: " + __CurrentPrice__);
								System.out.println("__OldPrice__: " + __OldPrice__);
								System.out.println("__DateInsert__: " + __DateInsert__);
								System.out.println(
										"---------------------------------------------------------------------------");
							}
							// ============================================================================================

						}
					} catch (Exception e) {
						if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__", "Không thể kết nối và lấy Products Selling")
									.append("__Error__", e.getMessage()).append("__Date__", getTime())
									.append("__Status__", false));
						}
					}
					try {
						jsp = temp_jsp;
						links = jsp.doc.select("#home-discount-" + j).select(".swiper-wrapper").select(".swiper-slide")
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
							for (String money : moneys) {
								__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
										.replaceAll(",", ".");
								__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
										.replaceAll(",", ".");
							}
							__ViewCount__ = 0;
							__LoveCount__ = 0;
							__Status__ = "Promotion";
							__DateInsert__ = getTime();
							if (__Title__.isEmpty()) {
								if (!data_error.CheckExistsRecord(new Document("__Content__",
										"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__LinkTitle__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __LinkTitle__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__LinkImage__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __LinkImage__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __LinkImage__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__CurrentPrice__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __CurrentPrice__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__OldPrice__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __OldPrice__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __OldPrice__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
									&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
											|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
								ArrayList<Document> category = new ArrayList<Document>();
								jsp = new jsoups(__LinkTitle__);
								Elements links_category = jsp.doc.select(".breadcrumb-wrap")
										.select("ol[class=breadcrumb]").select("a");
								for (Element categorys : links_category) {
									__CategoryChildName__ = categorys.text();
									if (__CategoryChildName__.equals("Trang chủ")) {
										continue;
									}
									if (!__Title__.equals(__CategoryChildName__)) {
									__CategoryChildLink__ = "https://tiki.vn" + categorys.attr("href");
											System.out.println("............................." + __HomePage__
													+ ".............................");
											category.add(new Document("__CategoryChildName__", __CategoryChildName__)
													.append("__CategoryChildLink__", __CategoryChildLink__)
													.append("__LinkTitle__", __LinkTitle__)
													.append("__CategoryParentName__", "Chung")
													.append("__Status__", false));
											System.out.println(
													".............................Category......................................");
											System.out.println("__CategoryChildName__: " + __CategoryChildName__);
											System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);

									}
								}
								data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
										.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
										.append("__LinkImage__", __LinkImage__)
										.append("__CurrentPrice__", __CurrentPrice__)
										.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
										.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
										.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
										.append("__CheckNew__", true).append("__Category__", category));
								System.out.println("__Status__: " + __Status__);
								System.out.println("__Title__: " + __Title__);
								System.out.println("__LinkTitle__: " + __LinkTitle__);
								System.out.println("__LinkImage__: " + __LinkImage__);
								System.out.println("__CurrentPrice__: " + __CurrentPrice__);
								System.out.println("__OldPrice__: " + __OldPrice__);
								System.out.println("__DateInsert__: " + __DateInsert__);
								System.out.println(
										"---------------------------------------------------------------------------");
							}
						}
					} catch (Exception e) {
						if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__", "Không thể kết nối và lấy Products Promotion")
									.append("__Error__", e.getMessage()).append("__Date__", getTime())
									.append("__Status__", false));
						}
					}

				}
			} catch (Exception e) {
				data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
						.append("__Content__", "Không thể kết nối và lấy Products").append("__Date__", getTime())
						.append("__Status__", false));
			}
			// ============================================================================================
			info.add("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			Log.info("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "lazada":
			__HomePage__ = "http://www.lazada.vn";
			__LinkImage__ = null;
			// ============================================================================================
			try {
				jsp = new jsoups("http://www.lazada.vn/");
				temp_jsp = jsp;
				pattern = "http(.*?)jpg";
				try {
					links = jsp.doc.select("div[data-tab-id=1]").select(".c-promo-grid__cell-content").select("a");
					for (Element link : links) {
						__LinkTitle__ = link.attr("abs:href");
						__Title__ = new jsoups(__LinkTitle__).doc.title();
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
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Event đã thay đổi __Title__ = null")
									.append("__Status__", false));
						}
						if (__LinkTitle__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (__LinkImage__.isEmpty()) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang Event đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty())) {
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true));
							Log.warn(
									"............................." + __HomePage__ + ".............................\n");
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
							 
						} else {
							 
						}
					}
				} catch (Exception e) {
					if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__", "Không thể kết nối và lấy Event")
								.append("__Error__", e.getMessage()).append("__Date__", getTime())
								.append("__Status__", false));
					}
				}

				// ============================================================================================
				try {
					for (int i = 1; i < 11; i++) {
						jsp = new jsoups("http://www.lazada.vn/deal-gia-soc/?page=" + i);
						links = jsp.doc.select("div[data-qa-locator=product-item]");
						for (Element link : links) {
							__LinkTitle__ = link.select(".c-product-card__img-placeholder").select("a")
									.attr("abs:href");
							temp = link.select(".c-product-card__img-placeholder").toString();
							// Tao mot doi tuong Pattern
							r = Pattern.compile(pattern);
							// Tao doi tuong matcher.
							m = r.matcher(temp);
							while (m.find())
								__LinkImage__ = m.group(0);
							__Title__ = link.select(".c-product-card__description").select("a").text();
							__CurrentPrice__ = link.select(".c-product-card__price-block")
									.select(".c-product-card__price-final").text();
							__OldPrice__ = link.select(".c-product-card__price-block")
									.select(".c-product-card__old-price").text();
							for (String money : moneys) {
								__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
										.replaceAll(",", ".");
								__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
										.replaceAll(",", ".");

							}
							__ViewCount__ = 0;
							__LoveCount__ = 0;
							__Status__ = "Selling";
							__DateInsert__ = getTime();
							if (__Title__.isEmpty()) {
								if (!data_error.CheckExistsRecord(new Document("__Content__",
										"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__LinkTitle__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __LinkTitle__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__LinkImage__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __LinkImage__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __LinkImage__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
							}
							if (__CurrentPrice__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __CurrentPrice__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
								__CurrentPrice__ = "Đang cập nhật";
							}
							if (__OldPrice__.isEmpty()) {
								if (!data_error
										.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
												+ __Status__ + " đã thay đổi __OldPrice__ = null"))) {
									data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __OldPrice__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
								}
								__OldPrice__ = "Đang cập nhật";
							}
							if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
									&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
											|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
								ArrayList<Document> category = new ArrayList<Document>();
								jsp = new jsoups(__LinkTitle__);
								Elements links_category = jsp.doc.select(".header__breadcrumb")
										.select("ul[class=breadcrumb__list]").select("li[class=breadcrumb__item]")
										.select("a[class=breadcrumb__item-anchor]");
								for (Element link_category : links_category) {
									__CategoryChildLink__ = link_category.attr("href");
									__CategoryChildName__ = link_category.attr("title");
									if (!__Title__.equals(__CategoryChildName__)) {

										System.out.println("............................." + __HomePage__
												+ ".............................");
										category.add(new Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung")
												.append("__Status__", false));
										System.out.println(
												".............................Category......................................");
										System.out.println("__CategoryChildName__: " + __CategoryChildName__);
										System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);

										}
								}
								data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
										.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
										.append("__LinkImage__", __LinkImage__)
										.append("__CurrentPrice__", __CurrentPrice__)
										.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
										.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
										.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
										.append("__CheckNew__", true).append("__Category__", category));
								System.out.println("__Status__: " + __Status__);
								System.out.println("__Title__: " + __Title__);
								System.out.println("__LinkTitle__: " + __LinkTitle__);
								System.out.println("__LinkImage__: " + __LinkImage__);
								System.out.println("__CurrentPrice__: " + __CurrentPrice__);
								System.out.println("__OldPrice__: " + __OldPrice__);
								System.out.println("__DateInsert__: " + __DateInsert__);
								System.out.println(
										"---------------------------------------------------------------------------");
							}
							// =========================================================================================
						}
					}

				} catch (Exception e) {
					if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__", "Không thể kết nối và lấy Products Selling")
								.append("__Error__", e.getMessage()).append("__Date__", getTime())
								.append("__Status__", false));
					}
				}
				// ============================================================================================
				try {
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
						for (String money : moneys) {
							__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
									.replaceAll(",", ".");
							__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
									".");
						}
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Promotion";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __Title__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkTitle__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkTitle__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkImage__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkImage__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__CurrentPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
									+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __CurrentPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__OldPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __OldPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
										|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
							ArrayList<Document> category = new ArrayList<Document>();
							jsp = new jsoups(__LinkTitle__);
							Elements links_category = jsp.doc.select(".header__breadcrumb")
									.select("ul[class=breadcrumb__list]").select("li[class=breadcrumb__item]")
									.select("a[class=breadcrumb__item-anchor]");
							for (Element link_category : links_category) {
								__CategoryChildLink__ = link_category.attr("href");
								__CategoryChildName__ = link_category.attr("title");
								if (!__Title__.equals(__CategoryChildName__)) {
									System.out.println("............................." + __HomePage__
											+ ".............................");
									category.add(new Document("__CategoryChildName__", __CategoryChildName__)
											.append("__CategoryChildLink__", __CategoryChildLink__)
											.append("__LinkTitle__", __LinkTitle__)
											.append("__CategoryParentName__", "Chung")
											.append("__Status__", false));
									System.out.println(
											".............................Category......................................");
									System.out.println("__CategoryChildName__: " + __CategoryChildName__);
									System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);
								}
							}
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true).append("__Category__", category));
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
						}
						// =========================================================================================
					}
				} catch (Exception e) {
					if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__", "Không thể kết nối và lấy Products Promotion")
								.append("__Error__", e.getMessage()).append("__Date__", getTime())
								.append("__Status__", false));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			// ============================================================================================
			info.add("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			Log.info("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
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
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty())) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true));
						Log.warn("............................." + __HomePage__ + ".............................\n");
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
						 
					} else {
						 
					}
				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Event").append("__Error__", e.getMessage())
							.append("__Date__", getTime()).append("__Status__", false));
				}
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
					for (String money : moneys) {
						__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
								.replaceAll(",", ".");
						__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
								".");
					}
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Selling";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__CurrentPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __CurrentPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						__CurrentPrice__ = "Đang cập nhật";
					}
					if (__OldPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __OldPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						__OldPrice__ = "Đang cập nhật";
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
									|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
						ArrayList<Document> category = new ArrayList<Document>();
						jsp = new jsoups(__LinkTitle__);
						Elements links_category = jsp.doc.select(".block-pro-attr").select(".detail-breadcrumb")
								.select("div[itemprop=itemListElement]").select("a[itemprop=item]");
						for (Element categorys : links_category) {
							__CategoryChildName__ = categorys.attr("title");
							__CategoryChildLink__ = categorys.attr("href");
							if (__CategoryChildName__.equals("Sendo")) {
								continue;
							}
							if (!__Title__.equals(__CategoryChildName__)) {
								System.out.println("............................." + __HomePage__
										+ ".............................");
								category.add(new Document("__CategoryChildName__", __CategoryChildName__)
										.append("__CategoryChildLink__", __CategoryChildLink__)
										.append("__LinkTitle__", __LinkTitle__)
										.append("__CategoryParentName__", "Chung")
										.append("__Status__", false));
								System.out.println(
										".............................Category......................................");
								System.out.println("__CategoryChildName__: " + __CategoryChildName__);
								System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);
							}
						}
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true).append("__Category__", category));
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
					}
					// ========================================================================================
				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Products Selling")
							.append("__Error__", e.getMessage()).append("__Date__", getTime())
							.append("__Status__", false));
				}
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
						for (String money : moneys) {
							__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
									.replaceAll(",", ".");
							__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
									".");
						}
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Promotion";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __Title__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkTitle__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkTitle__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkImage__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkImage__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__CurrentPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
									+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __CurrentPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__OldPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __OldPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
										|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
							ArrayList<Document> category = new ArrayList<Document>();
							jsp = new jsoups(__LinkTitle__);
							Elements links_category = jsp.doc.select(".block-pro-attr").select(".detail-breadcrumb")
									.select("div[itemprop=itemListElement]").select("a[itemprop=item]");
							for (Element categorys : links_category) {
								__CategoryChildName__ = categorys.attr("title");
								__CategoryChildLink__ = categorys.attr("href");
								if (__CategoryChildName__.equals("Sendo")) {
									continue;
								}
								if (!__Title__.equals(__CategoryChildName__)) {
										System.out.println("............................." + __HomePage__
												+ ".............................");
										category.add(new Document("__CategoryChildName__", __CategoryChildName__)
												.append("__CategoryChildLink__", __CategoryChildLink__)
												.append("__LinkTitle__", __LinkTitle__)
												.append("__CategoryParentName__", "Chung")
												.append("__Status__", false));
										System.out.println(
												".............................Category......................................");
										System.out.println("__CategoryChildName__: " + __CategoryChildName__);
										System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);

									}
							}
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true).append("__Category__", category));
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
						}
						// =====================================================================
					}

				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Products Promotion")
							.append("__Error__", e.getMessage()).append("__Date__", getTime())
							.append("__Status__", false));
				}
			}
			// ============================================================================================
			info.add("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			Log.info("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			// ============================================================================================
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "vatgia":
			__HomePage__ = "http://www.vatgia.com";
			try {
				jsp = new jsoups("http://www.vatgia.com/home/");
				temp_jsp = jsp;
				try {

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
							__LinkTitle__ = link.attr("abs:href");
						}
						Elements temp1 = link.select("img[class=main_banner]");
						__Title__ = temp1.attr("alt");
						if (__Title__.isEmpty()) {
							temp_jsp = new jsoups(__LinkTitle__);
							__Title__ = temp_jsp.doc.title();
						}
						__LinkImage__ = temp1.attr("abs:src");
						m = r.matcher(__LinkImage__);
						if (m.find()) {
							__LinkImage__ = m.group(0);
						}
						__CurrentPrice__ = null;
						__OldPrice__ = null;
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Event";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __Title__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkTitle__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkTitle__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkImage__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkImage__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty())) {
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true));
							Log.warn(
									"............................." + __HomePage__ + ".............................\n");
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
							 
						} else {
							 
						}
					}
				} catch (Exception e) {
					if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__", "Không thể kết nối và lấy Event")
								.append("__Error__", e.getMessage()).append("__Date__", getTime())
								.append("__Status__", false));
					}
				}
				try {
					jsp = temp_jsp;
					links = jsp.doc.select("#home_product_top_week").select(".load_content")
							.select(".product_thumb_view").select(".block");
					for (Element link : links) {
						Elements links1 = link.select("div[class=picture_main]").select("a[class=picture_link]");
						Elements temp1 = links1.select("img");
						__Title__ = temp1.attr("alt");
						if (__Title__.isEmpty()) {
							__Title__ = link.select(".name").select("a").attr("title");
							links1 = link.select("div[class=picture_main]").select("a");
							__LinkTitle__ = link.select(".name").select("a").attr("abs:href");
							__LinkImage__ = links1.attr("src");
							if (__LinkImage__.isEmpty()) {
								__LinkImage__ = links1.attr("data-original");
							}
						} else {
							__LinkTitle__ = links1.attr("abs:href");
							__LinkImage__ = temp1.attr("src");
							if (__LinkImage__.isEmpty()) {
								__LinkImage__ = temp1.attr("data-original");
							}
						}
						Elements links2 = link.select(".price");
						__CurrentPrice__ = links2.text();
						__OldPrice__ = __CurrentPrice__;
						for (String money : moneys) {
							__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
									.replaceAll(",", ".");
							__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
									".");
						}
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Selling";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __Title__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkTitle__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkTitle__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkImage__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkImage__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__CurrentPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
									+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __CurrentPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
							__CurrentPrice__ = "Đang cập nhật";
						}
						if (__OldPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __OldPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
							__OldPrice__ = "Đang cập nhật";
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
										|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
							ArrayList<Document> category = new ArrayList<Document>();
							jsp = new jsoups(__LinkTitle__);
							Elements links_category = jsp.doc.select("#header_navigate").select(".container_width")
									.select("a");
							for (Element categorys : links_category) {
								__CategoryChildName__ = categorys.text();
								__CategoryChildLink__ = "http://vatgia.com" + categorys.attr("href");
								if (!__Title__.equals(__CategoryChildName__)) {
									System.out.println("............................." + __HomePage__
											+ ".............................");
									category.add(new Document("__CategoryChildName__", __CategoryChildName__)
											.append("__CategoryChildLink__", __CategoryChildLink__)
											.append("__LinkTitle__", __LinkTitle__)
											.append("__CategoryParentName__", "Chung")
											.append("__Status__", false));
									System.out.println(
											".............................Category......................................");
									System.out.println("__CategoryChildName__: " + __CategoryChildName__);
									System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);
								}
							}
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true).append("__Category__", category));
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
						}
						// ==============================================================================
					}
				} catch (Exception e) {
					if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__", "Không thể kết nối và lấy Products Selling")
								.append("__Error__", e.getMessage()).append("__Date__", getTime())
								.append("__Status__", false));
					}
				}
				try {
					jsp = temp_jsp;
					// ============================================================================================
					links = jsp.doc.select("#product_promotion_carousel").select(".load_content")
							.select(".product_thumb_view").select(".block");
					for (Element link : links) {
						Elements links1 = link.select("div[class=picture_main]").select("a");
						__LinkImage__ = links1.attr("abs:src");
						__LinkTitle__ = links1.attr("abs:href");
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
						for (String money : moneys) {
							__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
									.replaceAll(",", ".");
							__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
									".");
						}
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Promotion";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __Title__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkTitle__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkTitle__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkImage__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkImage__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__CurrentPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
									+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __CurrentPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__OldPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __OldPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
										|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
							ArrayList<Document> category = new ArrayList<Document>();
							jsp = new jsoups(__LinkTitle__);
							Elements links_category = jsp.doc.select("#header_navigate").select(".container_width")
									.select("a");
							for (Element categorys : links_category) {
								__CategoryChildName__ = categorys.text();
								__CategoryChildLink__ = "http://vatgia.com" + categorys.attr("href");
								if (!__Title__.equals(__CategoryChildName__)) {
									System.out.println("............................." + __HomePage__
											+ ".............................");
									category.add(new Document("__CategoryChildName__", __CategoryChildName__)
											.append("__CategoryChildLink__", __CategoryChildLink__)
											.append("__LinkTitle__", __LinkTitle__)
											.append("__CategoryParentName__", "Chung")
											.append("__Status__", false));
									System.out.println(
											".............................Category......................................");
									System.out.println("__CategoryChildName__: " + __CategoryChildName__);
									System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);

									}
							}
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true).append("__Category__", category));
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
						}
						// =================================================================================
					}
				} catch (Exception e) {
					if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
						data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
								.append("__Content__", "Không thể kết nối và lấy Products Promotion")
								.append("__Error__", e.getMessage()).append("__Date__", getTime())
								.append("__Status__", false));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			// ============================================================================================
			info.add("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			Log.info("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "fptshop":
			__HomePage__ = "http://fptshop.com.vn";
			try {
				jsp = new jsoups("http://fptshop.com.vn/");
				links = jsp.doc.select(".fshop-bntopmain").select("div[class^=f-]").select("a");
				for (Element link : links) {
					__LinkTitle__ = link.attr("abs:href");
					Elements link1 = link.select("img");
					__Title__ = link1.attr("title");
					if (__Title__.isEmpty()) {
						__Title__ = link1.attr("alt");
					}
					__LinkImage__ = link1.attr("abs:data-original");
					if (__LinkImage__.isEmpty()) {
						__LinkImage__ = link1.attr("abs:data-src");
					}
					if (__LinkImage__.isEmpty()) {
						__LinkImage__ = link1.attr("abs:src");
					}
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty())) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true));
						Log.warn("............................." + __HomePage__ + ".............................\n");
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
						 
					} else {
						 
					}
				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Event").append("__Error__", e.getMessage())
							.append("__Date__", getTime()).append("__Status__", false));
				}
			}

			// ============================================================================================================================
			try {
				jsp = new jsoups("http://fptshop.com.vn/bang-xep-hang");
				links = jsp.doc.select(".main-product").select(".bslr-item").select(".bslr-item-info");

				for (Element link : links) {
					Elements links1 = link.select(".bslr-item-image").select("a");
					__LinkTitle__ = links1.attr("abs:href");
					__LinkImage__ = links1.select("img").attr("src");
					__Title__ = link.select(".bslr-item-content").text();
					__CurrentPrice__ = link.select(".bslr-item-price").text();
					__OldPrice__ = __CurrentPrice__;
					for (String money : moneys) {
						__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
								.replaceAll(",", ".");
						__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
								".");
					}
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Selling";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__CurrentPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __CurrentPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						__CurrentPrice__ = "Đang cập nhật";
					}
					if (__OldPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __OldPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						__OldPrice__ = "Đang cập nhật";
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
									|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
						ArrayList<Document> category = new ArrayList<Document>();
						jsp = new jsoups(__LinkTitle__);
						Elements links_category = jsp.doc.select(".detail-breadcrumb").select("a");
						for (Element categorys : links_category) {
							__CategoryChildName__ = categorys.text();
							if (__CategoryChildName__.equals("Trang chủ")) {
								continue;
							}
							__CategoryChildLink__ = links_category.attr("abs:href");

							if (!__Title__.equals(__CategoryChildName__)) {
								System.out.println("............................." + __HomePage__
										+ ".............................");
								category.add(new Document("__CategoryChildName__", __CategoryChildName__)
										.append("__CategoryChildLink__", __CategoryChildLink__)
										.append("__LinkTitle__", __LinkTitle__)
										.append("__CategoryParentName__", "Chung")
										.append("__Status__", false));
								System.out.println(
										".............................Category......................................");
								System.out.println("__CategoryChildName__: " + __CategoryChildName__);
								System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);
							}
						}
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true).append("__Category__", category));
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
					}
					// ==============================================================================
				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Products Selling")
							.append("__Error__", e.getMessage()).append("__Date__", getTime())
							.append("__Status__", false));
				}
			}
			// ============================================================================================================================
			try {
				jsp = new jsoups("http://fptshop.com.vn/xa-hang");

				links = jsp.doc.select("div[class^=xh-lp]").select("a:has(img)");
				for (Element link : links) {
					__LinkTitle__ = link.attr("abs:href");
					temp_jsp = new jsoups(__LinkTitle__);
					__Title__ = temp_jsp.doc.title();
					__LinkImage__ = link.select("img").attr("abs:src");
					__CurrentPrice__ = temp_jsp.doc.select(".detail-current-price").select("strong").text();
					__OldPrice__ = temp_jsp.doc.select(".dt-xhprice").select("span").text();
					for (String money : moneys) {
						__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
								.replaceAll(",", ".");
						__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
								".");
					}
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Promotion";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__CurrentPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __CurrentPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__OldPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __OldPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
									|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
						ArrayList<Document> category = new ArrayList<Document>();
						jsp = new jsoups(__LinkTitle__);
						Elements links_category = jsp.doc.select(".detail-breadcrumb").select("a");
						for (Element categorys : links_category) {
							__CategoryChildName__ = categorys.text();
							if (__CategoryChildName__.equals("Trang chủ")) {
								continue;
							}
							__CategoryChildLink__ = links_category.attr("abs:href");
							if (!__Title__.equals(__CategoryChildName__)) {
								
								System.out.println("............................." + __HomePage__
										+ ".............................");
								category.add(new Document("__CategoryChildName__", __CategoryChildName__)
										.append("__CategoryChildLink__", __CategoryChildLink__)
										.append("__LinkTitle__", __LinkTitle__)
										.append("__CategoryParentName__", "Chung")
										.append("__Status__", false));
								System.out.println(
										".............................Category......................................");
								System.out.println("__CategoryChildName__: " + __CategoryChildName__);
								System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);
							}
						}
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true).append("__Category__", category));
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
					}
					// =================================================================================
				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Products Promotion")
							.append("__Error__", e.getMessage()).append("__Date__", getTime())
							.append("__Status__", false));
				}
			}
			// ============================================================================================
			info.add("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			Log.info("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "dienmayxanh":
			__HomePage__ = "https://www.dienmayxanh.com";
			// ============================================================================================
			try {
				for (int i = 1; i < 11; i++) {
					jsp = new jsoups("https://www.dienmayxanh.com/khuyen-mai?p=" + i);
					links = jsp.doc.select(".ul-list_newspro").select("a:has(img)");
					// System.out.println(links);
					for (Element link : links) {
						__LinkTitle__ = link.attr("abs:href");
						__Title__ = link.attr("title");
						Elements temp1 = link.select("img");
						__LinkImage__ = temp1.attr("abs:data-original");
						__CurrentPrice__ = null;
						__OldPrice__ = null;
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Event";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __Title__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkTitle__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkTitle__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkImage__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkImage__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty())) {
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true));
							Log.warn(
									"............................." + __HomePage__ + ".............................\n");
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
							 
						} else {
							 
						}
					}
				}

			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Event").append("__Error__", e.getMessage())
							.append("__Date__", getTime()).append("__Status__", false));
				}
			}
			// =============================================================================================
			info.add("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			Log.info("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			break;
		// ----------------------------------------------------------------------------------------------------------------------------
		case "adayroi":
			__HomePage__ = "https://adayroi.com";
			try {
				jsp = new jsoups("https://www.adayroi.com/?_ga=1.250547270.196677961.1491722791");
				temp_jsp = jsp;
				links = jsp.doc.select("#home-main").select("#main_features").select(".right")
						.select("a[data-position~=[0-9]+]");
				// System.out.println(links);
				for (Element link : links) {
					__LinkTitle__ = link.attr("href");
					__Title__ = new jsoups(__LinkTitle__).doc.title();
					Elements temp1 = link.select("img");
					__LinkImage__ = temp1.attr("src");
					if (__LinkImage__.isEmpty()) {
						__LinkImage__ = temp1.attr("data-src");
					}
					__CurrentPrice__ = null;
					__OldPrice__ = null;
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Event";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty())) {
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true));
						Log.warn("............................." + __HomePage__ + ".............................\n");
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
						 
					} else {
						 
					}

				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Event").append("__Error__", e.getMessage())
							.append("__Date__", getTime()).append("__Status__", false));
				}
			}
			try {
				jsp = new jsoups("https://www.adayroi.com/?_ga=1.250547270.196677961.1491722791");
				links = jsp.doc.select("li[id^=category_best_sells]").select("a");
				for (Element link : links) {
					__LinkTitle__ = link.attr("abs:href");
					temp_jsp = new jsoups(__LinkTitle__);
					__LinkImage__ = link.select("img").attr("src");
					if (__LinkImage__.isEmpty()) {
						__LinkImage__ = link.select("img").attr("data-src");
					}
					__Title__ = temp_jsp.doc.title();
					temp_jsp.doc.select(".is-free-delivery").remove();
					__CurrentPrice__ = temp_jsp.doc.select("#product_info").select("#item_prices").select(".item-price")
							.text();
					__OldPrice__ = temp_jsp.doc.select("#product_info").select("#item_prices").select(".line price")
							.select(".value original").text();
					if (__OldPrice__.isEmpty()) {
						__OldPrice__ = __CurrentPrice__;
					}
					for (String money : moneys) {
						__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
								.replaceAll(",", ".");
						__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
								".");
					}
					__ViewCount__ = 0;
					__LoveCount__ = 0;
					__Status__ = "Selling";
					__DateInsert__ = getTime();
					if (__Title__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
							data_error.Collection
									.insertOne(new org.bson.Document("__Website__", __HomePage__)
											.append("__Content__",
													"Có vẻ cấu trúc của Trang " + __Status__
															+ " đã thay đổi __Title__ = null")
											.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkTitle__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkTitle__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__LinkImage__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __LinkImage__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
					}
					if (__CurrentPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __CurrentPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						__CurrentPrice__ = "Đang cập nhật";
					}
					if (__OldPrice__.isEmpty()) {
						if (!data_error.CheckExistsRecord(new Document("__Content__",
								"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
							data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
									.append("__Content__",
											"Có vẻ cấu trúc của Trang " + __Status__
													+ " đã thay đổi __OldPrice__ = null")
									.append("__Date__", getTime()).append("__Status__", false));
						}
						__OldPrice__ = "Đang cập nhật";
					}
					if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
							&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
									|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
						ArrayList<Document> category = new ArrayList<Document>();
						jsp = new jsoups(__LinkTitle__);
						Elements links_category = jsp.doc.select("#header__breadcrumb").select(".item").select("a");
						for (Element categorys : links_category) {
							__CategoryChildLink__ = categorys.attr("abs:href");
							__CategoryChildName__ = categorys.select("span[itemprop=title]").text();
							if (__CategoryChildName__.equals("Trang chủ")) {
								continue;
							}
							if (!__Title__.equals(__CategoryChildName__)) {
								System.out.println("............................." + __HomePage__
										+ ".............................");
								category.add(new Document("__CategoryChildName__", __CategoryChildName__)
										.append("__CategoryChildLink__", __CategoryChildLink__)
										.append("__LinkTitle__", __LinkTitle__)
										.append("__CategoryParentName__", "Chung")
										.append("__Status__", false));
								System.out.println(
										".............................Category......................................");
								System.out.println("__CategoryChildName__: " + __CategoryChildName__);
								System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);
							}
						}
						data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
								.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
								.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
								.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
								.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
								.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
								.append("__CheckNew__", true).append("__Category__", category));
						System.out.println("__Status__: " + __Status__);
						System.out.println("__Title__: " + __Title__);
						System.out.println("__LinkTitle__: " + __LinkTitle__);
						System.out.println("__LinkImage__: " + __LinkImage__);
						System.out.println("__CurrentPrice__: " + __CurrentPrice__);
						System.out.println("__OldPrice__: " + __OldPrice__);
						System.out.println("__DateInsert__: " + __DateInsert__);
						System.out
								.println("---------------------------------------------------------------------------");
					}
					// ========================================================================================
				}

			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Products Selling")
							.append("__Error__", e.getMessage()).append("__Date__", getTime())
							.append("__Status__", false));
				}
			}
			try {
				String[] temp_links = {
						"https://deal.adayroi.com/deal-gia-soc-lp18?_ga=1.150884342.196677961.1491722791",
						"https://deal.adayroi.com/deal-gia-soc-lp18?gid=345",
						"https://deal.adayroi.com/deal-gia-soc-lp18?gid=346",
						"https://deal.adayroi.com/deal-gia-soc-lp18?gid=414" };
				for (String temp_link : temp_links) {
					jsp = new jsoups(temp_link);
					links = jsp.doc.select(".deal-list").select(".item-countdown").select(".style2");
					for (Element link : links) {
						Elements link1 = link.select("a[href^=/]");
						// System.out.println(link1);
						__Title__ = link1.attr("title");
						__LinkImage__ = link1.select(".lazyimg").attr("data-original");
						if (__LinkImage__.isEmpty()) {
							__LinkImage__ = link1.select(".lazyimg").attr("src");
						}
						__LinkTitle__ = link1.attr("abs:href");
						__CurrentPrice__ = link.select(".real").text();
						__OldPrice__ = link.select(".original").text();
						if (__OldPrice__.isEmpty()) {
							__OldPrice__ = __CurrentPrice__;
						}
						for (String money : moneys) {
							__CurrentPrice__ = __CurrentPrice__.replaceAll(money, moneytrue).replaceAll(" ", "")
									.replaceAll(",", ".");
							__OldPrice__ = __OldPrice__.replaceAll(money, moneytrue).replaceAll(" ", "").replaceAll(",",
									".");
						}
						__OldPrice__ = __OldPrice__.substring(0, __OldPrice__.indexOf('₫') + 1);
						__ViewCount__ = 0;
						__LoveCount__ = 0;
						__Status__ = "Promotion";
						__DateInsert__ = getTime();
						if (__Title__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __Title__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __Title__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkTitle__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkTitle__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkTitle__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__LinkImage__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __LinkImage__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __LinkImage__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__CurrentPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__", "Có vẻ cấu trúc của Trang "
									+ __Status__ + " đã thay đổi __CurrentPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __CurrentPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (__OldPrice__.isEmpty()) {
							if (!data_error.CheckExistsRecord(new Document("__Content__",
									"Có vẻ cấu trúc của Trang " + __Status__ + " đã thay đổi __OldPrice__ = null"))) {
								data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
										.append("__Content__",
												"Có vẻ cấu trúc của Trang " + __Status__
														+ " đã thay đổi __OldPrice__ = null")
										.append("__Date__", getTime()).append("__Status__", false));
							}
						}
						if (!data.CheckExistsRecord(new org.bson.Document("__LinkTitle__", __LinkTitle__))
								&& !(__Title__.isEmpty() || __LinkTitle__.isEmpty() || __LinkImage__.isEmpty()
										|| __CurrentPrice__.isEmpty() || __OldPrice__.isEmpty())) {
							ArrayList<Document> category = new ArrayList<Document>();
							jsp = new jsoups(__LinkTitle__);
							Elements links_category = jsp.doc.select(".breadcrumb").select("a");
							// System.out.println("dfdf"+jsp.doc.html());
							for (Element categorys : links_category) {
								__CategoryChildName__ = categorys.attr("title");
								__CategoryChildLink__ = categorys.attr("abs:href");
								if (__CategoryChildName__.equals("Trang chủ")) {
									continue;
								}
								if (!__Title__.equals(__CategoryChildName__)) {
									System.out.println("............................." + __HomePage__
											+ ".............................");
									category.add(new Document("__CategoryChildName__", __CategoryChildName__)
											.append("__CategoryChildLink__", __CategoryChildLink__)
											.append("__LinkTitle__", __LinkTitle__)
											.append("__CategoryParentName__", "Chung")
											.append("__Status__", false));
									System.out.println(
											".............................Category......................................");
									System.out.println("__CategoryChildName__: " + __CategoryChildName__);
									System.out.println("__CategoryChildLink__: " + __CategoryChildLink__);
								}
							}
							data.Collection.insertOne(new org.bson.Document("__HomePage__", __HomePage__)
									.append("__Title__", __Title__).append("__LinkTitle__", __LinkTitle__)
									.append("__LinkImage__", __LinkImage__).append("__CurrentPrice__", __CurrentPrice__)
									.append("__OldPrice__", __OldPrice__).append("__DateInsert__", __DateInsert__)
									.append("__Status__", __Status__).append("__ViewCount__", __ViewCount__)
									.append("__LoveCount__", __LoveCount__).append("__Delete__", __Delete__)
									.append("__CheckNew__", true).append("__Category__", category));
							System.out.println("__Status__: " + __Status__);
							System.out.println("__Title__: " + __Title__);
							System.out.println("__LinkTitle__: " + __LinkTitle__);
							System.out.println("__LinkImage__: " + __LinkImage__);
							System.out.println("__CurrentPrice__: " + __CurrentPrice__);
							System.out.println("__OldPrice__: " + __OldPrice__);
							System.out.println("__DateInsert__: " + __DateInsert__);
							System.out.println(
									"---------------------------------------------------------------------------");
						}
						// =================================================================================
					}
				}
			} catch (Exception e) {
				if (!data_error.CheckExistsRecord(new Document("__Error__", e.getMessage()))) {
					data_error.Collection.insertOne(new org.bson.Document("__Website__", __HomePage__)
							.append("__Content__", "Không thể kết nối và lấy Products Promotion")
							.append("__Error__", e.getMessage()).append("__Date__", getTime())
							.append("__Status__", false));
				}
			}

			info.add("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			Log.info("Lấy và thêm dữ liệu thành công Website: " + __HomePage__ + "\n");
			break;
		}

	}
	/*
	 * comment thêm cho tròn 2000 dòng cho đẹp các chế à :v haha
	 */
}
