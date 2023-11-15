package mypackage;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalculatorServer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String expression = in.readLine();
            String[] tokens = expression.split(" ");

            if (tokens.length == 3) {
                double num1 = Double.parseDouble(tokens[1]);
                double num2 = Double.parseDouble(tokens[2]);
                double result = 0;

                switch (tokens[0]) {
                    case "ADD":
                        result = num1 + num2;
                        break;
                    case "SUB":
                        result = num1 - num2;
                        break;
                    case "MUL":
                        result = num1 * num2;
                        break;
                    case "DIV":
                        if (num2 != 0) {
                            result = num1 / num2;
                        } else {
                            out.println("ERROR DIVISION_BY_ZERO");
                            return;
                        }
                        break;
                    default:
                        out.println("ERROR INVALID_OPERATION");
                        return;
                }
                out.println(result);
            } else {
                out.println("ERROR INVALID_INPUT");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
