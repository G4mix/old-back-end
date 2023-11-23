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

    USER_ALREADY_EXISTS_WITH_THIS_USERNAME(HttpStatus.CONFLICT, "Usuário com esse nome de usuário já existe."),
    USER_ALREADY_EXISTS_WITH_THIS_EMAIL(HttpStatus.CONFLICT, "Usuário com esse email já existe."), 
    USER_NOT_FOUND_BY_TOKEN(HttpStatus.NOT_FOUND, "Usuário não encontrado por token."),
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "Usuário não encontrado por email."), 
    USER_NOT_FOUND_BY_USERNAME(HttpStatus.NOT_FOUND, "Usuário não encontrado por nome de usuário."), 
    USER_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "Usuário não encontrado por ID."), 

    USER_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Perfil de usuário não encontrado por ID."), 

    POST_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND, "Post não encontrado por ID."), 
    POST_NOT_FOUND_BY_TITLE(HttpStatus.NOT_FOUND, "Post não encontrado por Titulo."),
    COMPLETELY_EMPTY_POST(HttpStatus.BAD_REQUEST, "Você não pode enviar um post totalmente vazio."),
    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "Titulo muito longo (comprimento máximo e 70 caracteres)."),
    CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "Conteúdo muito longo (comprimento máximo e 700 caracteres)."),
    TOO_MANY_LINKS(HttpStatus.BAD_REQUEST, "Muitos links no post (máximo 5)."),
    TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST, "Muitas imagens no post (máximo 8)."),

    INVALID_ID(HttpStatus.BAD_REQUEST, "ID Inválido.");

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
