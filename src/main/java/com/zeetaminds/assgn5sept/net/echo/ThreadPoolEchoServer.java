package com.zeetaminds.assgn5sept.net.echo;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ThreadPoolEchoServer {
    private static final int PORT = 1234;
    private static final int MAX_THREADS = 10;

    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Echo server is listening on port " + PORT);

            ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

            //noinspection InfiniteLoopStatement
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                threadPool.submit(() -> {
                    try {
                        ClientHandler handler = new ClientHandler(clientSocket);
                        handler.handleClient();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
