package com.zeetaminds.assgn5sept.net.echo;

import java.io.*;
import java.net.*;

public class ClientHandler implements Runnable {
    Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleClient() throws IOException {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String message;
            while ((message = input.readLine()) != null) {
                if (message.equals("end")) {
                    System.out.println("Termination Signal Received.");
                    break;
                }
                System.out.println("Received from client: " + message);
                output.println("Echo: " + message);
            }
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                    System.out.println("Client disconnected.");
                }
            } catch (IOException e) {
                System.err.println("Error while closing client socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
