package com.zeetaminds.assgn5sept.net.chat;
import java.io.*;
import java.net.*;

public class ChatServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server listening on port 5000...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create threads for sending and receiving data
            Thread sendThread = new Thread(() -> {
                try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    String message;
                    while (true) {
                        message = reader.readLine();
                        out.println(message);
                        if ("exit".equalsIgnoreCase(message)) {
                            System.out.println("Server terminating connection...");
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread receiveThread = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if ("exit".equalsIgnoreCase(message)) {
                            System.out.println("Client requested termination. Server shutting down...");
                            break;
                        }
                        System.out.println("Client: " + message);
                    }
                } catch (IOException e) {
                    if (!clientSocket.isClosed()) {
                        e.printStackTrace();
                    }
                }
            });

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
