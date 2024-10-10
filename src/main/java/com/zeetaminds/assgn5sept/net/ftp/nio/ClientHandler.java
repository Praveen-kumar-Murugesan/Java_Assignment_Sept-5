package com.zeetaminds.assgn5sept.net.ftp.nio;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientHandler {

    private static final Logger LOG = LogManager.getLogger(ClientHandler.class);

    private final SocketChannel clientChannel;
    private final CommandParser commandParser;
    private final StateManager stateManager;

    ClientHandler(SocketChannel clientChannel, StateManager stateManager) {
        this.clientChannel = clientChannel;
        this.commandParser = CommandParser.getInstance();
        this.stateManager = stateManager;
    }

    public void handle() {
        try {
            ByteBuffer buffer = stateManager.getBuffer();
            buffer.clear();

            if (clientChannel.read(buffer) < 0) throw new IOException("Read -1");
            buffer.flip();

            while (buffer.hasRemaining()) {
                if (!_handle()) break;
            }
        } catch (IOException | RuntimeException e) {
            LOG.error("Error in handling client {}", e.getMessage());
            closeResources();
        }
    }

    private boolean _handle() throws IOException {
        try {
            Command cmd = stateManager.getCurrentPutCommand()!= null ? stateManager.getCurrentPutCommand()
                    : commandParser.parseCommand(stateManager);

            if (cmd != null) cmd.execute(stateManager, clientChannel);
            else return false;
        } catch (InvalidCommandException e) {
            sendErrorToClient(e.getMessage());
        }
        return true;
    }

    private void closeResources() {
        try {
            stateManager.reset();

            if (clientChannel.isOpen()) {
                clientChannel.close();
            }
        } catch (IOException e) {
            LOG.error("Error closing resources: {}", e.getMessage(), e);
        }
    }

    private void sendErrorToClient(String msg) throws IOException {
        LOG.error(msg);
        String errorMessage = msg + "\n\n";

        ByteBuffer errorBuffer = ByteBuffer.wrap(errorMessage.getBytes());
        clientChannel.write(errorBuffer);
    }
}