package com.gamix.controller;

import static com.gamix.utils.ControllerUtils.throwError;

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
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.options.CookieOptions;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.service.PasswordUserService;
import com.gamix.utils.CookieUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class PasswordUserController {
    @Autowired
    private PasswordUserService passwordUserService;

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

            JwtTokens jwtTokens = passwordUserService.signUpPasswordUser(signUpPasswordUserInput);

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens, new CookieOptions(false, req.isSecure())
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

            JwtTokens jwtTokens = passwordUserService.signInPasswordUser(
                signInPasswordUserInput
            );
            
            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens, new CookieOptions(signInPasswordUserInput.rememberMe(), req.isSecure())
            );

            return ResponseEntity.status(HttpStatus.OK).body(cookieStrings);
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
            JwtTokens refreshedTokens = passwordUserService.refreshToken(requestBody.get("refreshToken"));

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                refreshedTokens, new CookieOptions(refreshedTokens.rememberMe(), req.isSecure())
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