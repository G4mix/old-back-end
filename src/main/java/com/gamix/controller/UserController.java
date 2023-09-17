package com.gamix.controller;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @MutationMapping
    public ResponseEntity<String> registerPasswordUser(UserInput userInput) {
        PasswordUser passwordUser = userService.registerPasswordUser(userInput);
        if (passwordUser == null) {
            return ResponseEntity.badRequest().body("Unable to register password user.");
        }
        return ResponseEntity.ok("created user");
    }


    @QueryMapping
    List<User> findAllUsers(
            @RequestParam(name = "skip", defaultValue = "0") int skip,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        List<User> users = userService.findAllUsers(skip, limit);
        return users;
    }

    @QueryMapping
    User findUserByEmail(@Argument String email) {
        User user = userService.findUserByEmail(email);
        return user;
    }

    @MutationMapping
    User updateUser(@Arguments Integer id, PartialUserInput userInput) {
        User updatedUser = userService.updateUser(id, userInput);
        return updatedUser;
    }

    @MutationMapping
    void deleteAccount(@Argument Integer id) {
        userService.deleteAccount(id);
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment){
        return GraphQLError
                .newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
    }

    public record UserInput(String username, String email, String password, String icon) {}
    public record PartialUserInput(String username, String icon) {
        public PartialUserInput(UserInput userInput) {
            this(userInput.username(), userInput.icon());
        }
    }
}
