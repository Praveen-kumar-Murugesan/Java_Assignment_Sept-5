package com.zeetaminds.assgn5sept.net.ftp.main;

import com.zeetaminds.assgn5sept.fileio.serialize.BinarySerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);
    private Socket clientSocket;
    private InputStream in;
    private OutputStream out;
    private CommandParser commandParser;
    private ResponseSender responseSender;
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
            responseSender.sendResponse(in, out, "220 FTP Server ready");

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
                    responseSender.sendResponse(in, out, "502 Command not implemented.\n");
                }
            }
        } catch (IOException e) {
            LOG.error("Client {}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}