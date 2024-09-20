package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class ChatClient {
    private static final Logger LOG = LogManager.getLogger(ChatClient.class);

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            socket.setTcpNoDelay(true);
            System.out.println("Connected to server.");

            Thread sendThread = new Thread(new MessageSender(socket));
            Thread receiveThread = new Thread(new MessageReceiver(socket));

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }
}
