package com.zeetaminds.assgn5sept.net.chat.stream;

import java.io.IOException;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MessageHandler implements Runnable {
    protected Socket socket;
    protected static final Logger LOG = LogManager.getLogger(MessageHandler.class);

    public MessageHandler(Socket socket) {
        this.socket = socket;
    }

    protected void closeSocket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            LOG.error("Error closing socket: {}" , e.getMessage());
        }
    }
}
