package pkg.mark;

import java.io.BufferedInputStream;
import java.io.IOException;

public class SecondReader {

    public void readFromMark(BufferedInputStream in) throws IOException {
        // Reset the stream to the marked position
//        in.reset();
        in.reset();
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Now read from the marked position
        bytesRead = in.read(buffer);
        System.out.println("SecondReader: Read from marked position:");
//        System.out.println(new String(buffer, 0, bytesRead));

        for(int i=0; i<bytesRead; i++){
            System.out.print((char)buffer[i]);
        }
        in.reset();
        System.out.println("\n");
        for(int i=0; i<bytesRead; i++){
            System.out.print((char)buffer[i]);
        }
    }
}
