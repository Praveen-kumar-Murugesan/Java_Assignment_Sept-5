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

    public ClientHandler(SocketChannel clientChannel, StateManager stateManager) {
        this.clientChannel = clientChannel;
        this.commandParser = CommandParser.getInstance();
        this.stateManager = stateManager;
    }

    public void handle() throws IOException {
        ByteBuffer buffer = stateManager.getBuffer();
        buffer.clear();

        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            clientChannel.close();
            return;
        }

        buffer.flip();

        if (stateManager.isExpectingFileContent()) {
            LOG.info("Resuming PUT command to receive file content for file: {}", stateManager.getCurrentPutFilename());

            PutCommand putCommand = new PutCommand(stateManager.getCurrentPutFilename());
            putCommand.execute(stateManager, clientChannel);

            if (!stateManager.isExpectingFileContent()) {
                LOG.info("File upload completed for: {}", stateManager.getCurrentPutFilename());
                stateManager.setCurrentPutFilename(null);
            }
        }

        while (buffer.hasRemaining() && !stateManager.isExpectingFileContent()) {
            try {
                Command cmd = commandParser.parseCommand(stateManager);
                if (cmd != null) {
                    cmd.execute(stateManager, clientChannel);
                } else {
                    break;
                }
            } catch (InvalidCommandException e) {
                LOG.error(e.getMessage());

                String errorMessage = e.getMessage() + "\n";

                ByteBuffer errorBuffer = ByteBuffer.wrap(errorMessage.getBytes());
                clientChannel.write(errorBuffer);
            }
        }
    }
}