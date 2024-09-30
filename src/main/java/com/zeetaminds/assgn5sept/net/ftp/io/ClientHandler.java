package com.zeetaminds.assgn5sept.net.ftp.io;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
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
                try {
                    Command cmd = commandParser.readCommand(bin, clientSocket);
                    if (cmd != null) {
                        cmd.execute(bin, out);
                    }
                } catch (InvalidCommandException | SocketException e) {
                    out.write(("500 " + e.getMessage() + "\r\n").getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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