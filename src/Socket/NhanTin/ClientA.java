package Socket.NhanTin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientA {
    private JFrame frame;
    private JTextArea textArea;
    private JTextField textField;
    private JButton sendButton;

    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientA() {
        username = JOptionPane.showInputDialog("Nhập tên của bạn: ");

        frame = new JFrame("Chat room - " + username);
        textArea = new JTextArea();
        textArea.setEditable(false);

        textField = new JTextField();
        sendButton = new JButton("Gửi");

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(textArea),BorderLayout.CENTER);

        JPanel panel =new JPanel(new BorderLayout());
        panel.add(textField,BorderLayout.CENTER);
        panel.add(sendButton,BorderLayout.EAST);


        frame.add(panel,BorderLayout.SOUTH);

        frame.setSize(400,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(frame);
        frame.setVisible(true);

        ActionListener sendAction  = e -> sendMessage();
        sendButton.addActionListener(sendAction);
        textField.addActionListener(sendAction);

        connectToServer();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("127.0.0.1", 5000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(username);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        textArea.append(msg + "\n");
                    }
                } catch (IOException e) {
                    textArea.append("Mất kết nối đến server\n");
                }
            }).start();
        } catch (IOException e) {
            JOptionPane.showInputDialog("Không có tín hiệu từ server! ");
        }
    }
    private void sendMessage() {
        String msg = textField.getText();
        if(!msg.isEmpty()) {
            out.println(msg);
            textField.setText("");

        }
    }
    public static void main(String[] args) {
        new ClientA();
    }
}
