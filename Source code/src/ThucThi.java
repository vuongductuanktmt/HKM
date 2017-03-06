package Database.HKM;

import java.io.IOException;
import java.text.ParseException;

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
		ThucThi tt1 = new ThucThi("sendo");
		ThucThi tt2 = new ThucThi("vatgia");
		ThucThi tt3 = new ThucThi("tiki");
		ThucThi tt4 = new ThucThi("lazada");
		ThucThi tt5 = new ThucThi("adayroi");
		ThucThi tt6 = new ThucThi("dienmayxanh");
		ThucThi tt7 = new ThucThi("fptshop");
		Thread t1 = new Thread(tt1);
		Thread t2 = new Thread(tt2);
		Thread t3 = new Thread(tt3);
		Thread t4 = new Thread(tt4);
		Thread t5 = new Thread(tt5);
		Thread t6 = new Thread(tt6);
		Thread t7 = new Thread(tt7);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		System.out.println("Main thread stopped");
	}
}