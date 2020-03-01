package com.example.demojersey.exception;

public class SimpleException extends RuntimeException {
    private String cause;
    private int returnCode;
    private SimpleExceptionEntity entity;

    public SimpleException(String message, DemoError demoError) {
        this(message, demoError, null);
    }

    public SimpleException(String message, DemoError demoError, String cause) {
        super(message);
        this.cause = cause;
        this.returnCode = demoError.httpStatus;
        this.entity = new SimpleExceptionEntity(message, demoError.errorId, demoError.name(), demoError.category,cause);
    }

    public int getReturnCode() {

        return returnCode;
    }

    public SimpleExceptionEntity getEntity() {

        return entity;
    }
}
