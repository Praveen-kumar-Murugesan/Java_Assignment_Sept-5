//package com.zeetaminds.assgn5sept.ftp;
//
//
//import com.zeetaminds.assgn5sept.net.ftp.main.*;
//import org.junit.jupiter.api.*;
//import org.mockito.ArgumentCaptor;
//
//import java.io.*;
//import java.net.Socket;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class CommandTests {
//
//    private InputStream in;
//    private OutputStream out;
//    private ByteArrayOutputStream outputStream;
//
//    @BeforeEach
//    public void setup() {
//        outputStream = new ByteArrayOutputStream();
//        out = new BufferedOutputStream(outputStream);
//        in = new ByteArrayInputStream(new byte[0]); // Mock InputStream as needed
//    }
//
//    @AfterEach
//    public void tearDown() throws IOException {
//        outputStream.close();
//    }
//
//    @Test
//    public void testListCommand() throws IOException {
//        ListCommand listCommand = new ListCommand();
//        listCommand.execute(in, out, "LIST ");
//
//        String response = outputStream.toString();
//        System.out.println("Output from list command " +response);
//        assertTrue(response.contains("150 Opening data connection for file list."));
//        assertTrue(response.contains("226 Transfer complete."));
//    }
//
//    @Test
//    public void testGetCommandFileExists() throws IOException {
//        // Create a test file
//        File testFile = new File("testFile.txt");
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
//            writer.write("This is a test file.");
//        }
//
//        GetCommand getCommand = new GetCommand();
//        getCommand.execute(in, out, "GET testFile.txt");
//
//        String response = outputStream.toString();
//        assertTrue(response.contains("File size:"));
//        assertTrue(response.contains("225 Transfer complete."));
//
//        // Clean up
//        testFile.delete();
//    }
//
//    @Test
//    public void testGetCommandFileDoesNotExist() throws IOException {
//        GetCommand getCommand = new GetCommand();
//        getCommand.execute(in, out, "GET nonExistingFile.txt");
//
//        String response = outputStream.toString();
//        assertTrue(response.contains("550 File not found."));
//    }
//
//    @Test
//    public void testPutCommand() throws IOException {
//        PutCommand putCommand = new PutCommand();
//
//        // Simulate the input stream for the command
//        byte[] data = "This is a test file content.".getBytes();
//        in = new ByteArrayInputStream(data);
//
//        putCommand.execute(in, out, "PUT testPutFile.txt");
//
//        // Check the response
//        String response = outputStream.toString();
//        assertTrue(response.contains("226 Transfer complete."));
//
//        // Verify the file was created
//        File testFile = new File("testPutFile.txt");
//        assertTrue(testFile.exists());
//
//        // Clean up
//        testFile.delete();
//    }
//
//    @Test
//    public void testPwdCommand() throws IOException {
//        PwdCommand pwdCommand = new PwdCommand();
//        pwdCommand.execute(in, out, "PWD");
//
//        String response = outputStream.toString();
//        assertTrue(response.contains("100"));
//    }
//
//    @Test
//    public void testQuitCommand() throws IOException {
//        Socket mockSocket = mock(Socket.class);
//        QuitCommand quitCommand = new QuitCommand(mockSocket);
//        quitCommand.execute(in, out, "QUIT");
//
//        String response = outputStream.toString();
//        assertTrue(response.contains("221 Goodbye."));
//        verify(mockSocket).close(); // Verify that the socket was closed
//    }
//}
