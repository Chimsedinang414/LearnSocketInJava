package Socket.Caculator;
import java.io.*;
import java.net.*;
import java.util.Stack;


public class ServerCalculator {
    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(8386);
            System.out.println("Server running...");

            while (true) {
                Socket client = server.accept();
                System.out.println("Client connecte");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                BuferedReader out = new BufferedReader(

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
