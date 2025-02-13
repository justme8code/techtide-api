package com.justme8code.techtide.exceptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class JWTException extends RuntimeException {
    private final HttpStatus  errorCode;
    private String message;
    private Exception originalException;

    public JWTException(HttpStatus errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public JWTException(HttpStatus errorCode, String message, Exception exception) {
        this.errorCode = errorCode;
        this.message = message;
        this.originalException = exception;
    }
}
