package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class PutCommand implements Command {

    private final static int DEFAULT_SIZE = 1024;
    private final String fileName;

    public PutCommand(String token) {
        this.fileName = token;
    }

    @Override
    public void execute(ByteBuffer buffer, WritableByteChannel channel) throws IOException {

        File file = new File(fileName);

        if (file.exists() && !file.canWrite()) {
            writeResponse(channel, "510 Write Permission Denied");
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {

            byte[] byteBuffer = new byte[DEFAULT_SIZE];
            boolean stopReading = false;
            int bytesRead = 0;

            while (!stopReading && buffer.hasRemaining()) {
                bytesRead = Math.min(buffer.remaining(), DEFAULT_SIZE);
                buffer.get(byteBuffer, 0, bytesRead); // Read from ByteBuffer

                for (int i = 0; i < bytesRead; i++) {
                    byte currentByte = byteBuffer[i];

                    // Check for stopping condition (e.g., ':q')
                    if (byteBuffer[i] == ':' && i + 1 < bytesRead && byteBuffer[i + 1] == 'q') {
                        stopReading = true;
                        break;
                    }

                    // Write the byte to the file
                    fos.write(currentByte);
                }

                fos.flush();
            }

            writeResponse(channel, "222 File Uploaded Successfully.");
        } catch (IOException e) {
            writeResponse(channel, "552 Could not create file.");
        }
    }
}