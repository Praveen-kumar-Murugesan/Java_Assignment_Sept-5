package com.zeetaminds.assgn5sept.net.ftp.io;

import com.zeetaminds.assgn5sept.exception.InvalidCommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandParserTest {

    CommandParser mockParser = mock(CommandParser.class);
    private CommandParser commandParser;
    private Socket mockSocket;

    @BeforeEach
    void setUp() {
        commandParser = CommandParser.getInstance();
        mockSocket = mock(Socket.class);
    }

    @Test
    void parseCommand_ValidListCommand() throws IOException, InvalidCommandException {
        String input = "LIST\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        Command command = commandParser.parseCommand(bin, mockSocket);

        assertInstanceOf(ListCommand.class, command, "Expected ListCommand instance");
    }

    @Test
    void parseCommand_ValidGetCommand() throws IOException, InvalidCommandException {
        String input = "GET testFile.txt\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        Command command = commandParser.parseCommand(bin, mockSocket);

        assertInstanceOf(GetCommand.class, command, "Expected GetCommand instance");
    }

    @Test
    void parseCommand_ValidPutCommand() throws IOException, InvalidCommandException {
        String input = "PUT testFile.txt\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        Command command = commandParser.parseCommand(bin, mockSocket);

        assertInstanceOf(PutCommand.class, command, "Expected PutCommand instance");
    }

    @Test
    void parseCommand_ValidPwdCommand() throws IOException, InvalidCommandException {
        String input = "PWD\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        Command command = commandParser.parseCommand(bin, mockSocket);

        assertInstanceOf(PwdCommand.class, command, "Expected PwdCommand instance");
    }

    @Test
    void parseCommand_ValidQuitCommand() throws IOException, InvalidCommandException {
        String input = "QUIT\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        Command command = commandParser.parseCommand(bin, mockSocket);

        assertInstanceOf(QuitCommand.class, command, "Expected QuitCommand instance");
    }

    @Test
    void parseCommand_InvalidCommand() {
        String input = "INVALID_COMMAND\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });
    }

    @Test
    void parseCommand_EmptyCommand() {
        String input = "\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });
    }

    @Test
    void parseCommand_GetCommandMissingFilename() {
        String input = "GET \n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });
    }

    @Test
    void parseCommand_PutCommandMissingFilename() {
        String input = "PUT \n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });
    }

    @Test
    void parsePipedCommands_ValidCommands() throws IOException, InvalidCommandException {
        String input = "LIST\nPUT testFile.txt\nGET test.txt\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertInstanceOf(ListCommand.class, commandParser.parseCommand(bin, mockSocket));
        assertInstanceOf(PutCommand.class, commandParser.parseCommand(bin, mockSocket));
        assertInstanceOf(GetCommand.class, commandParser.parseCommand(bin, mockSocket));
    }

    @Test
    void parsePipedCommands_InvalidCommands() throws InvalidCommandException, IOException {
        String input = "LIST\nINVALID_COMMAND\nGET test.txt\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertInstanceOf(ListCommand.class, commandParser.parseCommand(bin, mockSocket));
        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });

        assertInstanceOf(GetCommand.class, commandParser.parseCommand(bin, mockSocket));
    }

    @Test
    void parsePipedCommands_MixedValidAndInvalidCommands() throws InvalidCommandException, IOException {
        String input = "LIST\nPUT test.txt\nkhbdsknjndcvl ldc\nGET test.txt\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertInstanceOf(ListCommand.class, commandParser.parseCommand(bin, mockSocket));
        assertInstanceOf(PutCommand.class, commandParser.parseCommand(bin, mockSocket));
        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });

        assertInstanceOf(GetCommand.class, commandParser.parseCommand(bin, mockSocket));
    }

    @Test
    void parsePipedCommands_EmptyCommands() {
        String input = "\n\n\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });
    }

    @Test
    void parsePipedCommands_MultipleEmptyCommands() throws InvalidCommandException, IOException {
        String input = "LIST\n\n\nPUT test.txt\n\nGET test.txt\n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertInstanceOf(ListCommand.class, commandParser.parseCommand(bin, mockSocket));
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(bin, mockSocket));
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(bin, mockSocket));
        assertInstanceOf(PutCommand.class, commandParser.parseCommand(bin, mockSocket));
        assertThrows(InvalidCommandException.class, () -> commandParser.parseCommand(bin, mockSocket));
        assertInstanceOf(GetCommand.class, commandParser.parseCommand(bin, mockSocket));
    }

    @Test
    void parsePipedCommands_GetCommandMissingFilename() {
        String input = "GET \n";

        BufferedInputStream bin = new BufferedInputStream(new ByteArrayInputStream(input.getBytes()));
        bin.mark(1024);

        assertThrows(InvalidCommandException.class, () -> {
            commandParser.parseCommand(bin, mockSocket);
        });
    }
}
