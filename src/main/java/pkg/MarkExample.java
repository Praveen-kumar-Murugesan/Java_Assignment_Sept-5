package pkg;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MarkExample {
    public static void main(String[] args) {
        // Sample input data
        String data = "Hello, World! This is a test.";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getBytes());

        // Wrapping the byte array input stream with BufferedInputStream
        BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);

        try {
            // Mark the current position in the stream
            bufferedInputStream.mark(20); // Mark the position with a read limit of 20 bytes

            // Read some bytes
            byte[] buffer = new byte[10];
            int bytesRead = bufferedInputStream.read(buffer);
            System.out.println("First Read: " + new String(buffer, 0, bytesRead));

            // Reset the stream back to the marked position
            bufferedInputStream.reset();

            // Read again
            bytesRead = bufferedInputStream.read(buffer);
            System.out.println("After Reset: " + new String(buffer, 0, bytesRead));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

