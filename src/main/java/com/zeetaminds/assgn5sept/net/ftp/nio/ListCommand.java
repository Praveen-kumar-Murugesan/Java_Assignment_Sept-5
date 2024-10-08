package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class ListCommand implements Command {
    @Override
    public void execute(BufferManager bufferManager, SocketChannel out) throws IOException {

        File dir = new File(".");
        File[] files = dir.listFiles();

        if (files == null) {
            writeResponse(out, "000 Empty Folder\n");
            return;
        }

        writeResponse(out, "150 Opening data connection for file list.");
        writeResponse(out, ("Number of files in Server: " + (files.length)));

        for (File file : files) {
            writeResponse(out, (file.getName()));
        }
        writeResponse(out, "226 Transfer complete.\n");
    }
}