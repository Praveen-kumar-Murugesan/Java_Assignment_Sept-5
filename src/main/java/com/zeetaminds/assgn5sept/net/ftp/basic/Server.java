package com.zeetaminds.assgn5sept.net.ftp.basic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {

    static DataOutputStream dataOutputStream = null;
    static DataInputStream dataInputStream = null;
    static final String FILE_PATH = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/server_files/";
    static final Logger LOG = LogManager.getLogger(Server.class);
    static ListFiles list = new ListFiles(FILE_PATH);
    static SendFile sender = new SendFile();
    static ReceiveFile receiver = new ReceiveFile();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            LOG.info("Server is Starting in Port 8888");

            Socket clientSocket = serverSocket.accept();
            LOG.info("Server Connected to Client{}", clientSocket.getInetAddress());

            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            String option = "";
            while (!"END".equalsIgnoreCase(option)) {
                option = dataInputStream.readUTF();
                LOG.info("Command received: {}", option);
                String[] parts = option.split(" ", 2);
                String option1 = parts[0];
                if ("PUT".equalsIgnoreCase(option1)) {
                    receiver.receiveFile(dataInputStream, FILE_PATH + parts[1]);
                } else if ("GET".equalsIgnoreCase(option1)) {
                    sender.sendFile(dataOutputStream, FILE_PATH + parts[1]);
                } else if ("LIST".equalsIgnoreCase(option1)) {
                    list.listFiles(dataOutputStream);
                } else if ("END".equalsIgnoreCase(option1)) {
                    LOG.info("Server Side Terminating connection...");
                } else {
                    LOG.error("Invalid option received.");
                }
            }

            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }
}