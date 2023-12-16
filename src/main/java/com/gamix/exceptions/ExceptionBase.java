package com.gamix.exceptions;

import com.gamix.enums.ExceptionMessage;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Getter
public class ExceptionBase extends RuntimeException implements GraphQLError {
    private final HttpStatusCode status;
    private final String error, message;

    public ExceptionBase(ExceptionMessage e) {
        this.status = e.getHttpStatus();
        this.error = e.toString();
        this.message = e.getMessage();
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
