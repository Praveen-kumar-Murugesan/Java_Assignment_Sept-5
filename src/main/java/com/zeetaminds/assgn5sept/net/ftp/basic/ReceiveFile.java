package com.zeetaminds.assgn5sept.net.ftp.basic;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReceiveFile {
    private static final Logger LOG = LogManager.getLogger(ReceiveFile.class);

    public void receiveFile(DataInputStream dataInputStream, String filePath) throws IOException {
        int bytes;
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

        long size = dataInputStream.readLong();
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, Math.min(buffer.length, (int) size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;
        }
        fileOutputStream.close();
        LOG.info("File received: {}", filePath);
    }
}
