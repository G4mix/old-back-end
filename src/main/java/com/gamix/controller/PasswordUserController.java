package com.gamix.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import static com.gamix.utils.ControllerUtils.throwError;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class PasswordUserController {
    @Autowired
    private PasswordUserService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signUpPasswordUser(
        HttpServletRequest req, 
        @RequestBody SignUpPasswordUserInput signUpPasswordUserInput
    ) {
        try {
            JwtTokens jwtTokens = authService.signUpPasswordUser(signUpPasswordUserInput);
            
            HttpHeaders headers = new HttpHeaders();
    
            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens.accessToken(), 
                jwtTokens.refreshToken(), 
                new CookieOptions(false, req.isSecure())
            );
    
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
        } catch(ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
        HttpServletRequest req,
        @RequestBody SignInPasswordUserInput signInPasswordUserInput
    ) {
        try {
            JwtTokens jwtTokens = authService.signInPasswordUser(
                signInPasswordUserInput
            );
            
            HttpHeaders headers = new HttpHeaders();

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens.accessToken(), 
                jwtTokens.refreshToken(),
                new CookieOptions(signInPasswordUserInput.rememberMe(), req.isSecure())
            );

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/signout")
    public ResponseEntity<Object> signOutPasswordUser(
        @RequestBody Map<String, String> requestBody
    ) {
        try {
            String accessToken = requestBody.get("accessToken");
            String refreshToken = requestBody.get("refreshToken");

            SignOutPasswordUserInput signOutPasswordUserInput = new SignOutPasswordUserInput(accessToken, refreshToken);
            authService.signOutPasswordUser(signOutPasswordUserInput);

            HttpHeaders headers = new HttpHeaders();
        
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<Object> refreshTokenPasswodUser(
        HttpServletRequest req,
        @RequestBody Map<String, String> requestBody
    ) {
        try {
            JwtTokens refreshedTokens = authService.refreshToken(requestBody.get("refreshToken"));

            HttpHeaders headers = new HttpHeaders();
            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                refreshedTokens.accessToken(), 
                refreshedTokens.refreshToken(),
                new CookieOptions(refreshedTokens.rememberMe(), req.isSecure())
            );
                
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
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