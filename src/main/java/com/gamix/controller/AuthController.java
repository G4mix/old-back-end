package com.gamix.controller;

import com.gamix.communication.authController.SessionReturn;
import com.gamix.communication.authController.SignInUserInput;
import com.gamix.communication.authController.SignUpUserInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(tags = "Auth", description = "Signup in the Gamix plataform")
    @PostMapping("/signup")
    public ResponseEntity<Object> signUpPasswordUser(
            @RequestBody SignUpUserInput signUpUserInput
    ) throws ExceptionBase {
        try {
            SessionReturn session = authService.signUpPasswordUser(signUpUserInput);
            return ResponseEntity.ok()
                    .header("Set-Cookie", "token="+session.getToken()+"; path=/; max-age=3600; SameSite=Lax")
                    .body(session.getUser());
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(tags = "Auth", description = "Signin in the Gamix plataform")
    @PostMapping("/signin")
    public ResponseEntity<Object> signInPasswordUser(@RequestBody SignInUserInput signInUserInput) throws ExceptionBase {
        try {
            SessionReturn session = authService.signInPasswordUser(signInUserInput);
            int maxAge = signInUserInput.rememberMe() ? 259200 : 3600;
            return ResponseEntity.ok()
                    .header("Set-Cookie",
                            "token="+session.getToken()+"; path=/; max-age="+maxAge+"; SameSite=Lax")
                    .body(session.getUser());
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}