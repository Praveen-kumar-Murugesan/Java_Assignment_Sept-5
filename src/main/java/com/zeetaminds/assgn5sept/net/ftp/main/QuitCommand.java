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
    public void execute(BufferedInputStream in, OutputStream out, String command) throws IOException {
        out.write("221 Goodbye.\r\n".getBytes(StandardCharsets.UTF_8));
        out.flush();

        if(clientSocket!=null && !clientSocket.isClosed()){
            clientSocket.close();
        }
    }
}