package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GetCommand implements Command {
    private final String fileName;

    public GetCommand(String token) {
        this.fileName = token;
    }

    @Override
    public void execute(BufferedInputStream in, OutputStream out) throws IOException {

        if (!(fileName.length() > 1)) {
            writeResponse(out, "501 Syntax error in parameters or arguments.");
            return;
        }

        File file = new File(fileName);
        if (!file.exists() && file.isDirectory()) {
            writeResponse(out, "550 File not found.");
            return;
        }

        long fileSize = Files.size(file.toPath());
        writeResponse(out, ("File size: " + fileSize + " bytes"));

        try (FileInputStream fileReader = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileReader.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        writeResponse(out, "\n225 File Downloaded Successfully.");
    }
}
