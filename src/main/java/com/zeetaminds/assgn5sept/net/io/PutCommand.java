package com.zeetaminds.assgn5sept.net.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PutCommand implements Command {

    private static final int DEFAULT_SIZE = 1024;
    private final String fileName;
    private byte prev;
    private int count = 0;
    private int index = 0;

    public PutCommand(String token) {
        this.fileName = token;
    }

    @Override
    public void execute(ByteBuffer buffer, SocketChannel clientChannel) throws IOException {

        File file = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {

            boolean stopReading = false;

            // Read data from ByteBuffer and write to the file until marker ":q" is found
            while (!stopReading) {
                buffer.flip(); // Prepare buffer for reading
                int bytesRead = buffer.remaining(); // Get remaining data in the buffer
                byte[] byteArray = new byte[bytesRead];
                buffer.get(byteArray); // Read data from ByteBuffer into byteArray

                int readIndex = getIndex(byteArray, bytesRead, fos);
                if (readIndex < bytesRead) {
                    stopReading = true; // Stop when ":q" marker is found
                }

                buffer.clear(); // Clear buffer for the next read
                if (clientChannel.read(buffer) == -1) {
                    break; // Client closed the connection
                }
            }

            writeResponse(clientChannel, "\n222 File Uploaded Successfully.");

        } catch (IOException e) {
            writeResponse(clientChannel, "552 Could not create file.");
        }
    }

    private int getIndex(byte[] buffer, int bytesRead, FileOutputStream fos) throws IOException {

        for (int i = 0; i < bytesRead; i++) {
            index++;

            // Detect the end marker ":q"
            if (count == 1 && buffer[i] == 'q') {
                return index;
            }
            char c = (char) buffer[i];
            if (buffer[i] == ':') {
                if (count == 1 && prev == ':') {
                    fos.write(':');
                    i++;
                    count = 0;
                } else {
                    count = 1;
                }

                while (i + 1 < bytesRead && buffer[i + 1] == ':') {
                    index++;

                    if (++count == 2) {
                        fos.write(':');
                        count = 0;
                    }

                    i++;
                }
                continue;
            }

            // Write the content of the buffer to the file
            fos.write(buffer[i]);
            prev = buffer[i];
        }

        fos.flush(); // Ensure all data is written to the file
        return index;
    }
}
