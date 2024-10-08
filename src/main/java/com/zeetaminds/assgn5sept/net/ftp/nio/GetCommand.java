package com.zeetaminds.assgn5sept.net.ftp.nio;

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
    public void execute(BufferManager bufferManager, SocketChannel out) throws IOException {
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            writeResponse(out, "550 File not found.\n");
            return;
        }

        long fileSize = Files.size(file.toPath());
        writeResponse(out, ("File size: " + fileSize + " bytes"));

        try (FileInputStream fileReader = new FileInputStream(file)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead;
            while ((bytesRead = fileReader.read(buffer.array())) != -1) {
                buffer.limit(bytesRead);
                out.write(buffer);
                buffer.clear();
            }
        }
        writeResponse(out, "\n225 File Downloaded Successfully.\n");
    }
}
