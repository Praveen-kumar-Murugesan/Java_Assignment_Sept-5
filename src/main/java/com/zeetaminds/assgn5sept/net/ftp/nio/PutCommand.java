package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.StandardOpenOption;

public class PutCommand implements Command {

    private final static int DEFAULT_SIZE = 10;
    private final String fileName;
    private byte prev;
    private int count = 0;
    private int index = 0;

    public PutCommand(String token) {
        this.fileName = token;
    }

    @Override
    public void execute(BufferManager bufferManager, SocketChannel out) throws IOException {

        File file = new File(fileName);

        try (FileChannel fileChannel = FileChannel.open(file.toPath(),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            ByteBuffer buffer = ByteBuffer.allocate(DEFAULT_SIZE);
            boolean stopReading = false;
            int bytesRead;
            int readIndex = 0;

            while (!stopReading && (bytesRead = out.read(buffer)) != -1) {
                if (bytesRead == 0) {
                    continue; // No data read, continue reading
                }

                // Prepare buffer for reading (flip changes from writing mode to reading mode)
                buffer.flip();
                readIndex = writeToFile(buffer, fileChannel);

                if (readIndex < bytesRead) {
                    stopReading = true;
                }
                buffer.compact();
            }

            writeResponse(out, "\n222 File Uploaded Successfully.");

        } catch (IOException e) {
            writeResponse(out, "552 Could not create file.");
        }

    }

    private int writeToFile(ByteBuffer buffer, FileChannel fileChannel) throws IOException {

        while (buffer.hasRemaining()) {
            byte currentByte = buffer.get();
            index++;

            // Check the end condition (":q" marker)
            if (prev == ':' && count == 1 && currentByte == 'q') {
                return index - 1; // Stop writing here
            }

            // Handle colon ":" count logic for "::" sequence
            if (currentByte == ':') {
                if (count == 1 && prev == ':') {
                    fileChannel.write(ByteBuffer.wrap(new byte[]{':'})); // Write one colon
                    prev = currentByte;
                    count = 0;
                    continue;
                } else {
                    count = 1;
                }

                // Count consecutive colons "::"
                while (buffer.hasRemaining() && buffer.get(buffer.position()) == ':') {
                    index++;
                    if (++count == 2) {
                        fileChannel.write(ByteBuffer.wrap(new byte[]{':'})); // Write "::"
                        count = 0;
                    }
                    buffer.get();                                                                           // Move position forward
                }

                prev = currentByte;
                continue;
            }

            // Write current byte to file
            fileChannel.write(ByteBuffer.wrap(new byte[]{currentByte}));
            prev = currentByte;
        }

        fileChannel.force(true); // Force writing to disk
        return index;
    }

}