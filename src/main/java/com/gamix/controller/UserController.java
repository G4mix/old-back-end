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
        System.out.println(userDetails);
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
    User findUserByUsername(@Argument String username) {
        User user = userService.findUserByUsername(username);
        return user;
    }

    @PreAuthorize("hasAuthority('USER')")  
    @QueryMapping
    User findUserByEmail(@Argument String email) {
        User user = userService.findUserByEmail(email);
        return user;
    }

    @PreAuthorize("hasAuthority('USER')")  
    @MutationMapping
    User updateUser(@Argument("id") Integer id, @Argument("userInput") PartialUserInput userInput) {
        User updatedUser = userService.updateUser(id, userInput);
        return updatedUser;
    }

    @PreAuthorize("hasAuthority('USER')")  
    @MutationMapping
    boolean deleteAccount(@Argument("id") Integer id) {
        try {
            userService.deleteAccount(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
        return GraphQLError
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(ex.getMessage())
            .path(environment.getExecutionStepInfo().getPath())
            .location(environment.getField().getSourceLocation())
            .build();
    }
}
