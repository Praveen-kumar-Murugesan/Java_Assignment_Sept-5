package com.zeetaminds.assgn5sept.net.ftp.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private final CommandParser commandParser;
    private final ResponseSender responseSender;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.commandParser = CommandParser.getInstance();
        this.responseSender = new ResponseSender();
    }

    @Override
    public void run() {
        try (BufferedInputStream bin = new BufferedInputStream(clientSocket.getInputStream()); OutputStream out = clientSocket.getOutputStream()) {

            responseSender.sendResponse(out, "220 FTP Server ready");
            bin.mark(1024);
            //noinspection InfiniteLoopStatement
            while (true) {
                commandParser.readCommand(bin, out, clientSocket);
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