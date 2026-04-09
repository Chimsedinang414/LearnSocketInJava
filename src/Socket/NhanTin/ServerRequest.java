package Socket.NhanTin;
import java.io.*;
import java.net.*;
import java.util.*;

public class ServerRequest {
    private static final int PORT = 5000;
    private static Set<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Đang chạy server...");

        try ( ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(true) {
                Socket socket = serverSocket.accept();
                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                new Thread(client).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void broadcast(String message) {
        for(ClientHandler client :clients) {
            client.sendMessage(message);
        }
    }
    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in ;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket =socket;
        }
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(),true);

                username = in.readLine();
                broadcast(" >> " + username  + " Đã tham gia phòng ");

                String msg;
            while((msg = in.readLine()) != null) {
                broadcast("[" + username + "]:" + msg);
            }
            }catch (IOException e) {
                System.out.println("Client rời đi ");
            }finally {
                clients.remove(this);
                broadcast(">>" + username+ " đã rời phòng");
                try { socket.close();
            }catch (IOException e) {

                }
        }
    }
    public void sendMessage(String message) {
        out.println(message);
        }
    }
}