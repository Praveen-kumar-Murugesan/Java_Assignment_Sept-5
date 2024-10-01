package com.zeetaminds.assgn5sept.net.io;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ClientHandler {
    private StringBuilder accumulatedCommand = new StringBuilder();
    private static final int BUFFER_SIZE = 1024;
    private final SocketChannel clientChannel;
    private final CommandParser commandParser;
    private final ByteBuffer buffer;

    public ClientHandler(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
        this.commandParser = CommandParser.getInstance();
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);  // Allocate a ByteBuffer for reading
    }

    public void handleRead(SelectionKey key) {
        try {
            buffer.clear();  // Prepare buffer for reading
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                // Client has closed the connection
                clientChannel.close();
                key.cancel();
                return;
            }

            buffer.flip();  // Prepare buffer for reading data

            while (buffer.hasRemaining()) {
                char currentChar = (char) buffer.get();
                accumulatedCommand.append(currentChar);  // Add each character to the accumulated command

                if (currentChar == '\n') {
                    // End of a command found, process it
                    String command = accumulatedCommand.toString().trim();  // Get command without newline

                    try {
                        // Parse and execute the command
                        Command cmd = commandParser.parseCommand(command, clientChannel);
                        if (cmd != null) {
                            cmd.execute(buffer, clientChannel);
                        }
                    } catch (InvalidCommandException e) {
                        // Handle invalid command, but continue processing the next ones
                        System.err.println("Invalid command: " + command);
                        writeResponse(clientChannel, "500 Invalid command: " + command +"\n");
                    }

                    // Clear the accumulated command for the next one
                    accumulatedCommand.setLength(0);
                }
            }

        } catch (IOException e) {
            accumulatedCommand.setLength(0);  // Reset on exception
            e.printStackTrace();
        }
    }

    public void handleWrite(SelectionKey key, String response) throws IOException {
        buffer.clear();
        buffer.put(response.getBytes(StandardCharsets.UTF_8));
        buffer.flip();

        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }

        // Re-register interest in read operations after writing
        key.interestOps(SelectionKey.OP_READ);
    }

    private void writeResponse(SocketChannel clientChannel, String response) throws IOException {
        ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
        clientChannel.write(responseBuffer);
    }
}
