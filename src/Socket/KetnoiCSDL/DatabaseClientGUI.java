package Socket.KetnoiCSDL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DatabaseClientGUI extends JFrame {
    private final JTextField hostField = new JTextField("localhost", 12);
    private final JTextField portField = new JTextField("6000", 6);
    private final JTextField queryField = new JTextField(18);
    private final JLabel statusLabel = new JLabel("Chưa kết nối");
    private final DefaultTableModel tableModel;

    public DatabaseClientGUI() {
        super("Client Truy xuất cơ sở dữ liệu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        topPanel.add(new JLabel("Host:"));
        topPanel.add(hostField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);
        JButton connectButton = new JButton("Kiểm tra kết nối");
        connectButton.addActionListener(e -> checkServerConnection());
        topPanel.add(connectButton);

        topPanel.add(new JLabel("Tìm kiếm/ID:"));
        topPanel.add(queryField);
        JButton allButton = new JButton("Tất cả");
        allButton.addActionListener(e -> queryServer("GET ALL"));
        JButton searchIdButton = new JButton("Tìm theo ID");
        searchIdButton.addActionListener(e -> queryById());
        JButton searchNameButton = new JButton("Tìm theo tên/phòng ban");
        searchNameButton.addActionListener(e -> queryByName());

        topPanel.add(allButton);
        topPanel.add(searchIdButton);
        topPanel.add(searchNameButton);

        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Tên", "Phòng ban", "Lương"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(720, 260));
        add(new JScrollPane(table), BorderLayout.CENTER);

        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(240, 240, 240));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void checkServerConnection() {
        String host = hostField.getText().trim();
        int port = parsePort();
        if (port < 0) {
            return;
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            showStatus("Kết nối thành công tới " + host + ":" + port, Color.GREEN.darker());
        } catch (IOException ex) {
            showStatus("Không kết nối được: " + ex.getMessage(), Color.RED.darker());
        }
    }

    private void queryById() {
        String text = queryField.getText().trim();
        if (text.isEmpty()) {
            showStatus("Nhập ID cần tìm", Color.RED.darker());
            return;
        }
        queryServer("GET ID " + text);
    }

    private void queryByName() {
        String text = queryField.getText().trim();
        if (text.isEmpty()) {
            showStatus("Nhập tên hoặc phòng ban cần tìm", Color.RED.darker());
            return;
        }
        queryServer("SEARCH " + text);
    }

    private int parsePort() {
        try {
            return Integer.parseInt(portField.getText().trim());
        } catch (NumberFormatException ex) {
            showStatus("Port phải là số", Color.RED.darker());
            return -1;
        }
    }

    private void queryServer(String request) {
        String host = hostField.getText().trim();
        int port = parsePort();
        if (port < 0) {
            return;
        }

        try (Socket socket = new Socket(host, port);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream input = new DataInputStream(socket.getInputStream())) {

            output.writeUTF(request);
            output.flush();

            String response = input.readUTF();
            if (response.startsWith("ERROR|")) {
                showStatus(response.substring(6), Color.RED.darker());
                return;
            }
            if (response.equals("NO_RESULT") || response.trim().isEmpty()) {
                showStatus("Không tìm thấy bản ghi phù hợp", Color.ORANGE.darker());
                updateTable(new ArrayList<>());
                return;
            }

            updateTable(parseResponse(response));
            showStatus("Đã nhận dữ liệu từ server", Color.BLUE.darker());
        } catch (IOException ex) {
            showStatus("Lỗi giao tiếp: " + ex.getMessage(), Color.RED.darker());
        }
    }

    private List<Record> parseResponse(String response) {
        List<Record> list = new ArrayList<>();
        String[] lines = response.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split("\\|", 4);
            if (parts.length < 4) {
                continue;
            }
            try {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1];
                String department = parts[2];
                double salary = Double.parseDouble(parts[3].trim());
                list.add(new Record(id, name, department, salary));
            } catch (NumberFormatException ignored) {
            }
        }
        return list;
    }

    private void updateTable(List<Record> records) {
        tableModel.setRowCount(0);
        for (Record record : records) {
            tableModel.addRow(new Object[]{record.id, record.name, record.department, record.salary});
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private static class Record {
        final int id;
        final String name;
        final String department;
        final double salary;

        Record(int id, String name, String department, double salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.salary = salary;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseClientGUI gui = new DatabaseClientGUI();
            gui.setVisible(true);
        });
    }
}
