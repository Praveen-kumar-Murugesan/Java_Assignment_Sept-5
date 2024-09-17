package com.zeetaminds.assgn5sept.net.ftp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8888)) {

            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            Scanner scanner = new Scanner(System.in);
            System.out.println("Conntected Client");

            String option = "";

            while (!"END".equalsIgnoreCase(option)) {
                option = scanner.nextLine().toUpperCase();
                dataOutputStream.writeUTF(option);
                String filePath = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/client_files/ReceivedFromServer.txt";
                if ("SEND".equalsIgnoreCase(option)) {
                    System.out.println("Enter the file path to send:");
//                filePath = scanner.nextLine();
                    sendFile(filePath);
                } else if ("RECEIVE".equalsIgnoreCase(option)) {
                    receiveFile("ReceivedFromServer.txt");
                } else if ("LIST".equalsIgnoreCase(option)) {
                    listFiles();
                } else if ("END".equalsIgnoreCase(option)) {
                    System.out.println("Terminating connection...");
                } else {
                    System.out.println("Invalid command.");
                }
            }


            dataInputStream.close();
            dataInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendFile(String path) throws Exception {
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        dataOutputStream.writeLong(file.length());
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
            dataOutputStream.flush();
        }
        fileInputStream.close();
    }

    private static void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/client_files/" + fileName);

        long size = dataInputStream.readLong();
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = dataInputStream.read(buffer, 0, Math.min(buffer.length, (int) size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;
        }
        System.out.println("File received");
        fileOutputStream.close();
    }

    private static void listFiles() throws IOException {
        String response;
        while (!(response = dataInputStream.readUTF()).equals("END_OF_LIST")) {
            System.out.println(response);
        }
    }
}