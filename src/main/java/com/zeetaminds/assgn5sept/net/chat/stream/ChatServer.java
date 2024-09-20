package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatServer {
    private static final Logger LOG = LogManager.getLogger(ChatServer.class);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server listening on port 5000...");
            Socket clientSocket = serverSocket.accept();
            clientSocket.setTcpNoDelay(true);
            System.out.println("Client connected.");

            Thread sendThread = new Thread(new MessageSender(clientSocket));
            Thread receiveThread = new Thread(new MessageReceiver(clientSocket));

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();
        } catch (IOException | InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }
}
