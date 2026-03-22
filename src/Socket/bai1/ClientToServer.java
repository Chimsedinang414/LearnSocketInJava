package Socket.bai1;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class ClientToServer {
    public static void main(String[] args) throws Exception  {
        System.out.println("Dang lay thoi gian tu server...");
        Socket socket = new Socket("localhost",7788);
        DataInputStream din = new DataInputStream(socket.getInputStream());
        String time = din.readUTF(); // Doc tuw socket vao client
        System.out.println(time); // show
        socket.close();
    }
}