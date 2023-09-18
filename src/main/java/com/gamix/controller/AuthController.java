package com.gamix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamix.models.PasswordUser;
import com.gamix.records.UserRecords.UserInput;
import com.gamix.records.UserRecords.UserPasswordInput;
import com.gamix.records.UserRecords.UserSession;
import com.gamix.service.AuthService;

import io.github.cdimascio.dotenv.Dotenv;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private Dotenv dotenv;

    @PostMapping("/auth/signup")
    public ResponseEntity<String> signUpPasswordUser(@RequestBody UserInput userInput) {
        PasswordUser passwordUser = authService.signUpPasswordUser(userInput);
        
        if (passwordUser == null) {
            return ResponseEntity.badRequest().body("unable to register password user.");
        }
        UserSession userSession = new UserSession(
            passwordUser.getUser().getUsername(), passwordUser.getUser().getEmail(),
            passwordUser.getUser().getIcon(), passwordUser.getToken()
        );

        HttpHeaders headers = new HttpHeaders();
        
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+userSession.accessToken());
        headers.add("Session", userSession.toString());
        headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email,
        @RequestBody UserPasswordInput userPasswordInput
    ) {
        PasswordUser passwordUser = null;
        if(username != null) {
            passwordUser = authService.signInWithUsername(username, userPasswordInput.password());
        } else {
            passwordUser = authService.signInWithEmail(email, userPasswordInput.password());
        }
        HttpHeaders headers = new HttpHeaders();
        if (passwordUser != null) {
            UserSession userSession = new UserSession(
                passwordUser.getUser().getUsername(), passwordUser.getUser().getEmail(),
                passwordUser.getUser().getIcon(), passwordUser.getToken()
            );
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer "+userSession.accessToken());
            headers.add("Session", userSession.toString());
            headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/");

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
        }
        headers.add("Location", dotenv.get("FRONT_END_BASE_URL")+"/login");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(null);
    }

    @GetMapping("/auth/signout")
    public ResponseEntity<String> signOutPasswordUser(@RequestParam(value = "username", required = false) String username) {
        authService.signOutPasswordUser(username);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", dotenv.get("FRONT_END_BASE_URL") + "/login");
    
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }
}