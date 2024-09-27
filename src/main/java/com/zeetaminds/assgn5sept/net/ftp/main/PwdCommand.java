package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PwdCommand implements Command {
    @Override
    public void execute(BufferedInputStream in, OutputStream out) throws IOException {
        String currentDir = new File(".").getAbsolutePath();
        writeResponse(out, ("100 \"" + currentDir + "\" is the current directory."));
    }
}
