package com.gamix.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionMessage {
    // Exceções relacionadas à autenticação e tokens
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED),
    INVALID_JWT_SESSION_WITH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED),
    
    // Exceções relacionadas a usuários e senhas
    USER_NOT_FOUND(HttpStatus.NOT_FOUND), 
    PASSWORDUSER_NOT_FOUND(HttpStatus.NOT_FOUND),
    PASSWORDUSER_ALREADY_EXISTS(HttpStatus.CONFLICT),

    USERNAME_EMPTY(HttpStatus.BAD_REQUEST), 
    USERNAME_NULL(HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT),
    USERNAME_TOO_SHORT(HttpStatus.BAD_REQUEST),
    USERNAME_TOO_LONG(HttpStatus.BAD_REQUEST),
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST),
    USERNAME_CONTAINS_SPECIAL_CHAR(HttpStatus.BAD_REQUEST),
    
    EMAIL_EMPTY(HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    EMAIL_TOO_SHORT(HttpStatus.BAD_REQUEST),
    EMAIL_TOO_LONG(HttpStatus.BAD_REQUEST),
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST),

    PASSWORD_WRONG(HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST),
    PASSWORD_TOO_LONG(HttpStatus.BAD_REQUEST),
    PASSWORD_MISSING_SPECIAL_CHAR(HttpStatus.BAD_REQUEST),
    PASSWORD_MISSING_NUMBER(HttpStatus.BAD_REQUEST),
    PASSWORD_MISSING_UPPERCASE(HttpStatus.BAD_REQUEST),

    ICON_URL_INVALID(HttpStatus.BAD_REQUEST),
    ICON_URL_TOO_LONG(HttpStatus.BAD_REQUEST),

    // Exceções específicas do UserService
    USER_NOT_FOUND_BY_TOKEN(HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND),
    USER_NOT_FOUND_BY_USERNAME(HttpStatus.NOT_FOUND),
    INVALID_ID(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    private ExceptionMessage(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
