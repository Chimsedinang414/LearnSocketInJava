package Socket.Caculator;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ClientCaculator {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JTextArea output;
    private JTextField input;

    public ClientCaculator() {
        // Cấu hình Giao diện suwing
        JFrame frame = new JFrame("Máy tính TCP - Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 400);
        frame.setLayout(new BorderLayout(10, 10));

        // Khu vực hiển thị kết quả
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(output);


        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        input = new JTextField();
        JButton send = new JButton("Tính toán");
        inputPanel.add(new JLabel(" Nhập biểu thức: "), BorderLayout.WEST);
        inputPanel.add(input, BorderLayout.CENTER);
        inputPanel.add(send, BorderLayout.EAST);

        // Thêm vào Frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Kết nối Server
        connectServer();

        //  Sự kiện nút bấm
        send.addActionListener(e -> sendData());
        input.addActionListener(e -> sendData());
    }

    private void connectServer() {
        try {
            socket = new Socket("localhost", 8386);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            output.append("Đã kết nối tới Server thành công!\n");
        } catch (Exception e) {
            output.append("Lỗi: Không thể kết nối tới Server (Hãy chắc chắn Server đã chạy).\n");
        }
    }

    private void sendData() {
        try {
            String expr = input.getText().trim();
            if (expr.isEmpty()) return;

            if (out != null) {
                out.println(expr); // Gửi tới Server
                String res = in.readLine();

                output.append("> Biểu thức: " + expr + "\n");
                output.append("  => " + res + "\n\n");
                input.setText("");
                input.requestFocus();
            } else {
                output.append("Chưa có kết nối tới Server!\n");
            }
        } catch (Exception ex) {
            output.append("Lỗi: Mất kết nối tới Server.\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientCaculator());
    }
}