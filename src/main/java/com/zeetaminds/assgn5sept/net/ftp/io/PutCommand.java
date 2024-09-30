package com.zeetaminds.assgn5sept.net.ftp.io;

import java.io.*;

public class PutCommand implements Command {

    private final String fileName;
    private final static int DEFAULT_SIZE = 1024;

    public PutCommand(String token) {
        this.fileName = token;
    }

    @Override
    public void execute(BufferedInputStream bin, OutputStream out) throws IOException {

        File file = new File(fileName);

        try (FileOutputStream bos = new FileOutputStream(file)) {

            byte[] buffer = new byte[DEFAULT_SIZE];
            boolean stopReading = false;
            int bytesRead;
            int count = 0;
            int leftoverIndex = 0;

            bin.reset();

            while (!stopReading && (bytesRead = bin.read(buffer)) != -1) {
                    /*
                    read min 2 bytes
                    if file end. -> reset and mark, close fout
                    else continue reading
                     */
                for (int i = 0; i < bytesRead; i++) {
                    byte currentByte = buffer[i];
                    leftoverIndex++;

                    if (count == 1 && buffer[i] == 'q') {
                        stopReading = true;
                        break;
                    }
                    if (currentByte == ':') {
                        count++;
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
                bin.reset();
                bin.skip(leftoverIndex);
                bin.mark(DEFAULT_SIZE);
//            }
            writeResponse(out, "\n222 File Uploaded Successfully.");
        } catch (IOException e) {
            writeResponse(out, "552 Could not create file.");
        }

    }

}
