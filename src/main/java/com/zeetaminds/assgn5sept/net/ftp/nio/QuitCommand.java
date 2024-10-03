package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;

public class QuitCommand implements Command {
    private final SocketChannel clientSocket;

    public QuitCommand(SocketChannel clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void execute(BufferManager bufferManager, SocketChannel out) throws IOException {

        writeResponse(out, "221 Goodbye.");

        if(clientSocket!=null && clientSocket.isOpen()){
            clientSocket.close();
        }
    }
}