package com.zeetaminds.assgn5sept.net.ftp.basic;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendFile {
    private static final Logger LOG = LogManager.getLogger(SendFile.class);

    public static void sendFile(DataOutputStream dataOutputStream, String filePath) throws Exception {
        int bytes;
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            LOG.error("File not found: {}", filePath);
            dataOutputStream.writeUTF("ERROR: File not found");
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(file);

        long fileSize = file.length();
        dataOutputStream.writeLong(fileSize);

        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytes);
        }
        dataOutputStream.flush();
        fileInputStream.close();
        LOG.info("File sent: {}", filePath);
    }
}
