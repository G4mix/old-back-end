package com.gamix.resolvers.user;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.records.userController.PartialUserInput;
import com.gamix.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class UserMutationResolver implements GraphQLMutationResolver {
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    @MutationMapping
    User updateUser(@Argument("input") PartialUserInput userInput) throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        return userService.updateUser(token, userInput);
    }

    @MutationMapping
    boolean deleteAccount() throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        return userService.deleteAccount(token);
    }
}
