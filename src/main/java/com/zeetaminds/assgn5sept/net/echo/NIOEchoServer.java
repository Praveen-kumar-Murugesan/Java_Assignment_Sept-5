package com.zeetaminds.assgn5sept.net.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NIOEchoServer {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("NIO Echo server is listening on port " + PORT);

        //noinspection InfiniteLoopStatement
        while (true) {
            selector.select();

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {
                    try(
                    ServerSocketChannel server = (ServerSocketChannel) key.channel()) {
                        SocketChannel clientChannel = server.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
                    }
                } else if (key.isReadable()) {
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = clientChannel.read(buffer);

                    if (bytesRead == -1) {
                        clientChannel.close();
                        System.out.println("Client disconnected: "+ clientChannel.getRemoteAddress());
                    } else {
                        buffer.flip();
                        String message = new String(buffer.array(), 0, buffer.limit());
                        System.out.println("Received from client: " + message.trim());

                        buffer.rewind();
                        clientChannel.write(buffer);
                    }
                }
            }
        }
    }
}
