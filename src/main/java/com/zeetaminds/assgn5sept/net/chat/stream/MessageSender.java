package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.*;
import java.net.Socket;

public class MessageSender implements Runnable {
    private Socket socket;

    public MessageSender(Socket socket) {
        this.socket = socket;
    }

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
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
