package com.gamix.controller;

import static com.gamix.utils.ControllerUtils.throwError;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.records.inputs.passwordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.passwordUserController.SignUpPasswordUserInput;
import com.gamix.service.PasswordUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PasswordUserController {
    private final PasswordUserService passwordUserService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Object> signUpPasswordUser(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest req) throws ExceptionBase {
        try {
            SignUpPasswordUserInput signUpPasswordUserInput = new SignUpPasswordUserInput(
                    requestBody.get("username"),
                    requestBody.get("email"),
                    requestBody.get("password"));

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + passwordUserService
                            .signUpPasswordUser(signUpPasswordUserInput))
                    .build();
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Object> signInPasswordUser(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest req) throws ExceptionBase {
        try {
            SignInPasswordUserInput signInPasswordUserInput = new SignInPasswordUserInput(
                    requestBody.get("username"),
                    requestBody.get("email"),
                    requestBody.get("password"),
                    Boolean.parseBoolean(requestBody.get("rememberMe")));

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + passwordUserService
                            .signInPasswordUser(signInPasswordUserInput))
                    .build();
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}