package Socket.bai3;
import  java.io.BufferedReader;
import  java.io.DataOutputStream;
import  java.io.DataInputStream;
import  java.io.PrintWriter;
import  java.net.Socket;

public class Connect2Server {
     public static void main(String[]  args) {
         try {
             // Wifi LAN : webQuan
//             pass :anhquandz0401
             Socket soc = new Socket("192.168.10.62",3000);
             DataInputStream dis = new DataInputStream(soc.getInputStream());
             System.out.println(dis.readUTF());
         }catch (Exception e) {
             System.out.println("Errro");
         }
     }
}