package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;

public class MessageSender extends MessageHandler {

    public MessageSender(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try (DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in), 1024)) {
            socket.setSendBufferSize(8192);
            String message;
            while (true) {
                message = reader.readLine();
                LocalDateTime beforeWrite = LocalDateTime.now();
                out.writeUTF((message + "\n"));
                out.flush();
                LocalDateTime afterWrite = LocalDateTime.now();
                long microseconds = Duration.between(beforeWrite, afterWrite).toNanos() / 1000;
                LOG.info("\n Before write [{}] \n After write [{}] \n Time difference: {} microseconds" , beforeWrite.format(FORMATTER) , afterWrite.format(FORMATTER) , microseconds);


                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Terminating connection...");
                    break;
                }
            }
        } catch (IOException e) {
            LOG.error("Error in MessageSender: {}" , e.getMessage());
        } finally {
            closeSocket();
        }
    }
}
