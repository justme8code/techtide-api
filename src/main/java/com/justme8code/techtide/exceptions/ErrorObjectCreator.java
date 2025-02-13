package com.justme8code.techtide.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorObjectCreator {

    private ErrorObjectCreator() {}

    public static ErrorObject createErrorObject(HttpStatus status, String message) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(status.value());
        errorObject.setMessage(message);
        errorObject.setTimestamp(new Date());
        return errorObject;
    }
}
