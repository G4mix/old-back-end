package com.gamix.controller;

import com.gamix.communication.authController.SessionReturn;
import com.gamix.communication.userController.UserReturn;
import com.gamix.security.JwtManager;
import com.gamix.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.service.UserService;
import lombok.RequiredArgsConstructor;

import static com.gamix.utils.ControllerUtils.throwError;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<User> getUserByAccessToken(@RequestHeader("Authorization") String token) {
        // try {
        User user = userService.findUserByToken(token);
        return ResponseEntity.ok().body(user);
        // } catch (ExceptionBase ex) {
        //     return throwError(ex);
        // }
    }

    @GetMapping("/username/{username}")
    @ResponseBody
    public ResponseEntity<Object> getUserByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok().body(new UserReturn(userService.findByUsername(username)));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @GetMapping("/email/{email}")
    @ResponseBody
    public ResponseEntity<Object> getUserByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok().body(new UserReturn(userService.findByEmail(email)));
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }

    @DeleteMapping()
    @ResponseBody
    public ResponseEntity<Object> deleteUserById(@RequestHeader("Authorization") String token) {
        System.out.println(token);
        try {
            userService.deleteAccount(JwtManager.getIdFromToken(token));
            return ResponseEntity.status(204).body(true);
        } catch (ExceptionBase ex) {
            return throwError(ex);
        }
    }
}