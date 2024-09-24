package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandParser {
    public String readCommand(InputStream in) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1];
        //noinspection ConditionalBreakInInfiniteLoop
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead == -1) {
                return null; // End of stream
            }
            bos.write(buffer[0]);

            // Check for newline character
            if (buffer[0] == '\n') {
                break; // End of command
            }
        }
        return bos.toString(StandardCharsets.UTF_8).trim(); // Use UTF-8 encoding
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
                return null; // Or throw an exception for unrecognized commands
        }
    }
}
