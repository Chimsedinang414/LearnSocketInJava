//package Socket.NhanTin;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.*;
//import java.net.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class ClientA {
//    private JFrame frame;
//    private JTextArea textArea;
//    private JTextField textField;
//    private JButton sendButton;
//
//    private PrintWriter out;
//    private BufferedReader in;
//    private String username;
//
//    public ClientA() {
//        // nhập username
//        username = JOptionPane.showInputDialog("Nhập tên của bạn:");
//
//        frame = new JFrame("Chat Room - " + username);
//        textArea = new JTextArea();
//        textArea.setEditable(false);
//
//        textField = new JTextField();
//        sendButton = new JButton("Gửi");
//
//        frame.setLayout(new BorderLayout());
//        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
//
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(textField, BorderLayout.CENTER);
//        panel.add(sendButton, BorderLayout.EAST);
//
//        frame.add(panel, BorderLayout.SOUTH);
//
//        frame.setSize(400, 500);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//
//        // sự kiện gửi
//        ActionListener sendAction = e -> sendMessage();
//        sendButton.addActionListener(sendAction);
//        textField.addActionListener(sendAction);
//
//        connectToServer();
//    }
//
//    private void connectToServer() {
//        try {
//            Socket socket = new Socket("127.0.0.1", 5000);
//
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            out = new PrintWriter(socket.getOutputStream(), true);
//
//            // gửi username
//            out.println(username);
//
//            // thread nhận tin
//            new Thread(() -> {
//                try {
//                    String msg;
//                    while ((msg = in.readLine()) != null) {
//                        textArea.append(msg + "\n");
//                    }
//                } catch (IOException e) {
//                    textArea.append("Mất kết nối server\n");
//                }
//            }).start();
//
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(frame, "Không kết nối được server");
//        }
//    }
//
//    private void sendMessage() {
//        String msg = textField.getText();
//        if (!msg.isEmpty()) {
//            out.println(msg);
//            textField.setText("");
//        }
//    }
//
//    public static void main(String[] args) {
//        new ClientA();
//    }
//}
//
//
//
//
//package UDP;
//
//import java.net.*;
//import java.nio.charset.StandardCharsets;
//
//public class UDPDateTimeClient {
//    public static void main(String[] args) throws Exception {
//
//        DatagramSocket clientSocket = new DatagramSocket();
//        InetAddress IPAddress = InetAddress.getByName("localhost");
//
//        byte[] sendData;
//        byte[] receiveData = new byte[1024];
//
//        sendData = "getDate".getBytes(StandardCharsets.UTF_8);
//
//        // Gửi request
//        DatagramPacket sendPacket =
//                new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
//        clientSocket.send(sendPacket);
//
//        // Nhận phản hồi
//        DatagramPacket receivePacket =
//                new DatagramPacket(receiveData, receiveData.length);
//        clientSocket.receive(receivePacket);
//
//
//        String str = new String(receivePacket.getData(), 0, receivePacket.getLength());
//
//        System.out.println("Server trả về: " + str);
//
//        clientSocket.close();
//    }
//}
//
//package UDP;
//
//import java.net.*;
//        import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class UDPDateTime {
//    public static void main(String[] args) throws Exception {
//        DatagramSocket serverSocket = new DatagramSocket(9876);
//        byte[] receiveData = new byte[1024];
//
//        System.out.println("UDP Server đang chạy...");
//
//        while (true) {
//            DatagramPacket receivePacket =
//                    new DatagramPacket(receiveData, receiveData.length);
//
//            serverSocket.receive(receivePacket);
//
//            String request = new String(receivePacket.getData(), 0,
//                    receivePacket.getLength());
//
//            System.out.println("Client gửi: " + request);
//
//            // Lấy thời gian hiện tại
//            String time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//                    .format(new Date());
//
//            byte[] sendData = time.getBytes();
//
//            InetAddress clientIP = receivePacket.getAddress();
//            int clientPort = receivePacket.getPort();
//
//            DatagramPacket sendPacket =
//                    new DatagramPacket(sendData, sendData.length,
//                            clientIP, clientPort);
//
//            serverSocket.send(sendPacket);
//        }
//    }
//}