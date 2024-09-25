package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Command {

    void execute(InputStream in, OutputStream out, String command) throws IOException;
}