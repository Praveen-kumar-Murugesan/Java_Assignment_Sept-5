package com.zeetaminds.assgn5sept.net.chat.writer;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatServer {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server listening on port 5000...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create threads for sending and receiving data
            Thread sendThread = new Thread(() -> {
                try (OutputStream out = clientSocket.getOutputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 1024)) {
                    clientSocket.setTcpNoDelay(true);
                    String message;
                    while (true) {
                        message = reader.readLine();
//                        LocalDateTime beforeWrite = LocalDateTime.now();
//                        System.out.println("Before write [" + beforeWrite.format(formatter) + "]");

                        // Convert message to bytes and send
                        byte[] messageBytes = (message + "\n").getBytes();
                        out.write(messageBytes);
                        out.flush();
//
//                        LocalDateTime afterWrite = LocalDateTime.now();
//                        System.out.println("After write [" + afterWrite.format(formatter) + "]");
//
//                        // Calculate time difference in microseconds
//                        long microseconds = Duration.between(beforeWrite, afterWrite).toNanos() / 1000;
//                        System.out.println("Time difference: " + microseconds + " microseconds");

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
                try (InputStream in = clientSocket.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    StringBuilder messageBuilder = new StringBuilder();
                    while ((bytesRead = in.read(buffer)) != -1) {
                        String message = new String(buffer, 0, bytesRead);
                        messageBuilder.append(message);
                        if (message.endsWith("\n")) {
                            String completeMessage = messageBuilder.toString().trim();
                            if ("exit".equalsIgnoreCase(completeMessage)) {
                                System.out.println("Client requested termination. Server shutting down...");
                                System.exit(0);
                            }
//                            System.out.println("Client [" + getCurrentTimestamp() + "]: " + completeMessage);
                            System.out.println(completeMessage);
                            messageBuilder.setLength(0);
                        }
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

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(formatter);
    }
}
