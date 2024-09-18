package com.zeetaminds.assgn5sept.net.ftp.basic;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListFiles {
    private static final Logger LOG = LogManager.getLogger(ListFiles.class);

    private final String filePath;

    public ListFiles(String filePath) {
        this.filePath = filePath;
    }

    public void listFiles(DataOutputStream dataOutputStream) throws IOException {
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null && listOfFiles.length > 0) {
            dataOutputStream.writeUTF("Files on server:");
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    dataOutputStream.writeUTF(file.getName());
                }
            }
        } else {
            dataOutputStream.writeUTF("No files found on server.");
        }
        dataOutputStream.writeUTF("END_OF_LIST");
        dataOutputStream.flush();
        LOG.info("List of files sent.");
    }
}
