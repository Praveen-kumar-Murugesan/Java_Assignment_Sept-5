package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

public interface Command {

    void execute(ByteBuffer buffer, WritableByteChannel channel) throws IOException;

    default void writeResponse(WritableByteChannel channel, String msg) throws IOException {
        ByteBuffer responseBuffer = ByteBuffer.wrap((msg + "\r\n").getBytes(StandardCharsets.UTF_8));
        while (responseBuffer.hasRemaining()) {
            channel.write(responseBuffer);
        }
    }
}
