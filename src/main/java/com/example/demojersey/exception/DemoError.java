package com.example.demojersey.exception;

public enum DemoError {
    USER_NOT_FOUND(40001,404,"CLIENT"),
    INVALID_USER_NAME(40002,400,"CLIENT"),
    INVALID_FILE(40004,400,"CLIENT"),
    File_NOT_FOUND(40005,404,"CLIENT"),
    FILE_UPLOAD_FAILED(40006,400,"SERVICE"),
    INTERNAL_SERCICE_ERROR(50001,500,"SERVICE");

    int errorId;
    int httpStatus;
    String category;

    DemoError(int errorId, int httpStatus, String category) {
        this.errorId = errorId;
        this.httpStatus = httpStatus;
        this.category = category;
    }
}
