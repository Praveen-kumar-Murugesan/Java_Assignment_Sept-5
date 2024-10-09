package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class PwdCommand implements Command {
    @Override
    public void execute(StateManager stateManager, SocketChannel out) throws IOException {

        String currentDir = new File(".").getAbsolutePath();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();

        writeResponse(out, ("100 \"" + currentDir + "\" is the current directory.\n"));
    }
}
