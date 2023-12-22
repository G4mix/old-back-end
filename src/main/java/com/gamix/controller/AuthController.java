package com.gamix.controller;

import com.gamix.communication.userController.SessionReturn;
import com.gamix.communication.userController.SignInUserInput;
import com.gamix.communication.userController.SignUpUserInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gamix.utils.ControllerUtils.throwError;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUpPasswordUser(
            @RequestBody SignUpUserInput signUpUserInput
    ) throws ExceptionBase {
        try {
            SessionReturn session = authService.signUpPasswordUser(signUpUserInput);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + session.getToken())
                    .body(session);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signInPasswordUser(
            @RequestBody SignInUserInput signInUserInput,
            HttpServletRequest req) throws ExceptionBase {
        try {
            SessionReturn session = authService.signInPasswordUser(signInUserInput);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + session.getToken())
                    .body(session);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}