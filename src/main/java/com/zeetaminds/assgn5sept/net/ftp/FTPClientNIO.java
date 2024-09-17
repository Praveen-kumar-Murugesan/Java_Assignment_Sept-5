package com.zeetaminds.assgn5sept.net.ftp;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Scanner;

public class FTPClientNIO {
    private static final int BUFFER_SIZE = 1024;
    private SocketChannel clientChannel;
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    public static void main(String[] args) {
        new FTPClientNIO().startClient();
    }

    public void startClient() {
        try {
            // Open a connection to the server
            selector = Selector.open();
            clientChannel = SocketChannel.open();
            clientChannel.configureBlocking(false);
            clientChannel.connect(new InetSocketAddress("localhost", 8888));

            // Register the channel with the selector
            clientChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);

            // Wait for the connection to complete
            while (!clientChannel.finishConnect()) {
                // No operation required
            }

            System.out.println("Connected to FTP server");

            // Start the interaction with the server
            interactWithServer();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void interactWithServer() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Handle the server prompt
            int numKeys = 0;
            while (numKeys == 0) {
                numKeys = selector.select();
            }

            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isReadable()) {
                    handleServerResponse();
                }

                if (key.isWritable()) {
                    handleUserInput(scanner);
                }
            }
        }
    }

    private void handleServerResponse() throws IOException {
        buffer.clear();
        int bytesRead = clientChannel.read(buffer);
        if (bytesRead > 0) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            String response = new String(data);
            System.out.print(response); // Print server response
        }
    }

    private void handleUserInput(Scanner scanner) throws IOException {
        System.out.print("ftp> ");
        String command = scanner.nextLine() + "\n";
        buffer.clear();
        buffer.put(command.getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }
}
