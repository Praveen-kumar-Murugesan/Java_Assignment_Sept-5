package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.*;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class MessageReceiver implements Runnable {
    private Socket socket;

    public MessageReceiver(Socket socket) {
        this.socket = socket;
    }
    private static final Logger LOG = LogManager.getLogger(MessageReceiver.class);

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            String message;

            while ((bytesRead = in.read(buffer)) != -1) {
                if (bytesRead <= 0) {
                    System.out.println("Socket closed or no bytes read, stopping receiver.");
                    break;
                }
                message = new String(buffer, 0, bytesRead);
                if (message.endsWith("\n")) {
                    String completeMessage = message.trim();
                    if ("exit".equalsIgnoreCase(completeMessage)) {
                        System.out.println("Terminating connection...");
                        System.exit(0);
                    }
                    if (!"null".startsWith(completeMessage)) {
                        System.out.println(completeMessage);
                    }
                }
            }
        }catch (IOException e) {
                LOG.error(e.getMessage());
        }
    }
}
