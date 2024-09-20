package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.*;
import java.net.Socket;

public class MessageReceiver implements Runnable {
    private Socket socket;

    public MessageReceiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
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
                        System.out.println("Terminating connection...");
                        System.exit(0);
                    }
                    System.out.println(completeMessage);
                    messageBuilder.setLength(0);
                }
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                e.printStackTrace();
            }
        }
    }
}
