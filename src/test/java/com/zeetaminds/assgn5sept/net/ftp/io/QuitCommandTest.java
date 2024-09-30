package com.zeetaminds.assgn5sept.net.ftp.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class QuitCommandTest {

    @Test
    void execute_ShouldSendGoodbyeMessageAndCloseSocket() throws IOException {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        QuitCommand quitCommand = new QuitCommand(mockSocket);

        when(mockSocket.isClosed()).thenReturn(false);

        quitCommand.execute(null, out);

        assertEquals("221 Goodbye.", out.toString().trim(), "The QUIT command should send '221 Goodbye.' message.");

        verify(mockSocket, times(1)).close();
    }

    @Test
    void execute_ShouldNotCloseSocketIfAlreadyClosed() throws IOException {
        Socket mockSocket = mock(Socket.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        QuitCommand quitCommand = new QuitCommand(mockSocket);

        when(mockSocket.isClosed()).thenReturn(true);

        quitCommand.execute(null, out);

        assertEquals("221 Goodbye.", out.toString().trim(), "The QUIT command should send '221 Goodbye.' message.");

        verify(mockSocket, never()).close();
    }
}
