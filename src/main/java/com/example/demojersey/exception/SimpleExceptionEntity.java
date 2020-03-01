package com.example.demojersey.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleExceptionEntity {
    @JsonProperty
    protected  String message;

    @JsonProperty
    protected int errorId;

    @JsonProperty
    protected String errorCode;

    @JsonProperty
    protected String category;

    @JsonProperty
    protected String cause;

    public SimpleExceptionEntity(String message, int errorId, String errorCode, String category, String cause) {
        this.message = message;
        this.errorId = errorId;
        this.errorCode = errorCode;
        this.category = category;
        this.cause = cause;
    }
}