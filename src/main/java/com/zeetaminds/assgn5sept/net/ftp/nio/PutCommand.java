package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PutCommand implements Command {

    private final String fileName;

    private byte prev;
    private int count = 0;

    public PutCommand(String token) {
        this.fileName = token;
    }


    @Override
    public void execute(StateManager stateManager, SocketChannel out) throws IOException, RuntimeException {
        BufferedOutputStream bos = gocFileStream(stateManager);
            /*
            endReached = writeToFile(buff)
            if(endReached) clearRes();
             */
        ByteBuffer buffer = stateManager.getBuffer();

        boolean stopReading = writeToFile(buffer, bos);

        stateManager.setExpectingFileContent(!stopReading);

        if (stopReading) writeResponse(out, "\n222 File Upload Successfully\n");

        else stateManager.setExpectingFileContent(true);

    }

    private BufferedOutputStream gocFileStream(StateManager stateManager) throws FileNotFoundException {
        BufferedOutputStream bos = stateManager.getCurrentOutputStream();

        if (bos != null) return bos;

        File file = new File(fileName);
        bos = new BufferedOutputStream(new FileOutputStream(file));
        stateManager.setCurrentOutputStream(bos);
        stateManager.setCurrentPutFilename(fileName);
        stateManager.setCurrentPutCommand(this);

        return bos;
    }

    private boolean writeToFile(ByteBuffer buffer, BufferedOutputStream bos) throws IOException {
        while (buffer.hasRemaining()) {
            byte currentByte = buffer.get();

            if (prev == ':' && count == 1 && currentByte == 'q') {
                bos.flush();
                return true;
            }

            // Handle colon ":" count logic for "::" sequence
            if (currentByte == ':') {
                if (count == 1 && prev == ':') {
                    bos.write(':'); // Write one colon
                    prev = currentByte;
                    count = 0;
                    continue;
                } else {
                    count = 1;
                }

                // Count consecutive colons "::"
                while (buffer.hasRemaining() && buffer.get(buffer.position()) == ':') {
                    if (++count == 2) {
                        bos.write(':'); // Write "::"
                        count = 0;
                    }
                    buffer.get();
                }

                prev = currentByte;
                continue;
            }

            bos.write(currentByte);
            prev = currentByte;
        }

        bos.flush();
        return false;
    }

}