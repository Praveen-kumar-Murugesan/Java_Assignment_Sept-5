package com.zeetaminds.assgn5sept.net.ftp.nio;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;

import java.nio.channels.SocketChannel;

public class CommandParser {

    private static final CommandParser CMD = new CommandParser();

    private CommandParser() {
    }

    public static CommandParser getInstance() {
        return CMD;
    }

    public Command readCommand(String commandLine, SocketChannel clientChannel) throws InvalidCommandException {
        String[] tokens = commandLine.trim().split(" ");
        String cmd = tokens[0].toUpperCase();

        switch (cmd) {
//            case "LIST":
//                return new ListCommand();
            case "GET":
                if (tokens.length < 2) throw new InvalidCommandException("Missing filename for GET.");
                return new GetCommand(tokens[1]);
//            case "PUT":
//                if (tokens.length < 2) throw new InvalidCommandException("Missing filename for PUT.");
//                return new PutCommand(tokens[1]);
//            case "PWD":
//                return new PwdCommand();
            case "QUIT":
                return new QuitCommand(clientChannel);
            default:
                throw new InvalidCommandException("Unknown command: " + cmd);
        }
    }
}
