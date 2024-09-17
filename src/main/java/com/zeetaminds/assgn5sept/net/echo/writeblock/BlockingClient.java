package com.zeetaminds.assgn5sept.net.echo.writeblock;

import java.io.*;
import java.net.*;

public class BlockingClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Send some initial data
            output.println("Hello Server");

            // Read data from the server
            String response;
            while ((response = input.readLine()) != null) {
                Thread.sleep(50000);
                System.out.println("Received from server: " + response);

                // Simulate processing of data
                // To test the server blocking, comment out the next line


                // Optionally send some data back to the server
                output.println("Received more data");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Client interrupted.");
        }
    }
}
