package com.zeetaminds.assgn5sept.net.echo.writeblock;

import java.io.*;
import java.net.*;

public class FlowControlEchoServer {
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
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String message;
            while ((message = input.readLine()) != null) {
                if (message.equals("end")) {
                    System.out.println("Termination Signal Received.");
                    break;
                }

                System.out.println("Received from client: " + message);
                System.out.println("Simulating flow control by sending large data...");

                // Simulate flow control by sending a large amount of data
                String largeMessage = "Datarrfrrrrrrr ".repeat(4000); // Large data to fill the buffer
                output.println(largeMessage);

                // Wait here indefinitely to simulate blocking
                System.out.println("Waiting indefinitely for client to read data...");
                try {
                    Thread.sleep(Long.MAX_VALUE); // Simulate indefinite block
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted while waiting.");
                }
            }
        } finally {
            clientSocket.close();
            System.out.println("Client disconnected.");
        }
    }
}
