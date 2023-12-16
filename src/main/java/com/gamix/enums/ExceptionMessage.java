package com.gamix.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
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

    USERNAME_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Nome de usuário inválido."),
    EMAIL_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Email inválido."),
    PASSWORD_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "Senha inválida."),
    PASSWORD_WRONG(HttpStatus.BAD_REQUEST, "Você errou a senha."),

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
    TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "Titulo muito longo (comprimento máximo é 70 caracteres)."),
    CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "Conteúdo muito longo (comprimento máximo é 700 caracteres)."),
    TOO_MANY_LINKS(HttpStatus.BAD_REQUEST, "Muitos links no post (máximo 5)."),
    TOO_MANY_IMAGES(HttpStatus.BAD_REQUEST, "Muitas imagens no post (máximo 8)."),

    ERROR_CREATING_IMAGE(HttpStatus.BAD_REQUEST, "Erro ao criar a imagem."),
    ERROR_UPDATING_IMAGE(HttpStatus.BAD_REQUEST, "Erro ao atualizar as imagens."),
    HANDLE_IMAGE_ERROR(HttpStatus.BAD_REQUEST, "Dados da imagem inválidos."),
    MAX_ALLOWABLE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "Tamanho máximo de imagem excedido."),
    COMMENT_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Comentário não encontrado por ID."),
    COMMENT_EMPTY(HttpStatus.BAD_REQUEST, "Comentário não pode ser vazio."),
    TOO_LONG_COMMENT(HttpStatus.BAD_REQUEST, "Comentário muito longo (comprimento máximo é 200 caracteres)."),

    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Erro desconhecido.");
    private final HttpStatusCode httpStatus;
    private final String message;

    ExceptionMessage(HttpStatusCode httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
