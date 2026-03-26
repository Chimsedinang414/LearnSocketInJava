package Socket.Xulychuoi;

import java.io.*;
import java.net.*;

public class ServerToClient {

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server running...");

            while (true) {
                Socket clientSocket = server.accept();
                System.out.println("Client connected!");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream()));

                String input = in.readLine();

                String result = processString(input);

                out.write(result);
                out.newLine();
                out.flush();

                clientSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processString(String str) {
        String reverse = new StringBuilder(str).reverse().toString();
        String upper = str.toUpperCase();
        String lower = str.toLowerCase();
        String capitalize = capitalizeWords(str);

        int wordCount = str.trim().split("\\s+").length;
        int vowelCount = countVowels(str);

        return "Chuoi dao: " + reverse +
                "\nChuoi In Hoa: " + upper +
                "\nChuoi in thuong: " + lower +
                "\nChuoi Hoa-Thuong: " + capitalize +
                "\nSo tu: " + wordCount +
                "\nSo nguyen am: " + vowelCount;
    }

    public static String capitalizeWords(String str) {
        String[] words = str.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String w : words) {
            if (w.length() > 0) {
                result.append(Character.toUpperCase(w.charAt(0)))
                        .append(w.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    public static int countVowels(String str) {
        int count = 0;
        str = str.toLowerCase();

        for (char c : str.toCharArray()) {
            if ("aeiou".indexOf(c) != -1) {
                count++;
            }
        }
        return count;
    }
}