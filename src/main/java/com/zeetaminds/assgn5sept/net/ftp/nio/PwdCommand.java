//package com.zeetaminds.assgn5sept.net.ftp.nio;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.OutputStream;
//
//public class PwdCommand implements Command {
//    @Override
//    public void execute(BufferedInputStream in, OutputStream out) throws IOException {
//
//        String currentDir = new File(".").getAbsolutePath();
//
//        writeResponse(out, ("100 \"" + currentDir + "\" is the current directory."));
//    }
//}
