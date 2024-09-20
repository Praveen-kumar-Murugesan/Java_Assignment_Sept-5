package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.*;
import java.net.Socket;

public class MessageSender extends MessageHandler {

    public MessageSender(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try (OutputStream out = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 1024)) {
            String message;
            while (true) {
                message = reader.readLine();
                out.write((message + "\n").getBytes());
                out.flush();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Terminating connection...");
                    break;
                }
            }
        } catch (IOException e) {
            LOG.error("Error in MessageSender: " + e.getMessage());
        } finally {
            closeSocket();
        }
    }
}
