package com.zeetaminds.assgn5sept.net.ftp.nio;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;
import java.nio.ByteBuffer;

public class CommandParser {

    private static final CommandParser CMD = new CommandParser();
//    private final StringBuilder commandBuilder = new StringBuilder();

    private CommandParser() {
    }

    public static CommandParser getInstance() {
        return CMD;
    }

    public Command parseCommand(StateManager stateManager) throws InvalidCommandException {

        ByteBuffer byteBuffer = stateManager.getBuffer();
        StringBuilder commandBuilder = stateManager.getCommandBuilder();
        int previousPosition = byteBuffer.position();
        String command;

        int len;
        if ((len = getIndexOfCR(byteBuffer, commandBuilder)) != -1) {
            command = commandBuilder.toString().trim();

            if (command.isEmpty()) {
                stateManager.resetCommandBuilder();
            } else {
                int newPosition = previousPosition + len + 1;
                byteBuffer.position(newPosition);

                stateManager.resetCommandBuilder();
                return _parseCommand(command);
            }
        }

        byteBuffer.compact();
        return null;
    }

    private Command _parseCommand(String command) throws InvalidCommandException {
        String[] tokens = command.split(" ");
        String cmd = tokens[0].toUpperCase();

        switch (cmd) {
            case "LIST":
                return new ListCommand();
            case "GET":
                validateCommand(tokens);
                return new GetCommand(tokens[1]);
            case "PUT":
                validateCommand(tokens);
                return new PutCommand(tokens[1]);
            case "PWD":
                return new PwdCommand();
            case "QUIT":
                return new QuitCommand();
            default:
                throw new InvalidCommandException(command);
        }
    }

    /**
     * Scans the ByteBuffer for a newline character ('\n') and appends characters to commandBuilder.
     *
     * @param byteBuffer the buffer containing the command data
     * @return the position of the newline character, or -1 if not found
     */
    private int getIndexOfCR(ByteBuffer byteBuffer, StringBuilder commandBuilder) {
        int len = byteBuffer.remaining();

        for (int i = 0; i < len; i++) {
            byte currentByte = byteBuffer.get();

            if (currentByte == '\n') {
                return i;
            } else if (currentByte != '\r') {
                commandBuilder.append((char) currentByte);
            }
        }
        return -1;
    }

    private void validateCommand(String[] tokens) throws InvalidCommandException {
        if (tokens.length < 2 || tokens[1].isEmpty() || tokens[1].equals(" ")) {
            throw new InvalidCommandException("Invalid Filename");
        }
    }
}
