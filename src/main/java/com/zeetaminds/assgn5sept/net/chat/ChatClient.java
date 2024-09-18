package com.zeetaminds.assgn5sept.net.chat;
import java.io.*;
import java.net.*;

public class ChatClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Connected to server.");

            // Create threads for sending and receiving data
            Thread sendThread = new Thread(() -> {
                try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    String message;
                    while (true) {
                        message = reader.readLine();
                        out.println(message);
                        if ("exit".equalsIgnoreCase(message)) {
                            System.out.println("Client terminating connection...");
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread receiveThread = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String message;
                    while ((message = in.readLine()) != null) {
                        if ("exit".equalsIgnoreCase(message)) {
                            System.out.println("Server requested termination. Client shutting down...");
                            break;
                        }
                        System.out.println("Server: " + message);
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {  // Only print stack trace if the socket isn't already closed
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
