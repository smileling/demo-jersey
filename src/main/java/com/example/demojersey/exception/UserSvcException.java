package com.example.demojersey.exception;

public class UserSvcException extends RuntimeException {
    private String cause;
    private int returnCode;
    private UserSvcExceptionEntity entity;

    public UserSvcException(String message, UserSvcError userSvcError) {
        this(message, userSvcError, null);
    }

    public UserSvcException(String message, UserSvcError userSvcError, String cause) {
        super(message);
        this.cause = cause;
        this.returnCode = userSvcError.httpStatus;
        this.entity = new UserSvcExceptionEntity(message, userSvcError.errorId, userSvcError.name(), userSvcError.category,cause);
    }

    public int getReturnCode() {

        return returnCode;
    }

    public UserSvcExceptionEntity getEntity() {

        return entity;
    }
}
