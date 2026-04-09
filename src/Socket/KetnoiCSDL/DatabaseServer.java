package Socket.KetnoiCSDL;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseServer {
    private static final int SERVER_PORT = 6000;
    private static final List<Record> records = new ArrayList<>();

    public static void main(String[] args) {
        loadSampleData();
        startServer();
    }

    private static void loadSampleData() {
        records.add(new Record(1, "Nguyen Van A", "Sales", 4500000));
        records.add(new Record(2, "Tran Thi B", "Accounting", 5200000));
        records.add(new Record(3, "Le Van C", "IT", 7000000));
        records.add(new Record(4, "Pham Thi D", "HR", 4800000));
        records.add(new Record(5, "Hoang Van E", "Logistics", 5200000));
    }

    private static void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server TCP lắng nghe trên cổng " + SERVER_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException ex) {
            System.err.println("Lỗi server: " + ex.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        String clientAddress = clientSocket.getRemoteSocketAddress().toString();
        System.out.println("Kết nối tới client: " + clientAddress);
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

            String request = input.readUTF();
            String response = processRequest(request);
            output.writeUTF(response);
            output.flush();
            System.out.println("Yêu cầu từ client: " + request);
        } catch (IOException ex) {
            System.err.println("Lỗi xử lý client " + clientAddress + ": " + ex.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static String processRequest(String request) {
        if (request == null || request.trim().isEmpty()) {
            return "ERROR|Yêu cầu rỗng";
        }
        String lower = request.trim().toLowerCase(Locale.ROOT);
        if (lower.equals("get all")) {
            return buildResponse(records);
        }
        if (lower.startsWith("get id ")) {
            try {
                int id = Integer.parseInt(request.substring(7).trim());
                return records.stream()
                        .filter(r -> r.id == id)
                        .map(Record::toProtocolString)
                        .findFirst()
                        .orElse("NO_RESULT");
            } catch (NumberFormatException ex) {
                return "ERROR|ID phải là số nguyên";
            }
        }
        if (lower.startsWith("search ")) {
            String term = request.substring(7).trim().toLowerCase(Locale.ROOT);
            List<Record> matches = new ArrayList<>();
            for (Record record : records) {
                if (record.name.toLowerCase(Locale.ROOT).contains(term)
                        || record.department.toLowerCase(Locale.ROOT).contains(term)) {
                    matches.add(record);
                }
            }
            if (matches.isEmpty()) {
                return "NO_RESULT";
            }
            return buildResponse(matches);
        }
        return "ERROR|Lệnh không hợp lệ";
    }

    private static String buildResponse(List<Record> list) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).toProtocolString());
            if (i < list.size() - 1) {
                builder.append("\n");
            }
        }
        return builder.toString();
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

        String toProtocolString() {
            return id + "|" + name + "|" + department + "|" + salary;
        }
    }
}
