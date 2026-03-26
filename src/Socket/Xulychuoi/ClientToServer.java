package Socket.Xulychuoi;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientToServer extends JFrame {

    JTextArea txtInput;
    JTextArea txtResult;
    JButton btnSend;

    //  Constructor
    public ClientToServer() {
        setTitle("Client TCP - Xử lý chuỗi");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Input
        txtInput = new JTextArea(3, 20);
        txtInput.setBorder(BorderFactory.createTitledBorder("Nhập chuỗi"));

        // Result
        txtResult = new JTextArea();
        txtResult.setEditable(false);
        txtResult.setBorder(BorderFactory.createTitledBorder("Kết quả"));

        // Button
        btnSend = new JButton("Gửi");

        // Layout
        add(new JScrollPane(txtInput), BorderLayout.NORTH);
        add(new JScrollPane(txtResult), BorderLayout.CENTER);
        add(btnSend, BorderLayout.SOUTH);

        // Action
        btnSend.addActionListener(e -> sendData());

        setVisible(true);
    }

    //  Gửi dữ liệu lên Server
    public void sendData() {
        try {
            Socket socket = new Socket("localhost", 5000);

            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String input = txtInput.getText().trim();

            if (input.isEmpty()) {
                txtResult.setText(" Vui lòng nhập chuỗi!");
                return;
            }

            // Gửi dữ liệu
            out.write(input);
            out.newLine();
            out.flush();

            // Nhận nhiều dòng từ server
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }

            txtResult.setText(response.toString());

            socket.close();

        } catch (Exception ex) {
            txtResult.setText(" Lỗi kết nối tới Server!\n" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientToServer();
        });
    }
}