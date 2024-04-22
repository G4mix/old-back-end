package com.gamix.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.gamix.exceptions.user.UserNotFoundByEmail;
import com.gamix.exceptions.user.UserNotFoundByUsername;
import com.gamix.models.User;
import com.gamix.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    @ResponseBody
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.findByUsername(username);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundByUsername userNotFoundByUsername) {
            throw new UserNotFoundByUsername();
        }
    }

    @GetMapping("/{email}")
    @ResponseBody
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.findByEmail(email);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundByEmail userNotFoundByEmail) {
            throw new UserNotFoundByEmail();
        }
    }   
}