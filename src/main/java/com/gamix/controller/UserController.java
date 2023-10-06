package com.gamix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;
import com.gamix.security.JwtUserDetails;
import com.gamix.service.UserService;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('USER')")  
    @QueryMapping
    List<User> findAllUsers(
            @Argument("skip") int skip,
            @Argument("limit") int limit
    ) {
        List<User> users = userService.findAllUsers(skip, limit);
        return users;
    }

    @PreAuthorize("hasAuthority('USER')")  
    @QueryMapping
    Object findUserByToken(@AuthenticationPrincipal JwtUserDetails userDetails) throws Exception {
        try {
            String accessToken = userDetails.getAccessToken();
            User user = userService.findUserByToken(accessToken);
            return user;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")  
    @QueryMapping
    User findUserByUsername(@Argument String username) throws ExceptionBase {
        try {
            User user = userService.findUserByUsername(username);
            return user;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")  
    @QueryMapping
    User findUserByEmail(@Argument String email) throws ExceptionBase {
        try {
            User user = userService.findUserByEmail(email);
            return user;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")  
    @MutationMapping
    User updateUser(
        @AuthenticationPrincipal JwtUserDetails userDetails,
        @Argument("input") PartialUserInput userInput
    ) throws ExceptionBase {
        try {    
            String accessToken = userDetails.getAccessToken();
            User updatedUser = userService.updateUser(accessToken, userInput);
            return updatedUser;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")  
    @MutationMapping
    boolean deleteAccount(@AuthenticationPrincipal JwtUserDetails userDetails) throws ExceptionBase {
        try {
            String accessToken = userDetails.getAccessToken();
            return userService.deleteAccount(accessToken);
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
        if (ex instanceof ExceptionBase) {
            return GraphQLError
                .newError()
                .errorType(ErrorClassification.errorClassification(((ExceptionBase) ex).getStatus().toString()))
                .message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
        }
        return GraphQLError
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(ex.getMessage())
            .path(environment.getExecutionStepInfo().getPath())
            .location(environment.getField().getSourceLocation())
            .build();
    }
}
