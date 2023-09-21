package com.gamix.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamix.exceptions.BackendException;
import com.gamix.records.inputs.UserInput;
import com.gamix.records.returns.jwt.JwtSessionWithRefreshToken;
import com.gamix.records.returns.jwt.JwtTokens;
import com.gamix.service.AuthService;
import com.gamix.utils.CookieUtils;

@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signUpPasswordUser(@RequestBody UserInput userInput) {
        JwtSessionWithRefreshToken jwtSessionWithRefreshToken = authService.signUpPasswordUser(userInput);
        
        HttpHeaders headers = new HttpHeaders();

        if (jwtSessionWithRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(null);
        }

        Map<String, String> cookieStrings = CookieUtils.generateCookies(
            jwtSessionWithRefreshToken.accessToken(), 
            jwtSessionWithRefreshToken.refreshToken(), 
            userInput.rememberMe()
        );

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
        @RequestBody Map<String, String> requestBody
    ) {
        JwtSessionWithRefreshToken jwtSessionWithRefreshToken;

        if(username != null) {
            jwtSessionWithRefreshToken = authService.signInWithUsername(username, requestBody.get("password"), rememberMe);
        } else {
            jwtSessionWithRefreshToken = authService.signInWithEmail(email, requestBody.get("password"), rememberMe);
        }
        
        HttpHeaders headers = new HttpHeaders();
        if (jwtSessionWithRefreshToken == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(null);
        }

        Map<String, String> cookieStrings = CookieUtils.generateCookies(
            jwtSessionWithRefreshToken.accessToken(), 
            jwtSessionWithRefreshToken.refreshToken(), rememberMe
        );

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
    }

    @GetMapping("/auth/signout")
    public ResponseEntity<String> signOutPasswordUser() {        
        HttpHeaders headers = new HttpHeaders();
    
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<Object> refreshTokenPasswodUser(
        @RequestBody Map<String, String> requestBody
    ) {
        JwtTokens refreshedTokens = authService.refreshToken(requestBody.get("refreshToken"));

        HttpHeaders headers = new HttpHeaders();
        Map<String, String> cookieStrings = CookieUtils.generateCookies(
            refreshedTokens.accessToken(), 
            refreshedTokens.refreshToken(),
            refreshedTokens.rememberMe()
        );
            
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(BackendException.class)
        public ResponseEntity<Object> handleJwtAuthenticationException(BackendException ex) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", ex.getMessage());

            return ResponseEntity.status(ex.getStatus()).body(ex);
        }
    }
}