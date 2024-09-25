package com.zeetaminds.assgn5sept.net.ftp.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);
    private final Socket clientSocket;
    private InputStream in;
    private OutputStream out;
    private final CommandParser commandParser;
    private final ResponseSender responseSender;
    private byte[] remainingData;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.commandParser = new CommandParser();
        this.responseSender = new ResponseSender();
        this.remainingData = null;
    }

    @Override
    public void run() {
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
            responseSender.sendResponse(out, "220 FTP Server ready");

            while (true) {
                String command = commandParser.readCommand(in, remainingData);
                remainingData = null;
                if (command == null) {
                    System.out.println("Client Disconnected");
                    break;
                }
                Command cmdHandler = commandParser.parseCommand(command, clientSocket);
                if (cmdHandler != null) {
                    cmdHandler.execute(in, out, command);
                    if (cmdHandler instanceof PutCommand) {
                        remainingData = ((PutCommand) cmdHandler).getRemainingData();
                    }
                } else {
                    responseSender.sendResponse(out, "502 Command not implemented.\n");
                }
            }
        } catch (IOException e) {
            LOG.error("Client {}", e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                LOG.info("Resources closed for client: {}", clientSocket.getInetAddress());
            }
        } catch (IOException e) {
            LOG.error("Error closing resources {}", e.getMessage());
        }
    }
}