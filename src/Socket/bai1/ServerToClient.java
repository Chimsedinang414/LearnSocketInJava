package Socket.bai1;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerToClient {
    public static void main(String[] args) throws Exception {
        System.out.println("Server đang chạy...");

        ServerSocket server = new ServerSocket(7788);

        while (true) {
            Socket socket = server.accept(); // chờ client kết nối
            System.out.println("Có client kết nối");

            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());

            // Gửi thời gian hiện tại
            String time = new Date().toString();
            dout.writeUTF(time);

            dout.flush();
            socket.close();
        }
    }
}