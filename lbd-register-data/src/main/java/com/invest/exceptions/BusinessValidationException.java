package com.invest.exceptions;

import lombok.Getter;

@Getter
public class BusinessValidationException extends RuntimeException {

    private final String field;

    public BusinessValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
        this.field = null;
    }

    public BusinessValidationException(String message) {
        super(message);
        this.field = null;
    }
}

