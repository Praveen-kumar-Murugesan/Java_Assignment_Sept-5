package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ListCommand implements Command {
    @Override
    public void execute(InputStream in, OutputStream out, String command) throws IOException {
        File dir = new File(".");
        File[] files = dir.listFiles();
        if (files != null) {
            out.write("150 Opening ASCII mode data connection for file list.\r\n".getBytes(StandardCharsets.UTF_8));
            out.write(("Number of files in Server: \r\n"+Long.toString(files.length)).getBytes());
            for (File file : files) {
                out.write((file.getName() + "\r\n").getBytes(StandardCharsets.UTF_8));
            }
        }
        out.write("226 Transfer complete.\r\n".getBytes(StandardCharsets.UTF_8));
    }
}