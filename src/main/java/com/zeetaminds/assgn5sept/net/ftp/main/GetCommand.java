package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GetCommand implements Command {
    @Override
    public void execute(BufferedInputStream in, OutputStream out, String fileName) throws IOException {
        if (fileName.length() > 1) {

            File file = new File(fileName);
            if (file.exists() && !file.isDirectory()) {

                long fileSize = Files.size(file.toPath());
                out.write(("File size: " + fileSize + " bytes\r\n").getBytes(StandardCharsets.UTF_8));

                try (FileInputStream fileReader = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileReader.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                out.write("\n225 File Downloaded Successfully.\r\n\n".getBytes(StandardCharsets.UTF_8));
            } else {
                out.write("550 File not found.\r\n".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            out.write("501 Syntax error in parameters or arguments.\r\n".getBytes(StandardCharsets.UTF_8));
        }
    }
}
