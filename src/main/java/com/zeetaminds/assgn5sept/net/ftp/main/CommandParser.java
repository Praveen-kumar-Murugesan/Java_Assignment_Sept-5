package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandParser {

    private int index = 0;
    private byte[] data = null;
    private final ResponseSender responseSender = new ResponseSender();

    public void readCommand(InputStream in, OutputStream out, byte[] remainingData, Socket clientSocket) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        int bytesRead;

        if (remainingData != null) {
            data = remainingData;
        }

        boolean commandComplete = false;
        while (!commandComplete) {
            if (data != null && index < data.length) {
                int length = Math.min(buffer.length, data.length - index);
                System.arraycopy(data, index, buffer, 0, length);
                bytesRead = length;
                index += length;
            } else {
                bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    return;
                }
            }

            for (int i = 0; i < bytesRead; i++) {
                bos.write(buffer[i]);
                if (buffer[i] == '\n') {
                    commandComplete = true;
                    break;
                }
            }

            bos.write(buffer[0]);
            if (data != null && index >= data.length) {
                index = 0;
                data = null;
            }
        }

        String command = bos.toString(StandardCharsets.UTF_8).trim();
        parseCommand(command, in, out, clientSocket);
    }

    public void parseCommand(String command, InputStream in, OutputStream out, Socket clientSocket) throws IOException {
        String[] tokens = command.split(" ", 2);
        String cmd = tokens[0].toUpperCase();

        Command cmdHandler;

        switch (cmd) {
            case "LIST":
                cmdHandler = new ListCommand();
                break;
            case "GET":
                cmdHandler = new GetCommand();
                break;
            case "PUT":
                cmdHandler = new PutCommand();
                break;
            case "PWD":
                cmdHandler = new PwdCommand();
                break;
            case "QUIT":
                cmdHandler = new QuitCommand(clientSocket);
                break;
            default:
                responseSender.sendResponse(out, "502 Command not implemented.\n");
                return;
        }

        // Execute the parsed command
        cmdHandler.execute(in, out, command);
    }
}