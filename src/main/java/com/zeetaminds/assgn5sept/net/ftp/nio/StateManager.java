package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.nio.ByteBuffer;

public class StateManager {

    private final ByteBuffer buffer;
    private PutCommand currentPutCommand;

    public StateManager(int size) {
        this.buffer = ByteBuffer.allocate(size);
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

    public void reset() {
        setCurrentPutCommand(null);
    }
}