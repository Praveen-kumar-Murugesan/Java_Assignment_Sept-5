package com.zeetaminds.assgn5sept.net.ftp.nio;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class CommandParser {
    private static final int DEFAULT_SIZE = 1024;
    private static final CommandParser CMD = new CommandParser();
    private int commandLength = 0;
    private final byte[] buffer = new byte[DEFAULT_SIZE];

    private CommandParser() {
    }

    public static CommandParser getInstance() {
        return CMD;
    }

    public Command parseCommand(BufferManager bufferManager, SocketChannel clientChannel)
            throws IOException, InvalidCommandException {

        StringBuilder commandBuilder = new StringBuilder();
        commandLength = 0;
        String command = "";

        ByteBuffer byteBuffer = bufferManager.getBuffer();
        int previousPosition = byteBuffer.position();

        while (byteBuffer.hasRemaining()) {
            int bytesToRead = Math.min(byteBuffer.remaining(), buffer.length);
            byteBuffer.get(buffer, 0, bytesToRead);

            commandBuilder.append(new String(buffer, 0, bytesToRead, StandardCharsets.UTF_8));

            int len;
            while ((len = getIndexOfCR(buffer, bytesToRead)) != -1) {
                command = commandBuilder.substring(0, commandLength).trim();
                int newPosition = previousPosition + len + 1; // +1 to skip the '\n'
                byteBuffer.position(newPosition);  // Set the buffer position to after the '\n'

                previousPosition = byteBuffer.position();  // Store the new position for the next iteration

                commandLength = 0;  // Reset length for the next command
                return _parseCommand(command, clientChannel);  // Return command for execution
            }
        }
        byteBuffer.compact();  // Prepare buffer for next read
        return null;
    }

    private Command _parseCommand(String command, SocketChannel clientChannel)
            throws InvalidCommandException {
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
                cmdHandler = new QuitCommand(clientChannel);
                break;
            default:
                throw new InvalidCommandException(command);
        }
        return cmdHandler;
    }

    private int getIndexOfCR(byte[] buff, int len) {
        for (int i = 0; i < len; i++) {
            if (buff[i] == '\n') return i;
            commandLength++;
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
