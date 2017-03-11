/**
 * 
 */
/**
 * @author huu21
 *
 */
package sv;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.sound.sampled.Line;

import sv.DataOutputExample;
 
public class SimpleServerProgram {
 
   public static void main(String args[]) throws IOException {
 
       ServerSocket listener = null;
 
       System.out.println("Server is waiting to accept user...");
       int clientNumber = 0;
       // Mở một ServerSocket tại cổng 9999.
       // Chú ý bạn không thể chọn cổng nhỏ hơn 1023 nếu không là người dùng
       // đặc quyền (privileged users (root)).
       try {
           listener = new ServerSocket(9999);
       } catch (IOException e) {
           System.out.println(e);
           System.exit(1);
       }
  
       try {
           while (true) {

               // Chấp nhận một yêu cầu kết nối từ phía Client.
               // Đồng thời nhận được một đối tượng Socket tại server.
        	// Su dung multithread
               // Khi co 1 client gui yeu cau toi thi se tao ra 1 thread phuc vu client do
               Socket socketOfServer = listener.accept();
               new ServiceThread(socketOfServer, clientNumber++).start();
           }
       } finally {
           listener.close();
       }
 
   }
 
   private static void log(String message) {
       System.out.println(message);
   }
   private static class ServiceThread extends Thread {
 
       private int clientNumber;
       private Socket socketOfServer;
       public ServiceThread(Socket socketOfServer, int clientNumber) {
           this.clientNumber = clientNumber;
           this.socketOfServer = socketOfServer;
           // Log
           log("New connection with client# " + this.clientNumber + " at " + socketOfServer);
       }
       public void run() {
           try {
               // Mở luồng vào ra trên Socket tại Server.
               BufferedReader is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
               BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
               while (true) {
            	   String line = is.readLine();
                  if (line.equals("REQUEST"))
                  { 
                	  // Đọc dữ liệu tới server (Do client gửi tới).
                      // Ghi vào luồng đầu ra của Socket tại Server.
                      // (Nghĩa là gửi tới Client).
                     os.write("SEVER >> client  "+ new Date() );
                      // Kết thúc dòng              
                      os.newLine();
                      // Đẩy dữ liệu đi
                      os.flush();
                  }
                   // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                   if ( line.compareTo("QUIT")==0 ) {
                       os.write("Stop connection with client# " + this.clientNumber + " at " + socketOfServer);
                       log("Stop connection with client# " + this.clientNumber + " at " + socketOfServer);
                       os.newLine();
                       os.flush();
                       break;
                   }
               }
 
           } catch (IOException e) {
               System.out.println(e);
               e.printStackTrace();
           }
       }
   }
}