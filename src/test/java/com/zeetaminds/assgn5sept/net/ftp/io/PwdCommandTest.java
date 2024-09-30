package com.zeetaminds.assgn5sept.net.ftp.io;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PwdCommandTest {

    @Test
    void execute_ShouldReturnCurrentDirectory() throws IOException {
        PwdCommand pwdCommand = new PwdCommand();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        pwdCommand.execute(null, out);

        String currentDir = new File(".").getAbsolutePath();
        String expectedResponse = "100 \"" + currentDir + "\" is the current directory.";

        assertEquals(expectedResponse, out.toString().trim(), "The PWD command should return the correct current directory.");
    }
}
