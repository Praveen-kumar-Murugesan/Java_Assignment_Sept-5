package pkg.mark;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MarkResetExample {
    public static void main(String[] args) {
        // Sample byte array
        byte[] data = "This is a test data stream to demonstrate mark and reset functionality.".getBytes();

        // Create a ByteArrayInputStream to simulate input

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(data));

        // Create the first reader
        FirstReader firstReader = new FirstReader();
        try {
            firstReader.readData(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
