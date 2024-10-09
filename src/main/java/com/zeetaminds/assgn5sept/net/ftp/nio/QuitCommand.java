package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class QuitCommand implements Command {
    @Override
    public void execute(StateManager stateManager, SocketChannel out) throws IOException {

        writeResponse(out, "221 Goodbye.");

        if(out!=null && out.isOpen()){
            out.close();
        }
    }
}