package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public interface Command {

    void execute(BufferManager bufferManager, SocketChannel clientChannel) throws IOException;

    default void writeResponse(SocketChannel clientChannel, String msg) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap((msg + "\r\n").getBytes(StandardCharsets.UTF_8));
        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }
}
