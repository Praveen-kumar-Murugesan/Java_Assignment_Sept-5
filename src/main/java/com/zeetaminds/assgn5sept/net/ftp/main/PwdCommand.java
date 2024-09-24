package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PwdCommand implements Command {
    @Override
    public void execute(InputStream in, OutputStream out, String command) throws IOException {
        String currentDir = new File(".").getAbsolutePath();
        out.write(("257 \"" + currentDir + "\" is the current directory.\r\n").getBytes(StandardCharsets.UTF_8));
    }
}
