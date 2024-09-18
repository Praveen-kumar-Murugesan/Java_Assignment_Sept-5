package com.zeetaminds.assgn5sept.net.ftp.basic;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {
    static DataOutputStream dataOutputStream = null;
    static DataInputStream dataInputStream = null;
    static final String FILE_PATH = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/client_files/";
    static final Logger LOG = LogManager.getLogger(Client.class);
    static Client list = new Client();
    static SendFile sender = new SendFile();
    static ReceiveFile receiver = new ReceiveFile();

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8888)) {

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);

            String option = "";

            while (!"END".equalsIgnoreCase(option)) {
                option = scanner.nextLine();
                dataOutputStream.writeUTF(option);
                String[] parts = option.split(" ", 2);
                String option1 = parts[0].toUpperCase();
                String filePath = "";
                if (parts.length > 1) {
                    filePath = FILE_PATH + parts[1];
                }
                if ("PUT".equalsIgnoreCase(option1)) {
//                    System.out.println("Enter the file path to send:");
//                    filePath = scanner.nextLine();
                    sender.sendFile(dataOutputStream, filePath);
                } else if ("GET".equalsIgnoreCase(option1)) {
                    receiver.receiveFile(dataInputStream, filePath);
                } else if ("LIST".equalsIgnoreCase(option1)) {
                    list.listFiles();
                } else if ("END".equalsIgnoreCase(option1)) {
                    LOG.info("Client Side Terminating connection...");
                } else {
                    LOG.error("Invalid command.");
                }
            }
            dataInputStream.close();
            dataInputStream.close();
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private void listFiles() throws IOException {
        StringBuilder sb = new StringBuilder();
        String response;
        while (!(response = dataInputStream.readUTF()).equals("END_OF_LIST")) {
            sb.append(response).append("\n");
        }
        LOG.info(sb.toString());
    }
}