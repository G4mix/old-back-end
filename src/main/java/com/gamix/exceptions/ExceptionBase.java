package com.gamix.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import com.gamix.enums.ExceptionMessage;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

public class ExceptionBase extends Exception implements GraphQLError {
    private final HttpStatus status;
    private final String error, message;

    public ExceptionBase(ExceptionMessage e) {
        this.error = e.toString();
        this.status = e.getHttpStatus();
        this.message = e.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public HttpStatusCode getStatus() {
        return status;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return ErrorClassification.errorClassification(error);
    }
}
