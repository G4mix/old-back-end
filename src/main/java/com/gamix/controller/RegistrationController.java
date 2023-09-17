package com.gamix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gamix.controller.UserController.UserInput;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.service.UserService;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;

    @PostMapping("/registerPasswordUser")
    public ResponseEntity<String> registerPasswordUser(@RequestBody UserInput userInput) {
        PasswordUser passwordUser = userService.registerPasswordUser(userInput);
        System.out.println("unable to register password user   "+passwordUser + "   unable to register password user");
        if (passwordUser == null) {
            return ResponseEntity.badRequest().body("unable to register password user.");
        }
        System.out.println("password user registered   "+passwordUser.getToken() + "   password user registered");
        return ResponseEntity.ok(passwordUser.getToken());
    }

    @PostMapping("/newUser")
    public User newUser(@RequestBody UserInput userInput) {
        return userService.createUser(userInput);
    }
}

