package pkg.mark;

import java.io.BufferedInputStream;
import java.io.IOException;

public class FirstReader {

    public void readData(BufferedInputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
in.mark(1024);
        bytesRead = in.read(buffer);
        System.out.println("FirstReader: Read data:");
//        System.out.println(new String(buffer, 0, bytesRead));
        for(int i=0; i<bytesRead; i++){
            System.out.print((char)buffer[i]);
        }

        System.out.println("\n");
        in.reset();
        in.skip(10);
        in.mark(1024);
//        in.read(buffer); // Read some more
//        System.out.println("FirstReader: Read more data (not marked):");
//        System.out.println(new String(buffer, 0, bytesRead));

        SecondReader sr = new SecondReader();
        sr.readFromMark(in);
    }
}
