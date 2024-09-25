package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandParser {
    private int index = 0;
    private byte[] data = null;
    public String readCommand(InputStream in, byte[] remainingData) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1];
        if(remainingData != null){
            data = remainingData;
        }
        while (true) {
            if (data != null && index < data.length) {
                buffer[0] = data[index++];
            } else {
                int bytesRead = in.read(buffer);
                if (bytesRead == -1) {
                    return null; // End of stream
                }
            }
            bos.write(buffer[0]);
            if (data != null && index >= data.length) {
                index = 0;
                data = null;
            }
            if (buffer[0] == '\n') {
                break;
            }
        }

        return bos.toString(StandardCharsets.UTF_8).trim();
    }

    public Command parseCommand(String command, Socket clientSocket) {
        String[] tokens = command.split(" ", 2);
        String cmd = tokens[0].toUpperCase();

        switch (cmd) {
            case "LIST":
                return new ListCommand();
            case "GET":
                return new GetCommand();
            case "PUT":
                return new PutCommand();
            case "PWD":
                return new PwdCommand();
            case "QUIT":
                return new QuitCommand(clientSocket);
            default:
                return null;
        }
    }
}
