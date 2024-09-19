package com.zeetaminds.assgn5sept.net.echo.writeblock;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SlowReaderServer {
    public static void main(String[] args) throws Exception {
        // Server listening on port 8080
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server is listening...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected.");

        InputStream in = clientSocket.getInputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        // Server intentionally delays reading
        while ((bytesRead = in.read(buffer)) != -1) {
            System.out.println("Server read " + bytesRead + " bytes.");
            // Introduce delay to simulate slow processing or network congestion
            Thread.sleep(5);
        }
//        while (true){}

        clientSocket.close();
        serverSocket.close();
    }
}
