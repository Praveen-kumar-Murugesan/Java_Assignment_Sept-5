package com.zeetaminds.assgn5sept.net.echo;

import java.io.*;
import java.net.*;

public class MultiThreadedEchoServer {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                        System.out.println(clientSocket.getInetAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String message;
            while ((message = input.readLine()) != null) {
                if (message.equals("end")) {
                    System.out.println("Termination Signal Received.");
                    break;
                }
                System.out.println("Received from client: " + message);
                output.println("Echo: " + message);
            }
        } finally {
            clientSocket.close();
            System.out.print("Client disconnected: ");
        }

    }
}
