package com.zeetaminds.assgn5sept.net.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public interface Command {

    void execute(ByteBuffer buffer, SocketChannel clientChannel) throws IOException;

    default void writeResponse(SocketChannel clientChannel, String msg) throws IOException{
        ByteBuffer responseBuffer = ByteBuffer.wrap((msg + "\r\n").getBytes());
        while (responseBuffer.hasRemaining()) {
            clientChannel.write(responseBuffer);
        }
    }
}