package com.zeetaminds.assgn5sept.net.ftp.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler extends Thread {

    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);

    private final Socket clientSocket;
    private final CommandParser commandParser;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.commandParser = CommandParser.getInstance();
    }

    @Override
    public void run() {
        try (BufferedInputStream bin = new BufferedInputStream(clientSocket.getInputStream());
             OutputStream out = clientSocket.getOutputStream()) {

            out.write("220 FTP Server ready\n".getBytes(StandardCharsets.UTF_8));

            bin.mark(1024);

            //noinspection InfiniteLoopStatement
            while (true) {
                Command cmd = commandParser.readCommand(bin, out, clientSocket);
                cmd.execute(bin, out);
            }
        } catch (IOException e) {
            LOG.error("Client {}", e.getMessage());
        } finally {
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                LOG.error("Error in Socket: {}", e.getMessage());
            }
        }
    }
}