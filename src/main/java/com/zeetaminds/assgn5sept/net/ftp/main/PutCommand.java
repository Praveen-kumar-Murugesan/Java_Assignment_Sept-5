package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class PutCommand implements Command {
    @Override
    public void execute(InputStream in, OutputStream out, String command) throws IOException {
        String[] tokens = command.split(" ", 2);

        if (tokens.length > 1) {
            String fileName = tokens[1];
            File file = new File(fileName);

            // Respond to client that the server is ready to receive the file
            out.write(("150 Opening binary mode data connection for " + fileName + "\r\n").getBytes(StandardCharsets.UTF_8));

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                // Read from the actual client input stream
                while ((bytesRead = in.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }

                bos.flush();

                // Notify client that the transfer is complete
                out.write("226 Transfer complete.\r\n".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                out.write("553 Could not create file.\r\n".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            out.write("501 Syntax error in parameters or arguments.\r\n".getBytes(StandardCharsets.UTF_8));
        }
    }
}