package com.zeetaminds.assgn5sept.net.ftp.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ListCommandTest {

    private final String[] testFiles = {"test1.txt", ".2.txt", "file3.txt"};
    private final String testDir = ".";

    @BeforeEach
    void setUp() throws IOException {
        for (String fileName : testFiles) {
            File file = new File(testDir, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("This is a test file: " + fileName); // Writing some content
            }
        }
    }

    @AfterEach
    void tearDown() {
        for (String fileName : testFiles) {
            File file = new File(testDir, fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @Test
    void execute_NonEmptyFolder() throws IOException {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ListCommand listCommand = new ListCommand();

        // Act
        listCommand.execute(null, out);

        // Assert - Verify the correct response sequence
        String response = out.toString();
        assertTrue(response.contains("150 Opening data connection for file list."),
                "Expected file list opening message.");
        assertTrue(response.contains("Number of files in Server: 3"),
                "Expected the correct number of files.");
        assertTrue(response.contains("test1.txt"),
                "Expected the first file in the list.");
        assertTrue(response.contains(".2.txt"),
                "Expected the second file in the list.");
        assertTrue(response.contains("file3.txt"),
                "Expected the third file in the list.");
        assertTrue(response.contains("226 Transfer complete."),
                "Expected the transfer completion message.");
    }
}
