package com.gamix.resolvers.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;
import com.gamix.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserMutationResolver implements GraphQLMutationResolver {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    User updateUser(@Argument("input") PartialUserInput userInput) throws ExceptionBase {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);
            User updatedUser = userService.updateUser(accessToken, userInput);
            return updatedUser;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @MutationMapping
    boolean deleteAccount() throws ExceptionBase {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);
            return userService.deleteAccount(accessToken);
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }
}
