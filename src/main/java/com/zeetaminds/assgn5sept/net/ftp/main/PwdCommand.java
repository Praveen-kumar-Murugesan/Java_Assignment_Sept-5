package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PwdCommand implements Command {
    @Override
    public void execute(BufferedInputStream in, OutputStream out, String command) throws IOException {
        String currentDir = new File(".").getAbsolutePath();
        out.write(("100 \"" + currentDir + "\" is the current directory.\r\n\n").getBytes(StandardCharsets.UTF_8));
    }
}
