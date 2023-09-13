package com.gamix.controller;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;


@Controller
public class UserController {
    UserService userService;
    UserRepository userRepository;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    List<User> findAllUsers() {
        List<User> users = userService.findAllUsers();
        return users;
    }

    @QueryMapping
    User findUserByEmail(@Argument String email) {
        User user = userService.findUserByEmail(email);

        return user;
    }

    @MutationMapping
    User updateUser(@Arguments Integer id, UserInput userInput) {
        User updatedUser = userService.updateUser(id,userInput);
        return updatedUser;
    }

    @MutationMapping
    void deleteAccount(@Argument Integer id) {
        userService.deleteAccount(id);
    }

    public record UserInput(String username, String icon) {}
}
