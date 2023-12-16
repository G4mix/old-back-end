package com.gamix.config;

import com.gamix.exceptions.ExceptionBase;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GraphQLExceptionHandler implements GraphQLErrorHandler {

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> list) {
        return list.stream().map(this::getNested).collect(Collectors.toList());
    }

    private GraphQLError getNested(GraphQLError error) {
        if (error instanceof ExceptionWhileDataFetching exceptionError) {
            if (exceptionError.getException() instanceof GraphQLError) {
                return (GraphQLError) exceptionError.getException();
            } else if (exceptionError.getException() instanceof ExceptionBase) {
                return (ExceptionBase) exceptionError.getException();
            }
        }
        return error;
    }

}