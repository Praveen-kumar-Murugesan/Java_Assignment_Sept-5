package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PutCommand implements Command {

    private final String fileName;
    BufferedOutputStream bos;

    private byte prev;
    private int count = 0;

    public PutCommand(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public void execute(StateManager stateManager, SocketChannel out) throws IOException, RuntimeException {
        if (stateManager.getCurrentPutCommand() == null) {
            stateManager.setCurrentPutCommand(this);
        }

        if(bos==null) bos = createFileStream();
            /*
            endReached = writeToFile(buff)
            if(endReached) clearRes();
             */
        ByteBuffer buffer = stateManager.getBuffer();

        if(writeToFile(buffer, bos)){
            reset();
            stateManager.clear();
            writeResponse(out, "\n222 File Upload Successfully\n");
        }

    }

    private BufferedOutputStream createFileStream() throws FileNotFoundException {
        File file = new File(fileName);
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    private boolean writeToFile(ByteBuffer buffer, BufferedOutputStream bos) throws IOException {
        while (buffer.hasRemaining()) {
            byte currentByte = buffer.get();

            if (isUploadTerminated(currentByte)) {
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
                colonCount(buffer);

                prev = currentByte;
                continue;
            }

            bos.write(currentByte);
            prev = currentByte;
        }
        bos.flush();
        return false;
    }

    private void colonCount(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining() && buffer.get(buffer.position()) == ':') {
            if (++count == 2) {
                bos.write(':'); // Write "::"
                count = 0;
            }
            buffer.get();
        }
    }

    private boolean isUploadTerminated(byte currentByte) {
        return prev == ':' && count == 1 && currentByte == 'q';
    }

    private void reset() throws IOException {
        if (bos != null) {
            bos.close();  // Close the stream when done
            bos = null;   // Clear the reference for future uploads
        }
        prev = 0;
        count = 0;
    }
}