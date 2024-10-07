package com.zeetaminds.assgn5sept.net.ftp.nio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

public class FTPServer {
    private static final Logger LOG = LogManager.getLogger(FTPServer.class);
    private static final int PORT = 8080;
    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) {
        try (Selector selector = Selector.open(); ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("FTP Server started on port " + PORT);

            //noinspection InfiniteLoopStatement
            while (true) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        BufferManager bufferManager = new BufferManager(BUFFER_SIZE);
                        clientChannel.register(selector, SelectionKey.OP_READ, new ClientHandler(clientChannel, bufferManager));

                        System.out.println("Client connected...");
                    } else if (key.isReadable()) {
                        ClientHandler handler = (ClientHandler) key.attachment();
                        handler.handle();
                    }
                    }
            }
        } catch (IOException e) {
            LOG.info("Error in ServerSocket: {}", e.getMessage());
        }
    }
}
