//package com.zeetaminds.assgn5sept.ftp;
//
//import com.zeetaminds.assgn5sept.net.ftp.main.*;
//
//import org.junit.jupiter.api.*;
//import org.mockito.ArgumentCaptor;
//
//import java.io.*;
//import java.net.Socket;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FTPServerTest {
//    private Socket mockSocket;
//    private InputStream is;
//    private OutputStream os;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        mockSocket = mock(Socket.class);
//        is = mock(InputStream.class);
//        os = mock(OutputStream.class);
//        when(mockSocket.getInputStream()).thenReturn(is);
//        when(mockSocket.getOutputStream()).thenReturn(os);
//        ClientHandler clientHandler = new ClientHandler(mockSocket);
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        mockSocket.close();
//    }
//
//    @Test
//    void testListCommand() throws IOException {
//        String command = "LIST";
//        Command listCommand = new ListCommand();
//        listCommand.execute(is, os, command);
//
//        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
//        verify(os, atLeastOnce()).write(captor.capture());
//
//        StringBuilder outputBuilder = new StringBuilder();
//        for (byte[] bytes : captor.getAllValues()) {
//            outputBuilder.append(new String(bytes));
//        }
//
//        String output = outputBuilder.toString();
//
//        assertTrue(output.contains("150 Opening data connection for file list."));
//        assertTrue(output.contains("226 Transfer complete."));
//    }
//
//
//    @Test
//    void testGetCommand_FileExists() throws IOException {
//        File testFile = File.createTempFile("test", ".txt");
//        testFile.deleteOnExit();
//
//        String command = "GET " + testFile.getAbsolutePath();
//        Command getCommand = new GetCommand();
//        getCommand.execute(is, os, command);
//
//        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
//        verify(os, atLeastOnce()).write(captor.capture());
//
//        StringBuilder outputBuilder = new StringBuilder();
//        for (byte[] bytes : captor.getAllValues()) {
//            outputBuilder.append(new String(bytes));
//        }
//
//        String output = outputBuilder.toString();
//        assertTrue(output.contains("File size:"));
//        assertTrue(output.contains("225 Transfer complete."));
//    }
//
//    @Test
//    void testPutCommand() throws IOException {
//        String command = "PUT testFile.txt";
//        Command putCommand = new PutCommand();
//
//        byte[] inputData = "Hello, FTP!".getBytes();
//        InputStream inputStream = new ByteArrayInputStream(inputData);
//        when(is.read(any(byte[].class))).thenAnswer(invocation -> {
//            byte[] buffer = invocation.getArgument(0);
//            System.arraycopy(inputData, 0, buffer, 0, inputData.length);
//            return inputData.length; // Indicate that all data has been read
//        });
//
//        putCommand.execute(inputStream, os, command);
//
//        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
//        verify(os, atLeastOnce()).write(captor.capture());
//
//        String output = new String(captor.getValue());
//        assertTrue(output.contains("226 Transfer complete."));
//    }
//
//    @Test
//    void testPwdCommand() throws IOException {
//        String command = "PWD";
//        Command pwdCommand = new PwdCommand();
//        pwdCommand.execute(is, os, command);
//
//        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
//        verify(os, atLeastOnce()).write(captor.capture());
//
//        String output = new String(captor.getValue());
//        assertTrue(output.contains("100 \""));
//    }
//
//    @Test
//    void testQuitCommand() throws IOException {
//        String command = "QUIT";
//        Command quitCommand = new QuitCommand(mockSocket);
//        quitCommand.execute(is, os, command);
//
//        verify(mockSocket, times(1)).close();
//
//        ArgumentCaptor<byte[]> captor = ArgumentCaptor.forClass(byte[].class);
//        verify(os, atLeastOnce()).write(captor.capture());
//
//        String output = new String(captor.getValue());
//        assertTrue(output.contains("221 Goodbye."));
//    }
//}
