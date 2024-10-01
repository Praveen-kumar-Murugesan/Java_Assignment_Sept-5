package com.zeetaminds.assgn5sept.net.ftp.io;

import java.io.*;

public class PutCommand implements Command {

    private final static int DEFAULT_SIZE = 1024;
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
            int readIndex=0;

            bin.reset();

            while (!stopReading && (bytesRead = bin.read(buffer)) != -1) {
                    /*
                    read min 2 bytes
                    if file end. -> reset and mark, close fout
                    else continue reading
                     */
                readIndex = getIndex(buffer, bytesRead, bos);
                if(readIndex < bytesRead){
                    stopReading = true;
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

    private int getIndex(byte[] buffer, int bytesRead, FileOutputStream bos) throws IOException {

        for (int i = 0; i < bytesRead; i++) {
            index++;

            if (count == 1 && buffer[i] == 'q') {
                return index;
            }

            if (buffer[i] == ':') {

                if(count==1 && prev == ':'){
                    bos.write(':');
                    i++;
                    count=0;
                }else {
                    count=1;
                }

                while (i + 1 < bytesRead && buffer[i + 1] == ':') {
                    index++;

                    if (++count == 2) {
                        bos.write(':');
                        count = 0;
                    }

                    i++;
                }
                continue;
            }

            bos.write(buffer[i]);
            prev = buffer[i];
        }

        bos.flush();
        return index;
    }

}
