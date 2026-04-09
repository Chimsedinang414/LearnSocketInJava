package UDP;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPDateTimeClient  {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddrees = InetAddress.getByName("localhost");

        byte[] sendData;
        byte[] receiverData = new byte[1024];

        sendData = "getDate".getBytes(StandardCharsets.UTF_8);

        //Send request
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddrees, 8386);
        clientSocket.send(sendPacket);
        //Receiver request
        DatagramPacket receiverPacket = new DatagramPacket(receiverData, receiverData.length);
        clientSocket.receive(receiverPacket);

        String str = new String(receiverPacket.getData(), 0, receiverPacket.getLength());
        System.out.println("Server return: " + str);
        clientSocket.close();


    }
}