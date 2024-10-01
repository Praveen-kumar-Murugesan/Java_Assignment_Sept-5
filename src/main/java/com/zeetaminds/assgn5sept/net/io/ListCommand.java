package com.zeetaminds.assgn5sept.net.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ListCommand implements Command {
    @Override
    public void execute(ByteBuffer buffer, SocketChannel out) throws IOException {

        File dir = new File(".");
        File[] files = dir.listFiles();

        if (files == null) {
            writeResponse(out, "000 Empty Folder");
            return;
        }

        writeResponse(out, "150 Opening data connection for file list.");
        writeResponse(out, ("Number of files in Server: " + (files.length)));

        for (File file : files) {
            writeResponse(out, (file.getName()));
        }
        writeResponse(out, "226 Transfer complete.");
    }
}