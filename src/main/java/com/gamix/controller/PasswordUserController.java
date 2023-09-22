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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.options.CookieOptions;
import com.gamix.records.returns.security.JwtSessionWithRefreshToken;
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
            JwtSessionWithRefreshToken jwtSessionWithRefreshToken = authService.signUpPasswordUser(signUpPasswordUserInput);
            
            HttpHeaders headers = new HttpHeaders();

            if (jwtSessionWithRefreshToken == null) throw new BackendException(ExceptionMessage.PASSWORDUSER_ALREADY_EXISTS);

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtSessionWithRefreshToken.accessToken(), 
                jwtSessionWithRefreshToken.refreshToken(), 
                new CookieOptions(false, req.isSecure())
            );

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
        } catch (BackendException ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
        HttpServletRequest req,
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
        @RequestBody Map<String, String> requestBody
    ) {
        try {
            SignInPasswordUserInput signInPasswordUserInput = new SignInPasswordUserInput(
                username, email, requestBody.get("password"), rememberMe
            );
            JwtSessionWithRefreshToken jwtSessionWithRefreshToken = authService.signInPasswordUser(
                signInPasswordUserInput
            );
            if (jwtSessionWithRefreshToken == null) {
                throw new BackendException(ExceptionMessage.INVALID_JWT_SESSION_WITH_REFRESH_TOKEN);
            }
            
            HttpHeaders headers = new HttpHeaders();

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtSessionWithRefreshToken.accessToken(), 
                jwtSessionWithRefreshToken.refreshToken(),
                new CookieOptions(rememberMe, req.isSecure())
            );

            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(cookieStrings);
        } catch (BackendException ex) {
            return throwError(ex);
        }
    }

    // @PreAuthorize("@securityUtils.checkUsername(#accessToken) == #requestBody.get("username")")
    @PostMapping("/auth/signout")
    public ResponseEntity<Object> signOutPasswordUser(
        @RequestHeader("Authorization") String accessToken,
        @RequestBody Map<String, String> requestBody
    ) {
        try {
            String refreshToken = requestBody.get("refreshToken");

            SignOutPasswordUserInput signOutPasswordUserInput = new SignOutPasswordUserInput(accessToken, refreshToken);
            authService.signOutPasswordUser(signOutPasswordUserInput);

            HttpHeaders headers = new HttpHeaders();
        
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(null);
        } catch (BackendException ex) {
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
        } catch (BackendException ex) {
            return throwError(ex);
        }
    }
    
    @ControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(BackendException.class)
        public ResponseEntity<Object> handleJwtAuthenticationException(BackendException ex) {
            return throwError(ex);
        }
    }
}