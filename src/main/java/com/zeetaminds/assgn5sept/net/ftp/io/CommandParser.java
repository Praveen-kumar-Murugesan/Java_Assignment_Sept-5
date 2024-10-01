package com.zeetaminds.assgn5sept.net.ftp.io;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class CommandParser {
    private static final int DEFAULT_SIZE = 10;
    private static final CommandParser CMD = new CommandParser();
    private static int commandLength = 0;
    byte[] buffer = new byte[DEFAULT_SIZE];

    private CommandParser() {
    }

    public static CommandParser getInstance() {
        return CMD;
    }

    public Command parseCommand(BufferedInputStream bin, Socket clientSocket) throws IOException, InvalidCommandException {
        StringBuilder commandBuilder = new StringBuilder();
        commandLength = 0;
        int bytesRead;
        String command = "";

        while (true) {
            bytesRead = bin.read(buffer);
            if (bytesRead == -1) {
                throw new IOException("Connection closed");
            }

            commandBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));

            int len = getIndexOfCR(buffer, bytesRead);
            if (len != -1) {
                command = command + commandBuilder.substring(0, commandLength).trim();

                bin.reset();
                //noinspection ResultOfMethodCallIgnored
                bin.skip(len + 1);
                bin.mark(DEFAULT_SIZE);

                return _parseCommand(command, clientSocket);
            } else {
                bin.mark(DEFAULT_SIZE);
            }
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
            char c = (char) buff[i];
            commandLength++;
            System.out.println(c);
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