package com.gamix.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionMessage {
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access token is invalid"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh token is invalid"),
    NULL_JWT_TOKENS(HttpStatus.INTERNAL_SERVER_ERROR, "NULL JWT tokens generated"),
    EXCESSIVE_FAILED_LOGIN_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "Excessive failed login attempts"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"), 
    PASSWORDUSER_NOT_FOUND(HttpStatus.NOT_FOUND, "Password user not found"),
    PASSWORDUSER_ALREADY_EXISTS(HttpStatus.CONFLICT, "Password user already exists"),

    USERNAME_EMPTY(HttpStatus.BAD_REQUEST, "Username cannot be empty"), 
    USERNAME_NULL(HttpStatus.BAD_REQUEST, "Username cannot be null"),
    USERNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "Username is too long"),
    USERNAME_TOO_SHORT(HttpStatus.BAD_REQUEST, "Username is too short"),
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Invalid username format"),
    USERNAME_CONTAINS_SPECIAL_CHAR(HttpStatus.BAD_REQUEST, "Username cannot contain special characters"),
    
    EMAIL_NULL(HttpStatus.BAD_REQUEST, "Email cannot be null"),
    EMAIL_EMPTY(HttpStatus.BAD_REQUEST, "Email cannot be empty"),
    EMAIL_TOO_SHORT(HttpStatus.BAD_REQUEST, "Email is too short"),
    EMAIL_TOO_LONG(HttpStatus.BAD_REQUEST, "Email is too long"),
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Invalid email format"),
    EMAIL_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "Email is already verified"),
    
    PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "Wrong password"),
    PASSWORD_NULL(HttpStatus.BAD_REQUEST, "Null password"),
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Invalid password format"),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "Password is too short"),
    PASSWORD_TOO_LONG(HttpStatus.BAD_REQUEST, "Password is too long"),
    PASSWORD_MISSING_SPECIAL_CHAR(HttpStatus.BAD_REQUEST, "Password must contain special character(s)"),
    PASSWORD_MISSING_NUMBER(HttpStatus.BAD_REQUEST, "Password must contain number(s)"),
    PASSWORD_MISSING_UPPERCASE(HttpStatus.BAD_REQUEST, "Password must contain uppercase letter(s)"),
    
    ICON_URL_INVALID(HttpStatus.BAD_REQUEST, "Invalid icon URL"),
    ICON_URL_TOO_LONG(HttpStatus.BAD_REQUEST, "Icon URL is too long"),
    
    USER_ALREADY_EXISTS_WITH_THIS_USERNAME(HttpStatus.CONFLICT, "User with this username already exists"),
    USER_ALREADY_EXISTS_WITH_THIS_EMAIL(HttpStatus.CONFLICT, "User with this email already exists"),
    USER_NOT_FOUND_BY_TOKEN(HttpStatus.NOT_FOUND, "User not found by token"),
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "User not found by email"),
    USER_NOT_FOUND_BY_USERNAME(HttpStatus.NOT_FOUND, "User not found by username"),
    
    INVALID_ID(HttpStatus.BAD_REQUEST, "Invalid ID");

    private final HttpStatus httpStatus;
    private final String message;

    private ExceptionMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
