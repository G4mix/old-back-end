package com.gamix.controller;

import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.gamix.models.User;
import com.gamix.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    ResponseEntity<String> createUser(UserInput userInput) {
        userService.createUser(userInput);
        return ResponseEntity.ok("usuário criado");
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

    public record UserInput(String username, String email, String password, String icon) {}
    public record PartialUserInput(String username, String icon) {
        public PartialUserInput(UserInput userInput) {
            this(userInput.username(), userInput.icon());
        }
    }
}
