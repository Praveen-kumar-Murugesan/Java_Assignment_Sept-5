package com.zeetaminds.assgn5sept.net.ftp.nio;

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

    public void handle() {
        try {
            if (!readDataFromClient()) return;

            while (stateManager.getBuffer().hasRemaining()) {
                if (stateManager.isExpectingFileContent()) {
                    stateManager.getCurrentPutCommand().execute(stateManager, clientChannel);
                } else {
                    processCommands();
                }
            }

        } catch (IOException e) {
            LOG.error("Error during client handling {}", e.getMessage());
            closeResources();
        }
    }

    private v
    private boolean readDataFromClient() throws IOException {
        ByteBuffer buffer = stateManager.getBuffer();
        stateManager.clearBuffer();

        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            closeResources();
            return false;
        }
        buffer.flip();
        return true;
    }

    private void closeResources() {
        try {
            if (clientChannel.isOpen()) clientChannel.close();

            stateManager.reset();
        } catch (IOException e) {
            LOG.error("Error closing resources {}", e.getMessage());
        }
    }
}


//        ByteBuffer buffer = bufferManager.getBuffer();
//        bufferManager.clearBuffer();
//        int bytesRead = clientChannel.read(buffer);
//
//        if (bytesRead == -1) {
//            clientChannel.close();
//            return;
//        }
//
//        buffer.flip();
//
//
////        while (buffer.hasRemaining()) {
////            try {
////                if (bufferManager.isExpectingFileContent()) {
////                    bufferManager.getCurrent().execute();
////                } else {
////                    Command cmd = commandParser.parseCommand(bufferManager);
////                    if (cmd != null) cmd.execute(bufferManager, clientChannel);
////                }
////            } catch (InvalidCommandException e) {
////                processInvalidCmd(e);
////            } catch (IOException e) {
////                closeResources();
////            }
////        }
//
//        if (bufferManager.isExpectingFileContent()) {
//            LOG.info("Resuming PUT command to receive file content for file: {}",
//                    bufferManager.getCurrentPutFilename());
//
//            PutCommand putCommand = new PutCommand(bufferManager.getCurrentPutFilename());
//            try {
//                putCommand.execute(bufferManager, clientChannel);
//            } catch (IOException | RuntimeException e) {
//                LOG.error("Error during file upload: {}", e.getMessage());
//
//                if (clientChannel.isOpen()) {
//                    clientChannel.close();
//                }
//
//                closeResources();
//                return;
//            }
//            if (!bufferManager.isExpectingFileContent()) {
//                LOG.info("File upload completed for: {}", bufferManager.getCurrentPutFilename());
//
//                closeResources();
//            }
//        }
//
//        while (buffer.hasRemaining() && !bufferManager.isExpectingFileContent()) {
//            try {
//                Command cmd = commandParser.parseCommand(bufferManager);
//                if (cmd != null) {
//                    cmd.execute(bufferManager, clientChannel);
//                } else {
//                    break;
//                }
//            } catch (InvalidCommandException e) {
//                LOG.error(e.getMessage());
//
//                String errorMessage = e.getMessage() + "\n\n";
//
//                ByteBuffer errorBuffer = ByteBuffer.wrap(errorMessage.getBytes());
//                clientChannel.write(errorBuffer);
//            } catch (IOException | RuntimeException e) {
//                LOG.error("Error executing command: {}", e.getMessage());
//
//
//                closeResources();
//                return;
//            }
//        }
//    }
//
//    private void closeResources() {
//        try {
//            bufferManager.closeOutputStream();
//            bufferManager.setCurrentOutputStream(null);
//            bufferManager.setExpectingFileContent(false);
//            bufferManager.setCurrentPutFilename(null);
//
//        } catch (IOException e) {
//            LOG.error("Error while closing resources: {}", e.getMessage());
//        }
//    }
//}