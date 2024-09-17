package com.zeetaminds.assgn5sept.net.echo.writeblock;

import java.io.*;
import java.net.Socket;

import java.io.*;
import java.net.Socket;

public class SimpleClient {
    private static final String SERVER_ADDRESS = "127.0.0.1"; // Localhost
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the server.");

            // Input stream to read data from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Output stream to send data to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Send a few messages to the server
            for (int i = 1; i <= 5; i++) {
                String message = "Message " + i;
                System.out.println("Sending to server: " + message);
                output.println(message);

                // Delay to simulate reading after some time
                Thread.sleep(2000);

                // Read server's response
                String serverResponse = input.readLine();
                System.out.println("Received from server: " + serverResponse);
            }

            // Signal the end of communication
            output.println("end");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
