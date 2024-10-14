package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.nio.ByteBuffer;

public class StateManager {

    private final ByteBuffer buffer;
    private PutCommand currentPutCommand;
    private final StringBuilder commandBuilder;

    public StateManager() {
        this.buffer = ByteBuffer.allocate(1024);
        this.commandBuilder = new StringBuilder();
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public PutCommand getCurrentPutCommand() {
        return currentPutCommand;
    }

    public void setCurrentPutCommand(PutCommand currentPutCommand) {
        this.currentPutCommand = currentPutCommand;
    }

    public StringBuilder getCommandBuilder() {
        return commandBuilder;
    }

    public void resetCommandBuilder() {
        commandBuilder.setLength(0); // Clear the commandBuilder for reuse
    }

    public void clear() {
        setCurrentPutCommand(null);
    }
}