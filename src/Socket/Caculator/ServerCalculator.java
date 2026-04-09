package Socket.Caculator;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerCalculator {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8386);
        System.out.println("Server running at port 8386...");

        while (true) {
            Socket socket = server.accept();
            new ClientHandler(socket).start();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String expr;
            while ((expr = in.readLine()) != null) {
                String result = evaluate(expr);
                out.println(result);
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }

    // XỬ LÝ BIỂU THỨC
    private String evaluate(String expr) {
        try {
            return "Result = " + calculate(expr);
        } catch (Exception e) {
            return "Error";
        }
    }

    private int calculate(String s) {
        Stack<Integer> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == ' ') continue;

            // số nhiều chữ số
            if (Character.isDigit(c)) {
                int num = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + (s.charAt(i) - '0');
                    i++;
                }
                i--;
                values.push(num);
            }

            else if (c == '(') {
                ops.push(c);
            }

            else if (c == ')') {
                while (ops.peek() != '(') {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.pop();
            }

            else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(c)) {
                    values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private int applyOp(char op, int b, int a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return b == 0 ? 0 : a / b;
        }
        return 0;
    }
}