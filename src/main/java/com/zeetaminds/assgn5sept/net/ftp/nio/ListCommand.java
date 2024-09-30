package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class ListCommand implements Command {
    @Override
    public void execute(ByteBuffer buffer, WritableByteChannel channel) throws IOException {

        File dir = new File(".");
        File[] files = dir.listFiles();

        if (files == null) {
            writeResponse(channel, "000 Empty Folder");
            return;
        }

        writeResponse(channel, "150 Opening data connection for file list.");
        writeResponse(channel, ("Number of files in Server: " + (files.length)));

        for (File file : files) {
            writeResponse(channel, (file.getName()));
        }
        writeResponse(channel, "226 Transfer complete.");
    }
}