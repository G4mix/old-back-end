package com.gamix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.gamix.exceptions.BackendException;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;
import com.gamix.service.UserService;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @QueryMapping
    List<User> findAllUsers(
            @Argument("skip") int skip,
            @Argument("limit") int limit
    ) {
        List<User> users = userService.findAllUsers(skip, limit);
        return users;
    }

    @QueryMapping
    User findUserByToken(@Argument String accessToken) {
        User user = userService.findUserByToken(accessToken);
        return user;
    }

    @QueryMapping
    User findUserByUsername(@Argument String username) {
        User user = userService.findUserByUsername(username);
        return user;
    }

    @QueryMapping
    User findUserByEmail(@Argument String email) {
        User user = userService.findUserByEmail(email);
        return user;
    }

    @MutationMapping
    User updateUser(@PathVariable Integer id, PartialUserInput userInput) {
        User updatedUser = userService.updateUser(id, userInput);
        return updatedUser;
    }

    @MutationMapping
    void deleteAccount(@PathVariable Integer id) {
        userService.deleteAccount(id);
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
        if (ex instanceof BackendException) {
            return GraphQLError
                .newError()
                .errorType(ErrorClassification.errorClassification(ex.getMessage()))
                .message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
        } else {
            return GraphQLError
                .newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
        }
    }
}
