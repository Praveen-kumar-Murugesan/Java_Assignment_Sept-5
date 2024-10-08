package com.zeetaminds.assgn5sept.net.ftp.io;

import java.io.*;

public class PutCommand implements Command {

    private final static int DEFAULT_SIZE = 10;
    private final String fileName;
    private byte prev;
    private int count = 0;
    private int index = 0;

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
            int readIndex = 0;

                /*  read min 2 bytes
                    if file end. -> reset and mark, close fout
                    else continue reading
                     */

            while (!stopReading && (bytesRead = bin.read(buffer)) != -1) {
                readIndex = writeToFile(buffer, bytesRead, bos);

                if (readIndex < bytesRead) {
                    stopReading = true;
                } else {
                    bin.mark(DEFAULT_SIZE);
                    index = 0;
                }
            }

            bin.reset();
            bin.skip(readIndex);
            bin.mark(DEFAULT_SIZE);

            writeResponse(out, "\n222 File Uploaded Successfully.");

        } catch (IOException e) {
            writeResponse(out, "552 Could not create file.");
        }

    }

    private int writeToFile(byte[] buffer, int bytesRead, FileOutputStream bos) throws IOException {

        for (int i = 0; i < bytesRead; i++) {
            index++;

            //to check the end condition
            if (prev == ':' && count == 1 && buffer[i] == 'q') {
                return index;
            }

            //to check previous buffer ends with :
            if (buffer[i] == ':') {
                if (count == 1 && prev == ':') {
                    bos.write(':');
                    prev = buffer[i];
                    count = 0;
                    continue;
                } else {
                    count = 1;
                }

                //count the number of colon
                while (i + 1 < bytesRead && buffer[i + 1] == ':') {
                    index++;
                    if (++count == 2) {
                        bos.write(':');
                        count = 0;
                    }
                    i++;
                }
                prev = buffer[i];
                continue;
            }
            bos.write(buffer[i]);
            prev = buffer[i];
        }
        bos.flush();
        return index;
    }

}