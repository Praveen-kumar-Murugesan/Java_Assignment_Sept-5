package com.zeetaminds.assgn5sept.net.ftp;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.Iterator;
import java.util.Set;

public class FTPServerNIO {
    private static final int BUFFER_SIZE = 1024;
    private static final String ROOT_DIR = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/net/ftp/server_files";
    private Selector selector;
    private ServerSocketChannel serverChannel;

    public static void main(String[] args) {
        new FTPServerNIO().startServer();
    }

    public void startServer() {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(8888));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("FTP Server started on port 8888");

            //noinspection InfiniteLoopStatement
            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        acceptConnection();
                    } else if (key.isReadable()) {
                        handleClientCommand(key);
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnection() throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Accepted connection from " + clientChannel.getRemoteAddress());
    }

    private void handleClientCommand(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();

        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            clientChannel.close();
            return;
        }

        String command = new String(buffer.array(), 0, bytesRead).trim();
        System.out.println("Received command: " + command);

        String[] commandParts = command.split(" ");
        String ftpCommand = commandParts[0].toUpperCase();

        switch (ftpCommand) {
            case "LIST":
                handleListCommand(clientChannel);
                break;
            case "GET":
                if (commandParts.length == 2) {
                    handleGetCommand(clientChannel, commandParts[1]);
                } else {
                    sendResponse(clientChannel, "Invalid GET command. Usage: GET <filename>");
                }
                break;
            case "PUT":
                if (commandParts.length == 2) {
                    handlePutCommand(clientChannel, commandParts[1]);
                } else {
                    sendResponse(clientChannel, "Invalid PUT command. Usage: PUT <filename>");
                }
                break;
            default:
                sendResponse(clientChannel, "Unknown command.");
                break;
        }
    }

    private void handleListCommand(SocketChannel clientChannel) throws IOException {
        Path dir = Paths.get(ROOT_DIR);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
        StringBuilder fileList = new StringBuilder();
        for (Path file : stream) {
            fileList.append(file.getFileName().toString()).append("\n");
        }

        sendResponse(clientChannel, fileList.toString());
        }
    }

    private void handleGetCommand(SocketChannel clientChannel, String fileName) throws IOException {
        Path filePath = Paths.get(ROOT_DIR, fileName);
        if (Files.exists(filePath)) {
            RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r");
            FileChannel fileChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                clientChannel.write(buffer);
                buffer.clear();
            }

            sendResponse(clientChannel, "File transfer completed. \n");
            file.close();
        } else {
            sendResponse(clientChannel, "File not found. \n");
        }
    }

    private void handlePutCommand(SocketChannel clientChannel, String fileName) throws IOException {
        Path filePath = Paths.get(ROOT_DIR, fileName);
        RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "rw");
        FileChannel fileChannel = file.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        while (clientChannel.read(buffer) > 0) {
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }

        fileChannel.close();
        sendResponse(clientChannel, "File upload completed. \n");
    }

    private void sendResponse(SocketChannel clientChannel, String message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        clientChannel.write(buffer);
    }
}