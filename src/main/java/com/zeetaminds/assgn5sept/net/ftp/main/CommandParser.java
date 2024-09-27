package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandParser {

    private static final CommandParser CMD = new CommandParser();

    private CommandParser() { }

    public static CommandParser getInstance() {
        return CMD;
    }

    private final ResponseSender responseSender = new ResponseSender();

    private int getIndexOfCR(byte[] buff, int len) {
        for(int i = 0; i < len; i++) {
            if(buff[i] == '\n') return i;
        }
        return -1;
    }

    public void readCommand(BufferedInputStream bin, OutputStream out, Socket clientSocket) throws IOException {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

//        boolean hasMore = true;
        while (true) {
            bytesRead = bin.read(buffer);

            if (bytesRead == -1) throw new IOException("Connection closed");

            int len = getIndexOfCR(buffer, bytesRead);
            if(len == -1) continue;

            String command = new String(buffer, 0, len, StandardCharsets.UTF_8);
            parseCommand(command, bin, out, clientSocket);

            bin.reset();
            bin.skip(len + 1);
            bin.mark(1024);
        }
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