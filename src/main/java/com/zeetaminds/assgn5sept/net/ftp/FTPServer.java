package com.zeetaminds.assgn5sept.net.ftp;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class FTPServer {
    private static final int PORT = 2121;
    private static final String USERS_FILE = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/client_files/test1.txt";
    private static final int BUFFER_SIZE = 10000;

    private static final Map<String, String> userDatabase = new HashMap<>();

    public static void main(String[] args) {
        loadUserDatabase();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUserDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                if (parts.length == 2) {
                    userDatabase.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;
        private String fileName;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    String response = handleCommand(inputLine);
                    out.println(response);

                    if (response.startsWith("221")) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String handleCommand(String command) {
            if (command.startsWith("USER")) {
                return handleUserCommand(command);
            } else if (command.startsWith("PASS")) {
                return handlePassCommand(command);
            } else if (command.startsWith("LIST")) {
                return handleListCommand();
            } else if (command.startsWith("PWD")) {
                return handlePwdCommand();
            } else if (command.startsWith("CWD")) {
                return handleCwdCommand(command);
            } else if (command.startsWith("MKD")) {
                return handleMkdCommand(command);
            } else if (command.startsWith("RMD")) {
                return handleRmdCommand(command);
            } else if (command.startsWith("STOR")) {
                return handleStorCommand(command);
            } else if (command.startsWith("RETR")) {
                return handleRetrCommand(command);
            } else if (command.startsWith("QUIT")) {
                return "221 Goodbye.";
            } else {
                return "502 Command not implemented.";
            }
        }

        private String handleUserCommand(String command) {
            // No authentication required, so we can just return a success message
            return "331 User name okay, need password.";
        }

        private String handlePassCommand(String command) {
            // No authentication required, so we can just return a success message
            return "230 User logged in, proceed.";
        }

        private String handleListCommand() {
            File currentDir = new File("server_files");
            StringBuilder response = new StringBuilder("150 File status okay.\n");
            for (String file : currentDir.list()) {
                response.append(file).append("\n");
            }
            return response.toString();
        }

        private String handlePwdCommand() {
            return "257 \"" + Paths.get("").toAbsolutePath().toString() + "\" is current directory.";
        }

        private String handleCwdCommand(String command) {
            String[] parts = command.split(" ", 2);
            if (parts.length < 2) return "503 Bad sequence of commands.";
            File dir = new File(parts[1]);
            if (dir.exists() && dir.isDirectory()) {
                System.setProperty("user.dir", dir.getAbsolutePath());
                return "250 Directory changed.";
            } else {
                return "550 No such directory.";
            }
        }

        private String handleMkdCommand(String command) {
            String[] parts = command.split(" ", 2);
            if (parts.length < 2) return "503 Bad sequence of commands.";
            File dir = new File(parts[1]);
            if (dir.mkdirs()) {
                return "257 Directory created.";
            } else {
                return "550 Directory creation failed.";
            }
        }

        private String handleRmdCommand(String command) {
            String[] parts = command.split(" ", 2);
            if (parts.length < 2) return "503 Bad sequence of commands.";
            File dir = new File(parts[1]);
            try {
                Files.walk(dir.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                return "250 Directory removed.";
            } catch (IOException e) {
                e.printStackTrace();
                return "550 Directory removal failed.";
            }
        }

        private String handleStorCommand(String command) {
            String[] parts = command.split(" ", 2);
            if (parts.length < 2) return "503 Bad sequence of commands.";
            fileName = parts[1];
            return "150 File status okay.";
        }

        private String handleRetrCommand(String command) {
            String[] parts = command.split(" ", 2);
            if (parts.length < 2) return "503 Bad sequence of commands.";
            fileName = parts[1];
            try (BufferedReader fileReader = new BufferedReader(new FileReader(fileName))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = fileReader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                return "226 Transfer complete.\n" + content.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "550 File not found.";
            }
        }
    }
}