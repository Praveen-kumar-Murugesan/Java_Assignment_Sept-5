package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class PwdCommand implements Command {
    @Override
    public void execute(ByteBuffer buffer, WritableByteChannel channel) throws IOException {

        String currentDir = new File(".").getAbsolutePath();

        writeResponse(channel, ("100 \"" + currentDir + "\" is the current directory."));
    }
}
