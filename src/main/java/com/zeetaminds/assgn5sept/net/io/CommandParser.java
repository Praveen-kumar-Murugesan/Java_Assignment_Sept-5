package com.zeetaminds.assgn5sept.net.io;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CommandParser {
    private static final CommandParser CMD = new CommandParser();

    private CommandParser() {
    }

    public static CommandParser getInstance() {
        return CMD;
    }

    public Command parseCommand(String command, SocketChannel clientChannel) throws IOException, InvalidCommandException {
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
                cmdHandler = new QuitCommand(clientChannel.socket());
                break;
            default:
                throw new InvalidCommandException(command);
        }
        return cmdHandler;
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
