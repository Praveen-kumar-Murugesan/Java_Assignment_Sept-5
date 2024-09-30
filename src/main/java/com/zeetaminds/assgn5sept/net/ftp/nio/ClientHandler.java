package com.zeetaminds.assgn5sept.net.ftp.nio;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class ClientHandler {

    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);

    private final SocketChannel clientChannel;
    private final CommandParser commandParser;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public ClientHandler(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
        this.commandParser = CommandParser.getInstance();
    }

    public void handleRead(SelectionKey key) {
        try {
            buffer.clear();
            int bytesRead = clientChannel.read(buffer);

            if (bytesRead == -1) {
                clientChannel.close();
                key.cancel();
                System.out.println("Client disconnected.");
                return;
            }

            buffer.flip();
            String commandLine = new String(buffer.array(), 0, buffer.limit());
            Command cmd = commandParser.readCommand(commandLine, clientChannel);
            if (cmd != null) {
                cmd.execute(buffer, clientChannel);
                key.interestOps(SelectionKey.OP_WRITE);
            }
        } catch (IOException | InvalidCommandException e) {
            try {
                clientChannel.write(ByteBuffer.wrap(("500 " + e.getMessage() + "\r\n").getBytes()));
            } catch (IOException ex) {
                LOG.error("Error writing response: {}", ex.getMessage());
            }
        }
    }

    public void handleWrite(SelectionKey key) {
        // Implement response writing logic here
        // After writing the response, reset interest ops to OP_READ
        key.interestOps(SelectionKey.OP_READ);
    }
}