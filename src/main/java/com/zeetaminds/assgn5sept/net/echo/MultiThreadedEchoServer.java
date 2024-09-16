package com.zeetaminds.assgn5sept.net.echo;

import java.io.*;
import java.net.*;

public class MultiThreadedEchoServer {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo server is listening on port " + PORT);

            //noinspection InfiniteLoopStatement
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new Thread(() -> {
                    try {
                        ClientHandler handler = new ClientHandler(clientSocket);
                        handler.handleClient();
                        System.out.println(clientSocket.getInetAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
