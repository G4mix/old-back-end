package com.gamix.resolvers.User;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    List<User> findAllUsers(@Argument("skip") int skip, @Argument("limit") int limit) {
        List<User> users = userService.findAllUsers(skip, limit);
        return users;
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByToken() throws Exception {
        try {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            String accessToken = authorizationHeader.substring(7);
            User user = userService.findUserByToken(accessToken);
            return user;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByUsername(@Argument String username) throws ExceptionBase {
        try {
            User user = userService.findUserByUsername(username);
            return user;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByEmail(@Argument String email) throws ExceptionBase {
        try {
            User user = userService.findUserByEmail(email);
            return user;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

}
