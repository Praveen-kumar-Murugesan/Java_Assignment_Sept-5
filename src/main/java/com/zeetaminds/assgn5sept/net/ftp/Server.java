package com.zeetaminds.assgn5sept.net.ftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server is Starting in Port 8888");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Connected");

            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

            String option = "";
            while (!"END".equalsIgnoreCase(option)) {
                option = dataInputStream.readUTF();
                System.out.println("Command received: " + option);

                if ("SEND".equalsIgnoreCase(option)) {
                    receiveFile("ReceivedFromClient.txt");
                } else if ("RECEIVE".equalsIgnoreCase(option)) {
                    sendFile("test5.txt");
                } else if ("LIST".equalsIgnoreCase(option)) {
                    listFiles();
                } else if ("END".equalsIgnoreCase(option)) {
                    System.out.println("Terminating connection...");
                } else {
                    System.out.println("Invalid option received.");
                }
            }

            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/server_files/" + fileName);

        long size = dataInputStream.readLong();
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;
        }
        System.out.println("File is Received");
        fileOutputStream.close();
    }

    private static void sendFile(String filePath) throws Exception {
        int bytes = 0;
        File file = new File("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/server_files/" + filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        dataOutputStream.writeLong(file.length());
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        System.out.println("File sent");
        fileInputStream.close();
    }

    private static void listFiles() throws IOException {
        File folder = new File("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/server_files/");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null && listOfFiles.length > 0) {
            dataOutputStream.writeUTF("Files on server:");
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    dataOutputStream.writeUTF(file.getName());
                }
            }
        } else {
            dataOutputStream.writeUTF("No files found on server.");
        }
        dataOutputStream.writeUTF("END_OF_LIST");
        dataOutputStream.flush();
    }
}