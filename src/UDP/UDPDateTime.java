package UDP;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPDateTime {
    public static void main(String[] agrs) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(8386);
        byte[] receiverData = new byte[1024];

        System.out.println("UDP Server running...");

        while(true) {
            DatagramPacket receiverPacket = new DatagramPacket(receiverData,receiverData.length);

            serverSocket.receive(receiverPacket);

            String request = new String(receiverPacket.getData(),0,receiverPacket.getLength());

            System.out.println("CLient sending..."+ request);

            // Pick up time now
            String time = new SimpleDateFormat("dd/MM/yyyy HH:mm::ss").format(new Date());
            byte[] sendData = time.getBytes(StandardCharsets.UTF_8);
            InetAddress clientIp = receiverPacket.getAddress();
            int clientPort = receiverPacket.getPort();

            DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,clientIp,clientPort);
            serverSocket.send(sendPacket);
        }
    }
}