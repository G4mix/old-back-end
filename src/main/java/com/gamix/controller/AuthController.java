package com.gamix.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gamix.controller.UserController.LoginWithPasswordUserInput;
import com.gamix.controller.UserController.UserInput;
import com.gamix.models.PasswordUser;
import com.gamix.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
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

    @PostMapping("/loginWithUsername")
    public void loginWithUsername(
        @RequestBody LoginWithPasswordUserInput  userInput,
        HttpServletResponse response
    ) throws IOException {
        String jwtToken = userService.loginWithUsername(userInput.username(), userInput.password());
        if (jwtToken != null) {
            response.setHeader("Authorization", "Bearer " + jwtToken);
            response.sendRedirect("/feed");
        } else {
            response.sendRedirect("/");
        }
    }

    @PostMapping("/signoutPasswordUser")
    public ResponseEntity<String> signoutPasswordUser(@RequestBody UserInput userInput) {

        // Remover do banco de dados
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Hello World! Você não está logado!");
    }

    @GetMapping("/feed")
    public ResponseEntity<String> feed() {
        return ResponseEntity.ok("Hello World! Você está logado!");
    }
}