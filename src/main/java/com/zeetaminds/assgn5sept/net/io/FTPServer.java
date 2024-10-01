package com.zeetaminds.assgn5sept.net.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class FTPServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            // Configure server socket for non-blocking mode
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);

            // Register the server socket with the selector to accept connections
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("FTP Server started on port " + PORT);

            //noinspection InfiniteLoopStatement
            while (true) {
                selector.select();  // Wait for events

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        // Accept the client connection
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        System.out.println("Client connected...");

                        // Register client for reading
                        clientChannel.register(selector, SelectionKey.OP_READ, new ClientHandler(clientChannel));
                    } else if (key.isReadable()) {
                        // Handle reading from the client
                        ClientHandler handler = (ClientHandler) key.attachment();
                        handler.handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
