package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
//            socket.setTcpNoDelay(true);
            System.out.println("Connected to server.");

            Thread sendThread = new Thread(new MessageSender(socket));
            Thread receiveThread = new Thread(new MessageReceiver(socket));

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
