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

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        commandParser = new CommandParser();
        responseSender = new ResponseSender();
    }

    @Override
    public void run() {
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
            responseSender.sendResponse(in, out, "220 FTP Server ready");

            while (true) {
                String command = commandParser.readCommand(in);
                if (command == null) {
                    System.out.println("Client Disconnected");
                    break;
                }
                Command cmdHandler = commandParser.parseCommand(command, clientSocket);
                if (cmdHandler != null) {
                    cmdHandler.execute(in, out, command);
                } else {
                    responseSender.sendResponse(in, out, "502 Command not implemented.");
                }
            }
        } catch (IOException e) {
            LOG.error("Error: {}", e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}