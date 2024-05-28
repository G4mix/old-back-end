package com.gamix.controller;

import com.gamix.communication.auth.SessionDTO;
import com.gamix.communication.auth.SignInUserInput;
import com.gamix.communication.auth.SignUpUserInput;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.GamixError;
import com.gamix.models.User;
import com.gamix.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
        tags = "Auth",
        description = "Signup in the Gamix plataform",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Account accessed",
                headers = {
                    @Header(
                        name = "Set-Cookie",
                        description = "The cookie used to access the resources of the Gamix",
                        example = "token=token_here; path=/; max-age=3600; SameSite=Lax"
                    )
                },
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Wrong input",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Resource already exists",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            )
        }
    )
    @PostMapping("/signup")
    public ResponseEntity<Object> signUpPasswordUser(
            @RequestBody SignUpUserInput signUpUserInput
    ) throws ExceptionBase {
        try {
            SessionDTO session = authService.signUpPasswordUser(signUpUserInput);
            return ResponseEntity.ok()
                    .header("Set-Cookie", "token="+session.getToken()+"; path=/; max-age=3600; SameSite=Lax")
                    .body(session.getUser());
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(
        tags = "Auth",
        description = "Signin in the Gamix plataform",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Account accessed",
                headers = {
                    @Header(
                        name = "Set-Cookie",
                        description = "The cookie used to access the resources of the Gamix",
                        example = "token=token_here; path=/; max-age=3600; SameSite=Lax"
                    )
                },
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Wrong input",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Resource not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            ),
            @ApiResponse(
                responseCode = "429",
                description = "Too many requests",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            )
        }
    )
    @PostMapping("/signin")
    public ResponseEntity<Object> signInPasswordUser(@RequestBody SignInUserInput signInUserInput) throws ExceptionBase {
        try {
            SessionDTO session = authService.signInPasswordUser(signInUserInput);
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