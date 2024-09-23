package com.zeetaminds.assgn5sept.net.ftp.trial1;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.*;

public class FTPServer {
    private static final int PORT = 8888; // Default FTP port

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("FTP Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            out.println("220 FTP Server ready");

            while (true) {
                try {
                    String command = in.readLine();
                    if(command == null){
                        System.out.println("Client Disconnected");
                        break;
                    }
                    System.out.println("Received: " + command);
                    handleCommand(command);
                }catch (IOException e) {
                    System.out.println("Error in communication: "+ e.getMessage());
                    break;
                }

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleCommand(String command) throws IOException {
        String[] tokens = command.split(" ", 2);
        String cmd = tokens[0].toUpperCase();

        switch (cmd) {
            case "LIST":
                sendDirectoryListing();
                break;
            case "GET":
                if (tokens.length > 1) {
                    sendFile(tokens[1]);
                } else {
                    out.println("501 Syntax error in parameters or arguments.");
                }
                break;
            case "PUT":
                if (tokens.length > 1) {
                    receiveFile(tokens[1]);
                } else {
                    out.println("501 Syntax error in parameters or arguments.");
                }
                break;
            case "PWD":
                sendCurrentDirectory();
                break;
            case "QUIT":
                out.println("221 Goodbye.");
                clientSocket.close();
                break;
            default:
                out.println("502 Command not implemented.");
                break;
        }
    }

    private void sendCurrentDirectory() {
        String currentDir = new File(".").getAbsolutePath();
        out.println("257 \"" + currentDir + "\" is the current directory.");
    }

    private void sendDirectoryListing() throws IOException {
        File dir = new File(".");
        File[] files = dir.listFiles();
        out.println("150 Opening ASCII mode data connection for file list.");
        if (files != null) {
            for (File file : files) {
                out.println(file.getName());
            }
        }
        out.println("226 Transfer complete.");
    }

    private void sendFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && !file.isDirectory()) {
//            out.println("150 Opening binary mode data connection for " + fileName);
//            byte[] buffer = new byte[1024];
//            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//                 OutputStream os = clientSocket.getOutputStream()) {
//                int bytesRead;
//                long bytesSent = 0;
//                long size = file.length();
//                while (size > bytesSent && (bytesRead = bis.read(buffer)) != -1) {
//                    os.write(buffer, 0, bytesRead);
//                    bytesSent += bytesRead;
//                }
//
//                // Ensure to flush the output stream after file transfer
//                os.flush();
//
//                // Send completion message if the socket is still open
//                if (!clientSocket.isClosed()) {
//                    out.println("226 Transfer complete.");
//                    out.flush(); // Ensure the message is sent
//                    return;
//                }
//            } catch (IOException e) {
//                System.out.println("Error during file transfer: " + e.getMessage());
//                if (!clientSocket.isClosed()) {
//                    out.println("550 File transfer failed: " + e.getMessage());
//                }
//            }
            long fileSize=0;
            try {
                fileSize = Files.size(file.toPath());
                out.println("File size: " + fileSize + " bytes");
            } catch (IOException e) {
                e.printStackTrace();
//                return "550 Error getting file size.";
            }

            long sentBytes = 0;
            try (BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileReader.read(buffer)) != -1 && sentBytes < fileSize) {
                    clientSocket.getOutputStream().write(buffer, 0, bytesRead);  // Send data to the client
                    sentBytes += bytesRead;
                }
                out.println("Total bytes sent: " + sentBytes);
                out.println("226 Transfer complete.\n");  // Inform client the transfer is complete
            } catch (IOException e) {
                e.printStackTrace();
//                return "550 Error reading file.";
            }
        } else {
            out.println("550 File not found.");
        }
    }


    private void receiveFile(String fileName) throws IOException {
        out.println("150 Opening binary mode data connection for " + fileName);
        byte[] buffer = new byte[1024];
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName));
             InputStream is = clientSocket.getInputStream()) {
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        }
        out.println("226 Transfer complete.");
    }
}