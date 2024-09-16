package com.zeetaminds.assgn5sept.net.echo;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ThreadPoolEchoServer {
    private static final int PORT = 1234;
    private static final int MAX_THREADS = 10;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Echo server is listening on port " + PORT);

        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            // Submit the client handling task to the thread pool
            threadPool.submit(() -> {
                try {
                    handleClient(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void handleClient(Socket clientSocket) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);

        String message;
        while ((message = input.readLine()) != null) {
            System.out.println("Received from client: " + message);
            output.println("Echo: " + message);
        }

        clientSocket.close();
        System.out.println("Client disconnected.");
    }
}
