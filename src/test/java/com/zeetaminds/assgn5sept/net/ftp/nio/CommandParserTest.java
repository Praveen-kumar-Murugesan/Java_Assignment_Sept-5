package com.zeetaminds.assgn5sept.net.ftp.nio;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.ByteBuffer;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommandParserTest {

    private CommandParser commandParser;
    private BufferManager mockBufferManager;

    @BeforeEach
    void setUp() {
        commandParser = CommandParser.getInstance();
        mockBufferManager = mock(BufferManager.class);
    }

    @Test
    void parseCommand_ValidListCommand() throws InvalidCommandException {
        String input = "LIST\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        Command command = commandParser.parseCommand(mockBufferManager);

        assertInstanceOf(ListCommand.class, command, "Expected ListCommand instance");
    }

    @Test
    void parseCommand_ValidGetCommand() throws InvalidCommandException {
        String input = "GET testFile.txt\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        Command command = commandParser.parseCommand(mockBufferManager);

        assertInstanceOf(GetCommand.class, command, "Expected GetCommand instance");
    }

    @Test
    void parseCommand_ValidPutCommand() throws InvalidCommandException {
        String input = "PUT testFile.txt\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        Command command = commandParser.parseCommand(mockBufferManager);

        assertInstanceOf(PutCommand.class, command, "Expected PutCommand instance");
    }

    @Test
    void parseCommand_ValidPwdCommand() throws InvalidCommandException {
        String input = "PWD\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        Command command = commandParser.parseCommand(mockBufferManager);

        assertInstanceOf(PwdCommand.class, command, "Expected PwdCommand instance");
    }

    @Test
    void parseCommand_ValidQuitCommand() throws InvalidCommandException {
        String input = "QUIT\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        Command command = commandParser.parseCommand(mockBufferManager);

        assertInstanceOf(QuitCommand.class, command, "Expected QuitCommand instance");
    }

    @Test
    void parseCommand_InvalidCommand() {
        String input = "INVALID_COMMAND\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));
    }

    @Test
    void parseCommand_GetCommandMissingFilename() {
        String input = "GET \n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));
    }

    @Test
    void parseCommand_PutCommandMissingFilename() {
        String input = "PUT \n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));
    }

    @Test
    void parsePipedCommands_MultipleValidCommands() throws InvalidCommandException {
        String input = "LIST\nPUT file1.txt\nGET file2.txt\nPWD\nQUIT\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        assertInstanceOf(ListCommand.class, commandParser.parseCommand(mockBufferManager));
        assertInstanceOf(PutCommand.class, commandParser.parseCommand(mockBufferManager));
        assertInstanceOf(GetCommand.class, commandParser.parseCommand(mockBufferManager));
        assertInstanceOf(PwdCommand.class, commandParser.parseCommand(mockBufferManager));
        assertInstanceOf(QuitCommand.class, commandParser.parseCommand(mockBufferManager));
    }

    @Test
    void parsePipedCommands_ValidAndInvalidCommands() throws InvalidCommandException {
        String input = "LIST\nPUT file.txt\nINVALID_CMD\nGET file.txt\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        assertInstanceOf(ListCommand.class, commandParser.parseCommand(mockBufferManager));
        assertInstanceOf(PutCommand.class, commandParser.parseCommand(mockBufferManager));

        // Expect the invalid command to throw an exception
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));

        // After handling the exception, the parser should still be able to handle subsequent valid commands
        assertInstanceOf(GetCommand.class, commandParser.parseCommand(mockBufferManager));
    }

    @Test
    void parsePipedCommands_OnlyInvalidCommands() {
        String input = "INVALID_CMD\nINVALID_CMD_2\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        // The first invalid command should throw an exception
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));
    }

    @Test
    void parsePipedCommands_OnlyEmptyAndInvalidCommands() {
        String input = "INVALID_CMD\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        // Then the invalid command should also throw an exception
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));
    }

    @Test
    void parsePipedCommands_MixedValidInvalidAndEmptyCommands() throws InvalidCommandException {
        String input = "LIST\nINVALID_CMD\nPUT file.txt\nputt file\nGET file.txt\n";
        ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
        when(mockBufferManager.getBuffer()).thenReturn(buffer);

        assertInstanceOf(ListCommand.class, commandParser.parseCommand(mockBufferManager));

        // The invalid command should throw an exception
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));

        assertInstanceOf(PutCommand.class, commandParser.parseCommand(mockBufferManager));

        // The final empty command should throw an exception
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(mockBufferManager));

        assertInstanceOf(GetCommand.class, commandParser.parseCommand(mockBufferManager));
    }
}
