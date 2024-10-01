package com.zeetaminds.assgn5sept.net.ftp.nio;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandParser {
    private static final int DEFAULT_SIZE = 1024;
    private static final CommandParser CMD = new CommandParser();

    private CommandParser() {
    }

    public static CommandParser getInstance() {
        return CMD;
    }

    public Command parseCommand(BufferedInputStream bin, Socket clientSocket) throws IOException, InvalidCommandException {
        byte[] buffer = new byte[DEFAULT_SIZE];
        int bytesRead;

        //noinspection WhileCanBeDoWhile
        while (true) {
            bytesRead = bin.read(buffer);

            if (bytesRead == -1) throw new IOException("Connection closed");

            int len = getIndexOfCR(buffer, bytesRead);
            if (len == -1) continue;

            String command = new String(buffer, 0, len, StandardCharsets.UTF_8);

            bin.reset();
            bin.skip(len + 1);
            bin.mark(DEFAULT_SIZE);

            return (_parseCommand(command, clientSocket));
        }
    }

    private Command _parseCommand(String command, Socket clientSocket) throws InvalidCommandException {
        String[] tokens = command.split(" ");
        String cmd = tokens[0].toUpperCase();

        Command cmdHandler;

        switch (cmd) {
            case "LIST":
                cmdHandler = new ListCommand();
                break;
            case "GET":
                validateCommand(tokens);
                cmdHandler = new GetCommand(tokens[1]);
                break;
            case "PUT":
                validateCommand(tokens);
                cmdHandler = new PutCommand(tokens[1]);
                break;
            case "PWD":
                cmdHandler = new PwdCommand();
                break;
            case "QUIT":
                cmdHandler = new QuitCommand(clientSocket);
                break;
            default:
                throw new InvalidCommandException(command);
        }
        return cmdHandler;
    }

    private int getIndexOfCR(byte[] buff, int len) {
        for (int i = 0; i < len; i++) {
            if (buff[i] == '\n') return i;
        }
        return -1;
    }

    private void validateCommand(String[] tokens) throws InvalidCommandException {
        if (tokens.length < 2) {
            throw new InvalidCommandException("Syntax Error");
        }
        if (tokens[1].isEmpty() || tokens[1].equals(" ")) {
            throw new InvalidCommandException("Invalid Filename");
        }
    }
}