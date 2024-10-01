package com.zeetaminds.assgn5sept.net.ftp.nio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer {
    private static final Logger LOG = LogManager.getLogger(FTPServer.class);
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("FTP Server started on port " + PORT);
            //noinspection InfiniteLoopStatement
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected...");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            LOG.info("Error in Socket: {}", e.getMessage());
        }
    }
}