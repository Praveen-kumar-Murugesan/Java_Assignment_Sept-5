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

        File file = new File(fileName);

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
            ByteBuffer buffer = bufferManager.getBuffer();

            int bytesRead;
            boolean flag = true;
            boolean stopReading = false;

            while (!stopReading) {
                if ((buffer.position() < buffer.limit()) && flag) {
                    stopReading = writeToFile(buffer, bos);
                    buffer.compact();
                }

                flag = false;

                if (!stopReading) {
                    buffer.clear();
                    bytesRead = out.read(buffer);

                    if (bytesRead == -1) {
                        break;
                    }
                    if (bytesRead == 0) continue;

                    buffer.flip();
                    stopReading = writeToFile(buffer, bos);
                }
            }

            writeResponse(out, "\n222 File Uploaded Successfully.");

        } catch (IOException e) {
            writeResponse(out, "552 Could not create file.");
        }

    }

    private boolean writeToFile(ByteBuffer buffer, BufferedOutputStream bos) throws IOException {
        while (buffer.hasRemaining()) {
            byte currentByte = buffer.get();

            if (prev == ':' && count == 1 && currentByte == 'q') {
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