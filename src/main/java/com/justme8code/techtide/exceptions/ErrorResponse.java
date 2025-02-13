package com.justme8code.techtide.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    String code;
    String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}