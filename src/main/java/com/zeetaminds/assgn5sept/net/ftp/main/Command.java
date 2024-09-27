package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface Command {

    void execute(BufferedInputStream bin, OutputStream out, String command) throws IOException;
}