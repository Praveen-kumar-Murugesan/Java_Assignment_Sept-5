package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageReceiver extends MessageHandler {
    public MessageReceiver(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(socket.getInputStream())) {
            socket.setReceiveBufferSize(8192);
//            byte[] buffer = new byte[1024];
//            int bytesRead;
            while (true) {
                String msg = in.readUTF();
                LocalDateTime afterReceive = LocalDateTime.now();
//                String message = new String(buffer, 0, bytesRead).trim();
                LOG.info("\n After receive [{}]", afterReceive.format(FORMATTER));
                if (msg.startsWith("null") || "exit".equalsIgnoreCase(msg)) {
                    System.out.println("Connection closed by other end");
                    break;
                }
                System.out.println("Received: " + msg);
            }
        } catch (IOException e) {
            LOG.error("Error in MessageReceiver: {}", e.getMessage());
        } finally {
            closeSocket();
        }
    }

    public static void main(String[] agrs) throws InterruptedException {
        LocalDateTime afterReceive = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("SSSSSSSSS");
        System.out.println(LocalDateTime.now().format(fmt));
        Thread.sleep(1000);
        System.out.println(LocalDateTime.now().format(fmt));
        System.out.println(LocalDateTime.now().format(fmt));
    }
}
