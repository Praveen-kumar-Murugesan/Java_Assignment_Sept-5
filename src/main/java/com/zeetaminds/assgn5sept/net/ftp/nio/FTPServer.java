package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FTPServer {
    private static final Logger LOG = LogManager.getLogger(FTPServer.class);
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);  // Non-blocking mode
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            LOG.info("FTP Server started on port {}", PORT);

            while (true) {
                selector.select();  // Blocking until at least one channel is ready
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        handleAccept(serverSocketChannel, selector);
                    } else if (key.isReadable()) {
                        ClientHandler clientHandler = (ClientHandler) key.attachment();
                        clientHandler.handleRead(key);
                    } else if (key.isWritable()) {
                        ClientHandler clientHandler = (ClientHandler) key.attachment();
                        clientHandler.handleWrite(key);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Error in FTPServer: {}", e.getMessage());
        }
    }

    private static void handleAccept(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);  // Non-blocking mode
        LOG.info("Client connected: {}", clientChannel.getRemoteAddress());
        ClientHandler clientHandler = new ClientHandler(clientChannel);
        clientChannel.register(selector, SelectionKey.OP_READ, clientHandler);
    }
}
