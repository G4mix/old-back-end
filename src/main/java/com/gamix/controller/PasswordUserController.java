package com.gamix.controller;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.records.passwordUserController.SessionReturn;
import com.gamix.records.passwordUserController.SignInPasswordUserInput;
import com.gamix.records.passwordUserController.SignUpPasswordUserInput;
import com.gamix.service.PasswordUserService;
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
public class PasswordUserController {
    private final PasswordUserService passwordUserService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signUpPasswordUser(
        @RequestBody SignUpPasswordUserInput signUpPasswordUserInput
    ) throws ExceptionBase {
        try {
            SessionReturn session = passwordUserService.signUpPasswordUser(signUpPasswordUserInput);

            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + session.getToken())
                .body(session);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signInPasswordUser(
            @RequestBody SignInPasswordUserInput signInPasswordUserInput,
            HttpServletRequest req) throws ExceptionBase {
        try {
            SessionReturn session = passwordUserService.signInPasswordUser(signInPasswordUserInput);

            return ResponseEntity.ok()
                .header("Authorization", "Bearer " + session.getToken())
                .body(session);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}