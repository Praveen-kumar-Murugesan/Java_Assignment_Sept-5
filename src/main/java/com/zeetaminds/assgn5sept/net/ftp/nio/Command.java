package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public interface Command {

    void execute(BufferedInputStream bin, OutputStream out) throws IOException;

    default void writeResponse(OutputStream out, String msg) throws IOException{
        out.write((msg+"\r\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}