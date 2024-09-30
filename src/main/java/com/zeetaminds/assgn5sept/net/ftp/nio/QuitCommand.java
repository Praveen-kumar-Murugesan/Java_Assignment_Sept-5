package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class QuitCommand implements Command {
    private final SocketChannel clientChannel;

    public QuitCommand(SocketChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public void execute(ByteBuffer buffer, WritableByteChannel channel) throws IOException {
        writeResponse(clientChannel, "221 Goodbye.");
        clientChannel.close();
    }
}
