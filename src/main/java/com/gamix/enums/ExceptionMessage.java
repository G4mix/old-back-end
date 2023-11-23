package com.gamix.enums;

import org.springframework.http.HttpStatus;

public enum ExceptionMessage {
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Token de acesso inválido."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Token de atualização inválido."),
    NULL_JWT_TOKENS(HttpStatus.INTERNAL_SERVER_ERROR, "Tokens JWT nulos gerados."),
    TOKEN_CLAIMS_EXCEPTION(HttpStatus.UNAUTHORIZED, "Erro ao obter reivindicações do token."),
    TOKEN_DO_NOT_MATCH_EXCEPTION(HttpStatus.UNAUTHORIZED, "Tokens não correspondem, envie os mesmos tokens."),
    EXCESSIVE_FAILED_LOGIN_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "Tentativas de login falhadas excessivamente."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Usuário não encontrado."),
    PASSWORDUSER_NOT_FOUND(HttpStatus.NOT_FOUND, "Usuário de senha não encontrado."),
    PASSWORDUSER_ALREADY_EXISTS(HttpStatus.CONFLICT, "Usuário de senha já existe."),

    USERNAME_EMPTY(HttpStatus.BAD_REQUEST, "Nome de usuário não pode ser vazio."),
    USERNAME_NULL(HttpStatus.BAD_REQUEST, "Nome de usuário não pode ser nulo."),
    USERNAME_TOO_LONG(HttpStatus.BAD_REQUEST, "Nome de usuário muito longo (comprimento máximo e 50 caracteres)."),
    USERNAME_TOO_SHORT(HttpStatus.BAD_REQUEST, "Nome de usuário muito curto (comprimento máximo e 3 caracteres)."),
    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, 
            "Nome de usuário tem um formato inválido. Deve conter apenas caracteres alfanuméricos."),

    EMAIL_EMPTY(HttpStatus.BAD_REQUEST, "Email não pode ser vazio."),
    EMAIL_NULL(HttpStatus.BAD_REQUEST, "Email não pode ser nulo."),
    EMAIL_TOO_SHORT(HttpStatus.BAD_REQUEST, "Email muito curto."),
    EMAIL_TOO_LONG(HttpStatus.BAD_REQUEST, "Email muito longo (comprimento máximo e 320 caracteres)."),
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST,
            "Email tem um formato inválido. Deve estar no formato 'nome@gmail.com'."),
    EMAIL_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "Email já está verificado."),

    PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "Senha incorreta."),
    PASSWORD_NULL(HttpStatus.BAD_REQUEST, "Senha nula."),
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Formato de senha inválido"),
    PASSWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "Senha muito curta (comprimento mínimo e 8 caracteres)."),
    PASSWORD_TOO_LONG(HttpStatus.BAD_REQUEST, "Senha muito longa (comprimento máximo e 128 caracteres)."),
    PASSWORD_MISSING_SPECIAL_CHAR(HttpStatus.BAD_REQUEST, "Senha deve conter caractere(s) especial(is)."),
    PASSWORD_MISSING_NUMBER(HttpStatus.BAD_REQUEST, "Senha deve conter número(s)."),
    PASSWORD_MISSING_UPPERCASE(HttpStatus.BAD_REQUEST, "Senha deve conter letra(s) maiúscula(s)."),

    ICON_NULL(HttpStatus.BAD_REQUEST, "URL do ícone nula."),

    USER_ALREADY_EXISTS_WITH_THIS_USERNAME(HttpStatus.CONFLICT, "User with this username already exists"),
    USER_ALREADY_EXISTS_WITH_THIS_EMAIL(HttpStatus.CONFLICT, "User with this email already exists"),
    USER_NOT_FOUND_BY_TOKEN(HttpStatus.NOT_FOUND, "User not found by token"),
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "User not found by email"),
    USER_NOT_FOUND_BY_USERNAME(HttpStatus.NOT_FOUND, "User not found by username"),
    USER_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "User not found by ID"),

    USER_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "User profile not found by ID"),

    POST_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "Post not found by"),
    POST_NOT_FOUND_BY_TITLE(HttpStatus.NOT_FOUND, "Post not found by title"),
    COMPLETELY_EMPTY_POST(HttpStatus.BAD_REQUEST, "Você não pode enviar um post totalmente vazio"),

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
