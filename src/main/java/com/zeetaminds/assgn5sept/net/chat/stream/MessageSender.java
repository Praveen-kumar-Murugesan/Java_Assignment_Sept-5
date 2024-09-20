package com.zeetaminds.assgn5sept.net.chat.stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageSender implements Runnable {
    private Socket socket;

    public MessageSender(Socket socket) {
        this.socket = socket;
    }
    private static final Logger LOG = LogManager.getLogger(MessageSender.class);

    @Override
    public void run() {
        try (OutputStream out = socket.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 1024)) {
            String message;
            while (true) {
                message = reader.readLine();
                byte[] messageBytes = (message + "\n").getBytes();
                out.write(messageBytes);
                out.flush();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Terminating connection...");
                    break;
                }
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        } finally {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
