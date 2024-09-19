package com.zeetaminds.assgn5sept.net.echo.writeblock;

import java.io.OutputStream;
import java.net.Socket;

public class FastWriterClient {
    public static void main(String[] args) throws Exception {
        // Connect to the server at localhost on port 8080
        Socket socket = new Socket("localhost", 8080);
        socket.setSendBufferSize(1024); // Set a small buffer size to simulate block
        OutputStream out = socket.getOutputStream();

        // Write large amounts of data continuously
            byte[] largeData = new byte[1024]; // 5 MB of data to send
        for (int i = 0; i < largeData.length; i++) {
            largeData[i] = 1;  // Fill with arbitrary data
        }

        // Keep writing data until the socket blocks
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                System.out.println("Client writing data...");
                out.write(largeData); // This will eventually block
                out.flush();          // Ensure the data is sent immediately
            }
        } catch (Exception e) {
             e.printStackTrace(); // Catch any exceptions
        } finally {
            socket.close();
        }
    }
}
