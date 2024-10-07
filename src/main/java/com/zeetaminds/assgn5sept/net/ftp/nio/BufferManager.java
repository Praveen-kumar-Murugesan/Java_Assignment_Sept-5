package com.zeetaminds.assgn5sept.net.ftp.nio;

import java.nio.ByteBuffer;

public class BufferManager {

    private final ByteBuffer buffer;
    private boolean expectingFileContent = false;
    private String currentPutFilename;

    public BufferManager(int size) {
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

    public String getCurrentPutFilename() {
        return currentPutFilename;
    }

    public void setCurrentPutFilename(String currentPutFilename) {
        this.currentPutFilename = currentPutFilename;
    }

    public void clearBuffer() {
        buffer.clear();
    }
}
