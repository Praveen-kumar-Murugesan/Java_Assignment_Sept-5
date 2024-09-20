package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.*;
import java.net.Socket;

public class MessageReceiver extends MessageHandler {

    public MessageReceiver(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                String message = new String(buffer, 0, bytesRead).trim();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Connection closed by client.");
                    break;
                }
                System.out.println("Received: " + message);
            }
        } catch (IOException e) {
            LOG.error("Error in MessageReceiver: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }
}
