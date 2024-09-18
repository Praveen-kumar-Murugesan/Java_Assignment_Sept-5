package com.zeetaminds.assgn5sept.net.ftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNC {

    private static final String FILE_PATH = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/server_files/";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server is Starting on Port 8888");

            // Accept client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connected");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String option = "";
                while (!"END".equalsIgnoreCase(option)) {
                    try {
                        // Read command from client
                        option = reader.readLine();
                        if (option == null) {
                            System.out.println("Client disconnected.");
                            break; // Exit the loop if the client disconnects
                        }

                        System.out.println("Command received: " + option);
                        String[] parts = option.split(" ", 2);
                        String command = parts[0].toUpperCase();

                        switch (command) {
                            case "PUT":
                                if (parts.length > 1) {
                                    receiveFile(FILE_PATH + parts[1], reader);
                                } else {
                                    writer.println("Filename missing for PUT command.");
                                }
                                break;
                            case "GET":
                                if (parts.length > 1) {
                                    sendFile(FILE_PATH + parts[1], clientSocket.getOutputStream());
                                } else {
                                    writer.println("Filename missing for GET command.");
                                }
                                break;
                            case "LIST":
                                listFiles(writer);
                                break;
                            case "END":
                                System.out.println("Terminating connection...");
                                break;
                            default:
                                writer.println("Invalid command: " + option);
                                break;
                        }
                    } catch (IOException e) {
                        System.out.println("Error reading from client: " + e.getMessage());
                        break; // Exit if there's an issue reading the client request
                    }
                }
            } catch (IOException e) {
                System.out.println("Error handling client connection: " + e.getMessage());
            } finally {
                clientSocket.close();
                System.out.println("Connection closed.");
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private static void receiveFile(String filePath, BufferedReader reader) throws IOException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            String line;
            while (!(line = reader.readLine()).equals("END_OF_FILE")) {
                fileWriter.write(line);
                fileWriter.newLine();
            }
            System.out.println("File received: " + filePath);
        }
    }

    private static void sendFile(String filePath, OutputStream outputStream) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println("File not found.");
            return;
        }

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file));
             PrintWriter writer = new PrintWriter(outputStream, true)) {

            String line;
            while ((line = fileReader.readLine()) != null) {
                writer.println(line);
            }
            writer.println("END_OF_FILE");
            writer.flush();
        }
        System.out.println("File sent: " + filePath);
    }

    private static void listFiles(PrintWriter writer) {
        File folder = new File(FILE_PATH);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null && listOfFiles.length > 0) {
            writer.println("Files on server:");
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    writer.println(file.getName());
                }
            }
        } else {
            writer.println("No files found on server.");
        }
        writer.println("END_OF_LIST");
    }
}
