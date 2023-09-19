package com.gamix.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamix.exceptions.BackendException;
import com.gamix.models.PasswordUser;
import com.gamix.records.UserRecords.RefreshedTokens;
import com.gamix.records.UserRecords.UserInput;
import com.gamix.records.UserRecords.UserPasswordInput;
import com.gamix.service.AuthService;
import com.gamix.utils.CookieUtils;

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
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/register");
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(null);
        }

        List<String> cookieStrings = CookieUtils.generateCookies(passwordUser, userInput.rememberMe());

        HttpHeaders headers = new HttpHeaders();
        
        headers.addAll(HttpHeaders.SET_COOKIE, cookieStrings);
        headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
        @RequestBody UserPasswordInput userPasswordInput
    ) {
        PasswordUser passwordUser = null;

        if(username != null) {
            passwordUser = authService.signInWithUsername(username, userPasswordInput.password(), rememberMe);
        } else {
            passwordUser = authService.signInWithEmail(email, userPasswordInput.password(), rememberMe);
        }
        
        HttpHeaders headers = new HttpHeaders();
        if (passwordUser == null) {
            headers.add("Location", dotenv.get("FRONT_END_BASE_URL")+"/login");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(null);
        }

        List<String> cookieStrings = CookieUtils.generateCookies(passwordUser, rememberMe);

        headers.addAll(HttpHeaders.SET_COOKIE, cookieStrings);
        headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @GetMapping("/auth/signout")
    public ResponseEntity<String> signOutPasswordUser(
        @RequestHeader("Authorization") String token
    ) {
        String accessToken = token.substring(7);
        authService.signOutPasswordUser(accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", dotenv.get("FRONT_END_BASE_URL") + "/login");
    
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<String> refreshTokenPasswodUser(
        @RequestBody String refreshToken
    ) {
        RefreshedTokens refreshedTokens = authService.refreshToken(refreshToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", dotenv.get("FRONT_END_BASE_URL") + "/");
    
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(refreshedTokens.toString());
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(BackendException.class)
        public ResponseEntity<Object> handleJwtAuthenticationException(BackendException ex) {
            return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
        }
    }
}