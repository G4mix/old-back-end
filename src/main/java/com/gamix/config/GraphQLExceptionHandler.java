package com.gamix.config;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.gamix.exceptions.ExceptionBase;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.kickstart.execution.error.GraphQLErrorHandler;

@Component
public class GraphQLExceptionHandler implements GraphQLErrorHandler {

    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> list) {
        return list.stream().map(this::getNested).collect(Collectors.toList());
    }

    private GraphQLError getNested(GraphQLError error) {
        if (error instanceof ExceptionWhileDataFetching) {
            ExceptionWhileDataFetching exceptionError = (ExceptionWhileDataFetching) error;
            if (exceptionError.getException() instanceof GraphQLError) {
                return (GraphQLError) exceptionError.getException();
            } else if (exceptionError.getException() instanceof ExceptionBase) {
                return (ExceptionBase) exceptionError.getException();
            }
        }
        return error;
    }

}