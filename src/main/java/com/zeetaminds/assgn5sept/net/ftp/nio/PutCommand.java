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
    public void execute(StateManager bufferManager, SocketChannel out) throws IOException, RuntimeException {
        BufferedOutputStream bos = gocFileStream(bufferManager);
            /*
            endReached = writeToFile(buff)
            if(endReached) clearRes();
             */
        ByteBuffer buffer = bufferManager.getBuffer();

        boolean stopReading = writeToFile(buffer, bos);

        bufferManager.setExpectingFileContent(!stopReading);

        if (stopReading) writeResponse(out, "\n222 File Uploaded Successfully.\n");
    }

    private BufferedOutputStream gocFileStream(StateManager bufferManager) throws FileNotFoundException {
        BufferedOutputStream bos = bufferManager.getCurrentOutputStream();
        if (bos != null) return bos;

        File file = new File(fileName);
        bos = new BufferedOutputStream(new FileOutputStream(file));
        bufferManager.setCurrentOutputStream(bos);
        bufferManager.setCurrentPutFilename(fileName);

        return bos;
    }

    /**
     * @param buffer File content from user.
     * @param bos Wrapped in FileOutputStream to write content to file.
     * @return True if the file's content has been fully written; False if there is still content left to be written.
     */

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