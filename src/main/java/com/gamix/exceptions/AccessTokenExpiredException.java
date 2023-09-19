package com.gamix.exceptions;

public class AccessTokenExpiredException extends RuntimeException {
    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
