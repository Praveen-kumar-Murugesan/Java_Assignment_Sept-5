package com.zeetaminds.assgn5sept.net.ftp.main;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PutCommand implements Command {

    @Override
    public void execute(BufferedInputStream bin, OutputStream out, String fileName) throws IOException {

        if (fileName.length() > 1) {
            File file = new File(fileName);

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;

                boolean stopReading = false;
                int count = 0;
                int leftoverIndex = 0;

                bin.reset();
                while (!stopReading && (bytesRead = bin.read(buffer)) != -1) {
                    for (int i = 0; i < bytesRead; i++) {
                        byte currentByte = buffer[i];
                        leftoverIndex++;
                        if (currentByte == ':') {
                            if (i + 1 < bytesRead && buffer[i + 1] == 'q') {
                                stopReading = true;
                                leftoverIndex ++;
                                break;
                            }
                        }

                        if (count == 1) {
                            if (buffer[i] == 'q') {
                                stopReading = true;
//                                leftoverIndex++;
                                break;
                            }
                        }

                        if (currentByte == ':') {
                            count = 1;
                            while (i + 1 < bytesRead && buffer[i + 1] == ':') {
                                leftoverIndex++;
                                count++;
                                if (count == 2) {
                                    bos.write(':');
                                    count = 0;
                                }
                                i++;
                            }
                            continue;
                        }
                        bos.write(currentByte);
                    }
                    bos.flush();
                }
                if(leftoverIndex < bytesRead){
                    bin.reset();
                    bin.skip(leftoverIndex);
                    bin.mark(1024);
                }
                out.write("226 Transfer complete.\r\n\n".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                out.write("552 Could not create file.\r\n\n".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            out.write("501 Syntax error in parameters or arguments.\r\n\n".getBytes(StandardCharsets.UTF_8));
        }
    }

}
