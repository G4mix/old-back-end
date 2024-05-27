package com.gamix.controller;

import com.gamix.security.JwtManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.GamixError;
import com.gamix.models.User;
import com.gamix.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import static com.gamix.utils.ControllerUtils.throwError;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    
    @Operation(
        tags = "User",
        description = "Get user informations using the access token",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Account accessed",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid Token supplied or user not exists"
            )
        }
    )
    @GetMapping()
    public ResponseEntity<Object> getUserByAccessToken(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok().body(userService.findUserByToken(token));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(
        tags = "User", 
        description = "Get user informations using the username", 
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Account accessed",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid username supplied or user not exists"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Username not found",
                content = @Content(
                    schema = @Schema(type = "application/json", implementation = GamixError.class)
                )
            ),
        }
    )
    @GetMapping("/username/{username}")
    @ResponseBody
    public ResponseEntity<Object> getUserByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok().body(userService.findByUsername(username));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(
        tags = "User",
        description = "Get user informations using the email",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Account accessed",
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid email supplied or user not exists"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GamixError.class)
                )
            )
        } 
    )
    @GetMapping("/email/{email}")
    @ResponseBody
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok().body(userService.findByEmail(email));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @Operation(
        tags = "User",
        description = "Delete user by ID",
        security = { @SecurityRequirement(name = "jwt") },
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Account deleted",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "true"))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Invalid Token supplied or user not exists"
            )
        }
    )
    @DeleteMapping()
    @ResponseBody
    public ResponseEntity<Object> deleteUserById(@RequestHeader("Authorization") String token) {
        try {
            userService.deleteAccount(JwtManager.getIdFromToken(token));
            return ResponseEntity.status(204).body(true);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}