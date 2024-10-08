package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public void execute(BufferManager bufferManager, SocketChannel out) throws IOException {
        try {
            BufferedOutputStream bos = bufferManager.getCurrentOutputStream();

            if (bos == null) {
                File file = new File(fileName);
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bufferManager.setCurrentOutputStream(bos);
                bufferManager.setCurrentPutFilename(fileName);
            }
            /*
            endReached = writeToFile(buff)
            if(endReached) clearRes();
             */

            ByteBuffer buffer = bufferManager.getBuffer();

            boolean stopReading = false;
            if (buffer.hasRemaining()) stopReading = writeToFile(bufferManager, buffer, bos, out);

            bufferManager.setExpectingFileContent(!stopReading);

        } catch (IOException e) {
            writeResponse(out, "552 Could not create file.\n");
            bufferManager.setCurrentOutputStream(null);
            bufferManager.setExpectingFileContent(false);
            bufferManager.setCurrentPutFilename(null);
        }

    }

    private boolean writeToFile(BufferManager bufferManager, ByteBuffer buffer, BufferedOutputStream bos, SocketChannel out)
            throws IOException {
        while (buffer.hasRemaining()) {
            byte currentByte = buffer.get();

            if (prev == ':' && count == 1 && currentByte == 'q') {
                writeResponse(out, "\n222 File Uploaded Successfully.\n");
                bos.flush();
                bufferManager.setCurrentOutputStream(null);
                bufferManager.setExpectingFileContent(false);
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