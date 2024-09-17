package com.zeetaminds.assgn5sept.net.echo.writeblock;
import java.io.*;
import java.net.*;

public class BlockingEchoServer {
    private static final int PORT = 1234;
    private static final int BUFFER_SIZE = 10000000; // Size of the data to send

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo server is listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new Thread(() -> {
                    try {
                        handleClient(clientSocket);
                    } catch (IOException e) {
                        System.err.println("Error handling client: " + e.getMessage());
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
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
                String largeMessage = "Data ".repeat(BUFFER_SIZE); // Large data to fill the buffer
                output.println(largeMessage);

                // Simulate blocking indefinitely if the client does not read the data or disconnects
                System.out.println("Blocking at out.println due to full buffer...");
                try {
                    // This will block because the buffer is full
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
