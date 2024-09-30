package com.zeetaminds.assgn5sept.net.ftp.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GetCommandTest {

    private final String testFileName = "test.txt";
    private final String testFileContent = "This is a test file for GetCommand.:q";
    private final String testDir = ".";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(testDir, testFileName);
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(testFileContent);
        }
    }

    @AfterEach
    void tearDown() {
        File testFile = new File(testDir, testFileName);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void execute_FileExists() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GetCommand getCommand = new GetCommand(testFileName);

        getCommand.execute(null, out);

        String response = out.toString();
        assertTrue(response.contains("File size: " + testFileContent.length() + " bytes"),
                "Expected file size message.");
        assertTrue(response.contains("225 File Downloaded Successfully."),
                "Expected successful download message.");
        assertTrue(response.contains(testFileContent),
                "Expected file content to be present in the output.");
    }

    @Test
    void execute_FileDoesNotExist() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GetCommand getCommand = new GetCommand("nonExistentFile.txt");

        getCommand.execute(null, out);

        String response = out.toString();
        System.out.println("output: "+ response);
        assertTrue(response.contains("550 File not found."),
                "Expected file not found message.");
    }

    @Test
    void execute_IsDirectory() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GetCommand getCommand = new GetCommand("src");

        getCommand.execute(null, out);

        String response = out.toString();
        assertTrue(response.contains("550 File not found."),
                "Expected file not found message.");
    }
}
