package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PutCommand implements Command {
    @Override
    public void execute(InputStream in, OutputStream out, String command) throws IOException {
        String[] tokens = command.split(" ", 2);

        if (tokens.length > 1) {
            String fileName = tokens[1];
            File file = new File(fileName);

            out.write(("150 Opening binary mode data connection for " + fileName + "\r\n").getBytes(StandardCharsets.UTF_8));

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead;

                boolean stopReading = false;
                int count=0;

                while (!stopReading && (bytesRead = in.read(buffer)) != -1) {
                    for (int i = 0; i < bytesRead; i++) {
                        byte currentByte = buffer[i];

                        if (currentByte == ':') {
                            if (i + 1 < bytesRead && buffer[i + 1] == 'q') {
                                stopReading = true;
                                break;
                            }
                        }
                        if (count == 1) {
                            if (buffer[i] == 'q') {
                                stopReading = true;
                                break;
                            }
                        }

                        if (currentByte == ':') {
                            count = 1;
                            while (i + 1 < bytesRead && buffer[i + 1] == ':') {
                                count++;
                                if(count == 2){
                                    bos.write(':');
                                    count=0;
                                }
                                i++;
                            }
                            continue;
                        }
                        bos.write(currentByte);
                    }
                    bos.flush();
                }

                out.write("226 Transfer complete.\r\n".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                out.write("552 Could not create file.\r\n".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            out.write("501 Syntax error in parameters or arguments.\r\n".getBytes(StandardCharsets.UTF_8));
        }
    }
}
