package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.StandardOpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetCommand implements Command {
    private final String fileName;

    public GetCommand(String token) {
        this.fileName = token;
    }

    @Override
    public void execute(ByteBuffer buffer, WritableByteChannel channel) throws IOException {
        Path filePath = Paths.get(fileName);
        File file = filePath.toFile();

        // Check if the file exists and is not a directory
        if (!file.exists() || file.isDirectory()) {
            writeResponse(channel, "550 File not found.");
            return;
        }

        // Check for read permissions
        if (!file.canRead()) {
            writeResponse(channel, "505 Read Permission Denied");
            return;
        }

        // Send the file size as a response
        long fileSize = file.length();
        writeResponse(channel, "File size: " + fileSize + " bytes");

        // Open the file and transfer its content using non-blocking I/O
        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            ByteBuffer fileBuffer = ByteBuffer.allocate(1024);
            int bytesRead;
            while ((bytesRead = fileChannel.read(fileBuffer)) != -1) {
                fileBuffer.flip();  // Prepare the buffer for reading
                channel.write(fileBuffer);
                fileBuffer.clear();  // Prepare the buffer for writing again
            }
        }
        writeResponse(channel, "225 File Downloaded Successfully.");
    }
}
