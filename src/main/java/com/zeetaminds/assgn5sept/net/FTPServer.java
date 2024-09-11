package com.zeetaminds.assgn5sept.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FTPServer {
    private static final int PORT = 12345;  // Port for the FTP server
    private static final String DIRECTORY = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/server_files";  // Directory to store files

    public static void main(String[] args) {
        File serverDirectory = new File(DIRECTORY);
        if (!serverDirectory.exists()) {
            serverDirectory.mkdir();
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("FTP Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, serverDirectory).start();
                System.out.println("Client Connection established");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private File directory;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, File directory) {
        this.socket = socket;
        this.directory = directory;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String command;
            while ((command = in.readLine()) != null) {
                String[] parts = command.split(" ", 2);
                String cmd = parts[0].toUpperCase();

                switch (cmd) {
                    case "LIST":
                        listFiles();
                        break;
                    case "GET":
                        if (parts.length > 1) {
                            sendFile(parts[1]);
                        } else {
                            out.println("ERROR: Filename required for GET command");
                        }
                        break;
                    case "PUT":
                        if (parts.length > 1) {
                            receiveFile(parts[1]);
                        } else {
                            out.println("ERROR: Filename required for PUT command");
                        }
                        break;
                    default:
                        out.println("ERROR: Unknown command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void listFiles() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                out.println(file.getName());
            }
        } else {
            out.println("ERROR: Unable to list files");
        }
    }

    private void sendFile(String filename) {
        File file = new File(directory, filename);
        if (file.exists() && !file.isDirectory()) {
            out.println("OK");  // Signal client to start receiving file

            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 OutputStream os = socket.getOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            } catch (IOException e) {
                out.println("ERROR: Unable to send file");
            }
        } else {
            out.println("ERROR: File not found");
        }
    }

    private void receiveFile(String filename) {
        File file = new File(directory, filename);
        out.println("OK");  // Signal client to start sending file

        try (FileOutputStream fos = new FileOutputStream(file);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             InputStream is = socket.getInputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } catch (IOException e) {
            out.println("ERROR: Unable to receive file");
        }
    }
}
