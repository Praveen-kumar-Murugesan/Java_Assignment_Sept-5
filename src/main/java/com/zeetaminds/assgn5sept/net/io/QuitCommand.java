package com.zeetaminds.assgn5sept.net.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class QuitCommand implements Command {
    private final Socket clientSocket;

    public QuitCommand(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void execute(ByteBuffer buffer, SocketChannel out) throws IOException {

        writeResponse(out, "221 Goodbye.");

        if(clientSocket!=null && !clientSocket.isClosed()){
            clientSocket.close();
        }
    }
}