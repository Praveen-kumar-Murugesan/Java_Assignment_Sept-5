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
    private final BufferManager bufferManager;

    public ClientHandler(SocketChannel clientChannel, BufferManager bufferManager) {
        this.clientChannel = clientChannel;
        this.commandParser = CommandParser.getInstance();
        this.bufferManager = bufferManager;
    }

    public void handle() throws IOException {
        ByteBuffer buffer = bufferManager.getBuffer();
        buffer.clear();

        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            clientChannel.close();
            return;
        }

        buffer.flip();

        if (bufferManager.isExpectingFileContent()) {
            LOG.info("Resuming PUT command to receive file content for file: {}", bufferManager.getCurrentPutFilename());

            PutCommand putCommand = new PutCommand(bufferManager.getCurrentPutFilename());
            putCommand.execute(bufferManager, clientChannel);

            if (!bufferManager.isExpectingFileContent()) {
                LOG.info("File upload completed for: {}", bufferManager.getCurrentPutFilename());
                bufferManager.setCurrentPutFilename(null);
            }
        }

        while (buffer.hasRemaining() && !bufferManager.isExpectingFileContent()) {
            try {
                Command cmd = commandParser.parseCommand(bufferManager, clientChannel);
                if (cmd != null) {
                    cmd.execute(bufferManager, clientChannel);
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