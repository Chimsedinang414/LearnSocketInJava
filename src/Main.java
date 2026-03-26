package Socket.TCPIP;

import java.io.*;
import java.net.*;
import java.util.Stack;

public class ServerTCP {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server running...");

            while (true) {
                Socket client = server.accept();
                System.out.println("Client connected");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream()));

                String request = in.readLine();

                String response = handleRequest(request);

                out.write(response);
                out.newLine();
                out.flush();

                client.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Xử lý theo protocol
    static String handleRequest(String req) {
        try {
            String[] parts = req.split("\\|");

            if (parts[0].equals("CALC")) {
                String expr = parts[1];
                double result = evaluate(expr);
                return "RESULT|" + result;
            }

            return "ERROR|Unknown command";

        } catch (Exception e) {
            return "ERROR|Invalid expression";
        }
    }

    // ====== XỬ LÝ BIỂU THỨC ======

    static int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    static String infixToPostfix(String expr) {
        StringBuilder output = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (c == ' ') continue;

            if (Character.isDigit(c)) {
                while (i < expr.length() && Character.isDigit(expr.charAt(i))) {
                    output.append(expr.charAt(i++));
                }
                output.append(" ");
                i--;
            }
            else if (c == '(') stack.push(c);

            else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(')
                    output.append(stack.pop()).append(" ");
                stack.pop();
            }
            else {
                while (!stack.isEmpty() &&
                        precedence(stack.peek()) >= precedence(c)) {
                    output.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty())
            output.append(stack.pop()).append(" ");

        return output.toString();
    }

    static double evaluate(String expr) {
        String postfix = infixToPostfix(expr);
        Stack<Double> stack = new Stack<>();

        for (String token : postfix.split(" ")) {
            if (token.isEmpty()) continue;

            if (token.matches("\\d+")) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();

                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/': stack.push(a / b); break;
                }
            }
        }

        return stack.pop();
    }
}