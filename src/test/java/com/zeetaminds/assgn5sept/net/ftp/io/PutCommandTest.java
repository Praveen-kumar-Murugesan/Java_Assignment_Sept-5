package com.zeetaminds.assgn5sept.net.ftp.io;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PutCommandTest {

    @Test
    void execute_SuccessfulUpload() throws IOException {

        String testFileName = "testFile.txt";
        String inputContent = "Hello, World!:::q";
        String outputContent = "Hello, World!:";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        OutputStream out = mock(OutputStream.class);
        bin.mark(1024);

        PutCommand putCommand = new PutCommand(testFileName);
        putCommand.execute(bin, out);

        File uploadedFile = new File(testFileName);
        assertTrue(uploadedFile.exists(), "File should be created successfully.");

        try (FileInputStream fis = new FileInputStream(uploadedFile)) {
            byte[] fileContent = new byte[(int) uploadedFile.length()];
            int bytesRead = fis.read(fileContent);

            assertEquals(14, bytesRead, "The number of bytes read should match the input content length.");
            assertArrayEquals(outputContent.getBytes(), fileContent, "The content of the uploaded file should match.");
        }

        uploadedFile.delete();
    }

    @Test
    void execute_UploadWithStopCommand() throws IOException {

        String testFileName = "testFile.txt";
        String inputContent = "This is a test:::q more data";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        OutputStream out = mock(OutputStream.class);
        bin.mark(1024);

        PutCommand putCommand = new PutCommand(testFileName);
        putCommand.execute(bin, out);

        File uploadedFile = new File(testFileName);
        assertTrue(uploadedFile.exists(), "File should be created successfully.");

        try (FileInputStream fis = new FileInputStream(uploadedFile)) {
            byte[] fileContent = new byte[(int) uploadedFile.length()];
            int bytesRead = fis.read(fileContent);

            assertEquals("This is a test:".getBytes().length, bytesRead, "The number of bytes read should match the expected content length.");
            assertArrayEquals("This is a test:".getBytes(), fileContent, "The content of the uploaded file should match until 'q'.");
        }
        uploadedFile.delete();
    }

    @Test
    void execute_EmptyInputStream() throws IOException {

        String testFileName = "emptyFile.txt";
        String inputContent = ":q";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputContent.getBytes());
        BufferedInputStream bin = new BufferedInputStream(inputStream);
        OutputStream out = mock(OutputStream.class);
        bin.mark(1024);

        PutCommand putCommand = new PutCommand(testFileName);
        putCommand.execute(bin, out);

        File uploadedFile = new File(testFileName);
        assertTrue(uploadedFile.exists(), "File should be created successfully.");

        try (FileInputStream fis = new FileInputStream(uploadedFile)) {
            byte[] fileContent = new byte[(int) uploadedFile.length()];
            int bytesRead = fis.read(fileContent);

            assertEquals(0, bytesRead, "The content of the uploaded file should be empty.");
        }
        uploadedFile.delete();
    }
}
