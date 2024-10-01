package com.zeetaminds.assgn5sept.net.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;

public class GetCommand implements Command {

    private final String fileName;

    public GetCommand(String token) {
        this.fileName = token;
    }

    @Override
    public void execute(ByteBuffer buffer, SocketChannel out) throws IOException {
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            writeResponse(out, "550 File not found.");
            return;
        }

        long fileSize = Files.size(file.toPath());
        writeResponse(out, ("File size: " + fileSize + " bytes"));

        try (FileInputStream fileReader = new FileInputStream(file)) {
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileReader.read(data)) != -1) {
                ByteBuffer fileBuffer = ByteBuffer.wrap(data, 0, bytesRead);
                while (fileBuffer.hasRemaining()) {
                    out.write(fileBuffer);
                }
            }
        }
        writeResponse(out, "\n225 File Downloaded Successfully.");
    }
}
