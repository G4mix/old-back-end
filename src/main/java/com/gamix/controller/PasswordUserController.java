package com.gamix.controller;

import static com.gamix.utils.ControllerUtils.throwError;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.options.CookieOptions;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.service.PasswordUserService;
import com.gamix.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class PasswordUserController {
    @Autowired
    private PasswordUserService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signUpPasswordUser(
        @RequestBody Map<String, String> requestBody,
        HttpServletRequest req
    ) throws ExceptionBase {
        try {
            SignUpPasswordUserInput signUpPasswordUserInput = new SignUpPasswordUserInput(
                requestBody.get("username"),
                requestBody.get("email"),
                requestBody.get("password")
            );

            JwtTokens jwtTokens = authService.signUpPasswordUser(signUpPasswordUserInput);

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens.accessToken(), 
                jwtTokens.refreshToken(), 
                new CookieOptions(false, req.isSecure())
            );
    
            return ResponseEntity.status(HttpStatus.OK).body(cookieStrings);
        } catch(ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
        @RequestBody Map<String, String> requestBody,
        HttpServletRequest req
    ) throws ExceptionBase {
        try {
            SignInPasswordUserInput signInPasswordUserInput = new SignInPasswordUserInput(
                requestBody.get("username"),
                requestBody.get("email"),
                requestBody.get("password"),
                Boolean.parseBoolean(requestBody.get("rememberMe"))
            );
            System.out.println(signInPasswordUserInput);
            JwtTokens jwtTokens = authService.signInPasswordUser(
                signInPasswordUserInput
            );
            
            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens.accessToken(), 
                jwtTokens.refreshToken(),
                new CookieOptions(signInPasswordUserInput.rememberMe(), req.isSecure())
            );

            return ResponseEntity.status(HttpStatus.OK).body(cookieStrings);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/signout")
    public ResponseEntity<Object> signOutPasswordUser(
        @RequestBody Map<String, String> requestBody
    ) throws ExceptionBase {
        try {
            SignOutPasswordUserInput signOutPasswordUserInput = new SignOutPasswordUserInput (
                requestBody.get("accessToken"),
                requestBody.get("refreshToken")
            );

            authService.signOutPasswordUser(signOutPasswordUserInput);
            Map<String, Object> body = new HashMap<>();
            body.put("success", true);
            return ResponseEntity.status(HttpStatus.OK).body(body);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<Object> refreshTokenPasswodUser(
        @RequestBody Map<String, String> requestBody,
        HttpServletRequest req
    ) throws ExceptionBase {
        try {
            JwtTokens refreshedTokens = authService.refreshToken(requestBody.get("refreshToken"));

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                refreshedTokens.accessToken(), 
                refreshedTokens.refreshToken(),
                new CookieOptions(refreshedTokens.rememberMe(), req.isSecure())
            );
            
            return ResponseEntity.status(HttpStatus.OK).body(cookieStrings);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(ExceptionBase.class)
        public ResponseEntity<Object> handleJwtAuthenticationException(ExceptionBase ex) {
            return throwError(ex);
        }
    }
}