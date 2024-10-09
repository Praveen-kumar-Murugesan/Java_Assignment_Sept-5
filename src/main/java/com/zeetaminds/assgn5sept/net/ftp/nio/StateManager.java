package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class StateManager {

    private final ByteBuffer buffer;
    private boolean expectingFileContent = false;
    private String currentPutFilename;
    private BufferedOutputStream currentOutputStream;
    private PutCommand currentPutCommand;

    public StateManager(int size) {
        this.buffer = ByteBuffer.allocate(size);
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public boolean isExpectingFileContent() {
        return expectingFileContent;
    }

    public void setExpectingFileContent(boolean expectingFileContent) {
        this.expectingFileContent = expectingFileContent;
    }

//    public String getCurrentPutFilename() {
//        return currentPutFilename;
//    }
//
//    public void setCurrentPutFilename(String currentPutFilename) {
//        this.currentPutFilename = currentPutFilename;
//    }
//
//    public BufferedOutputStream getCurrentOutputStream() {
//        return currentOutputStream;
//    }
//
//    public void setCurrentOutputStream(BufferedOutputStream currentOutputStream) {
//        this.currentOutputStream = currentOutputStream;
//    }

    public PutCommand getCurrentPutCommand() {
        return currentPutCommand;
    }

    public void setCurrentPutCommand(PutCommand currentPutCommand) {
        this.currentPutCommand = currentPutCommand;
    }

    public void clearBuffer() {
        buffer.clear();
    }

    public void reset() {
        expectingFileContent = false;
        currentPutCommand = null;
        buffer.clear();
    }
}