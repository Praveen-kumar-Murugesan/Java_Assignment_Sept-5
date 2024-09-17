package com.zeetaminds.assgn5sept.net.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class NIOChatServer {
    private static final int PORT = 1234;
    private static Map<SocketChannel, String> clientNames = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Chat server is listening on port " + PORT);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();

                if (key.isAcceptable()) {
                    try (ServerSocketChannel server = (ServerSocketChannel) key.channel()) {
                        SocketChannel clientChannel = server.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Client connected: " + clientChannel.getRemoteAddress());

                        clientNames.put(clientChannel, "Client" + clientChannel.hashCode());
                    }
                } else if (key.isReadable()) {
                    // Read data from the client
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = clientChannel.read(buffer);

                    if (bytesRead == -1) {
                        // Client disconnected
                        clientNames.remove(clientChannel);
                        clientChannel.close();
                        System.out.println("Client disconnected");
                    } else {
                        buffer.flip();
                        String message = new String(buffer.array(), 0, buffer.limit());
                        System.out.println("Received from client: " + message.trim());

                        // Broadcast the message to all other clients
                        broadcastMessage(clientChannel, message.trim(), selector);
                    }
                }
            }
        }
    }

    private static void broadcastMessage(SocketChannel senderChannel, String message, Selector selector) throws IOException {
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) {
                SocketChannel clientChannel = (SocketChannel) key.channel();
                if (clientChannel != senderChannel) {
                    ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                    clientChannel.write(buffer);
                }
            }
        }
    }
}
