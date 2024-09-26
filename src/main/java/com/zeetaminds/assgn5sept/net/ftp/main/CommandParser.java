package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandParser {
    private static final CommandParser CMD = new CommandParser();
    private CommandParser(){
    }

    public static CommandParser getInstance(){
        return CMD;
    }

//    private byte[] data = null;
    private final ResponseSender responseSender = new ResponseSender();

    public void readCommand(BufferedInputStream bin, OutputStream out, Socket clientSocket) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        int index=0;
        boolean commandComplete = false;
        while (!commandComplete) {
            bytesRead = bin.read(buffer);
            if (bytesRead == -1) {
                return;
            }

            for (int i = 0; i < bytesRead; i++) {
                bos.write(buffer[i]);
                if (buffer[i] == '\n') {
                    index ++;
                    commandComplete = true;
                    break;
                }
            }
            bin.reset();
            bin.skip(index);
            bin.mark(1024);
        }

        String command = bos.toString(StandardCharsets.UTF_8).trim();

        // Parse the command and act accordingly
        parseCommand(command, bin, out, clientSocket);
    }


    private void parseCommand(String command, BufferedInputStream bin, OutputStream out, Socket clientSocket) throws IOException {
        String[] tokens = command.split(" ");
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
        cmdHandler.execute(bin, out, tokens.length > 1 ? tokens[1] : null);
    }
}