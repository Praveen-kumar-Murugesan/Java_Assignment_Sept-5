package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class QuitCommand implements Command {
    private final Socket clientSocket;

    public QuitCommand(Socket clientSocket){
        this.clientSocket = clientSocket;
    }


    @Override
    public void execute(BufferedInputStream in, OutputStream out) throws IOException {

        writeResponse(out, "221 Goodbye.");

        if(clientSocket!=null && !clientSocket.isClosed()){
            clientSocket.close();
        }
    }
}