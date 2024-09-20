package com.zeetaminds.assgn5sept.net.chat.writer;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatClient {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            socket.setTcpNoDelay(true);
            System.out.println("Connected to server.");

            // Create threads for sending and receiving data
            Thread sendThread = new Thread(() -> {
                try (OutputStream out = socket.getOutputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 1024)) {
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
                try (InputStream in = socket.getInputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    StringBuilder messageBuilder = new StringBuilder();
                    while ((bytesRead = in.read(buffer)) != -1) {
                        String message = new String(buffer, 0, bytesRead);
                        messageBuilder.append(message);
                        if (message.endsWith("\n")) {
                            String completeMessage = messageBuilder.toString().trim();
                            if ("exit".equalsIgnoreCase(completeMessage)) {
                                System.out.println("Server requested termination. Client shutting down...");
                                System.exit(0);
                            }
                            System.out.println(completeMessage);
//                            System.out.println("Server [" + getCurrentTimestamp() + "]: " + completeMessage);
                            messageBuilder.setLength(0);
                        }
                    }
                } catch (IOException e) {
                    if (!socket.isClosed()) {
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
        return LocalDateTime.now().format(FORMATTER);
    }
}
