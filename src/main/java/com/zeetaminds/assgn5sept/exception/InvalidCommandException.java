package com.zeetaminds.assgn5sept.exception;

public class InvalidCommandException extends Exception {
    private static final String MSG = "Invalid Command";
    public InvalidCommandException() {
        super(MSG);
    }

    public InvalidCommandException(String message) {
        super(MSG + ": " + message );
    }

}
