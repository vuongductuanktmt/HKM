package Database.HKM;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Arrays;

public class ThucThi implements Runnable { //Thực thi đa luồng
	public String Website;

	public ThucThi(String website) {
		this.Website = website;
	}
	@Override
	public void run() {
		try {
			switch (this.Website) {
			case "sendo":
				jsoups.trandata("sendo");
				break;
			case "lazada":
				jsoups.trandata("lazada");
				break;
			case "tiki":
				jsoups.trandata("tiki");
				break;
			case "fptshop":
				jsoups.trandata("fptshop");
				break;
			case "dienmayxanh":
				jsoups.trandata("dienmayxanh");
				break;
			case "vatgia":
				jsoups.trandata("vatgia");
				break;
			case "adayroi":
				jsoups.trandata("adayroi");
				break;
			default:
				break;
			}
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException, ParseException, InterruptedException {
		System.out.println("Đợi lấy dữ liệu...");
		System.out.println("Main thread running..");
		Thread threads[] = { new Thread(new ThucThi("sendo")), new Thread(new ThucThi("tiki")),
				new Thread(new ThucThi("vatgia")), new Thread(new ThucThi("lazada")),
				new Thread(new ThucThi("adayroi")), new Thread(new ThucThi("dienmayxanh")),
				new Thread(new ThucThi("fptshop")) };
		for(int i = 0; i < Array.getLength(threads);  i++) {
		    threads[i].start();
		  }
		System.out.println("Main thread stopped");
	}
}