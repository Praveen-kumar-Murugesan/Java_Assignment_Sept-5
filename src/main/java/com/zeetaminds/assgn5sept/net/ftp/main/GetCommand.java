package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GetCommand implements Command {
    @Override
    public void execute(InputStream in, OutputStream out, String command) throws IOException {
        String[] tokens = command.split(" ", 2);
        if (tokens.length > 1) {
            File file = new File(tokens[1]);
            if (file.exists() && !file.isDirectory()) {
                long fileSize = Files.size(file.toPath());
                out.write(("File size: " + fileSize + " bytes\r\n").getBytes(StandardCharsets.UTF_8));

                try (BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(file))) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileReader.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
                out.write("\n225 Transfer complete.\r\n\n".getBytes(StandardCharsets.UTF_8));
            } else {
                out.write("550 File not found.\r\n".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            out.write("501 Syntax error in parameters or arguments.\r\n".getBytes(StandardCharsets.UTF_8));
        }
    }
}
