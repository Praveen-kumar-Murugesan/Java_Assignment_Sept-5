package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseSender {
    public void sendResponse(InputStream in, OutputStream out, String response) throws IOException {
        out.write((response + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}