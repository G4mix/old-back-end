package com.gamix.controller;

import java.util.List;
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
import com.gamix.records.JwtRecords.JwtTokens;
import com.gamix.records.SessionRecords.UserSessionWithRefreshToken;
import com.gamix.records.UserRecords.UserInput;
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
        UserSessionWithRefreshToken userSessionWithRefreshToken = authService.signUpPasswordUser(userInput);
        
        HttpHeaders headers = new HttpHeaders();

        if (userSessionWithRefreshToken == null) {
            headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/register");
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(null);
        }

        List<String> cookieStrings = CookieUtils.generateCookies(
            userSessionWithRefreshToken.accessToken(), 
            userSessionWithRefreshToken.refreshToken(), 
            userInput.rememberMe()
        );

        headers.addAll(HttpHeaders.SET_COOKIE, cookieStrings);
        headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
        @RequestBody Map<String, String> requestBody
    ) {
        UserSessionWithRefreshToken userSessionWithRefreshToken;

        if(username != null) {
            userSessionWithRefreshToken = authService.signInWithUsername(username, requestBody.get("password"), rememberMe);
        } else {
            userSessionWithRefreshToken = authService.signInWithEmail(email, requestBody.get("password"), rememberMe);
        }
        
        HttpHeaders headers = new HttpHeaders();
        if (userSessionWithRefreshToken == null) {
            headers.add("Location", dotenv.get("FRONT_END_BASE_URL")+"/login");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(null);
        }

        List<String> cookieStrings = CookieUtils.generateCookies(
            userSessionWithRefreshToken.accessToken(), 
            userSessionWithRefreshToken.refreshToken(), rememberMe
        );

        headers.addAll(HttpHeaders.SET_COOKIE, cookieStrings);
        headers.add(HttpHeaders.LOCATION, dotenv.get("FRONT_END_BASE_URL") + "/");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @GetMapping("/auth/signout")
    public ResponseEntity<String> signOutPasswordUser() {        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", dotenv.get("FRONT_END_BASE_URL") + "/login");
    
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<String> refreshTokenPasswodUser(
        @RequestBody Map<String, String> requestBody
    ) {
        JwtTokens refreshedTokens = authService.refreshToken(requestBody.get("refreshToken"));

        HttpHeaders headers = new HttpHeaders();
        List<String> cookieStrings = CookieUtils.generateCookies(
            refreshedTokens.accessToken(), 
            refreshedTokens.refreshToken(),
            refreshedTokens.rememberMe()
        );
            
        headers.add("Location", dotenv.get("FRONT_END_BASE_URL") + "/");
        headers.addAll(HttpHeaders.SET_COOKIE, cookieStrings);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(BackendException.class)
        public ResponseEntity<Object> handleJwtAuthenticationException(BackendException ex) {
            return ResponseEntity.status(ex.getStatus()).body(ex);
        }
    }
}