package mypackage;

import java.io.*;
import java.net.*;

public class CalculatorClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 1234;

        try (BufferedReader configReader = new BufferedReader(new FileReader("server_info.dat"))) {
            serverAddress = configReader.readLine();
            port = Integer.parseInt(configReader.readLine());
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Enter expression (e.g., ADD 5 10):");
            String expression = userInput.readLine();
            out.println(expression);

            String response = in.readLine();
            System.out.println("Server response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
