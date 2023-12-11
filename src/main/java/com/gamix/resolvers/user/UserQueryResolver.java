package com.gamix.resolvers.user;

import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserQueryResolver implements GraphQLQueryResolver {
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    List<User> findAllUsers(@Argument("skip") int skip, @Argument("limit") int limit) {
        return userService.findAllUsers(skip, limit);
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByToken() {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        return userService.findUserByToken(accessToken);
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByUsername(@Argument String username) throws ExceptionBase {
        return userService.findUserByUsername(username);
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByEmail(@Argument String email) throws ExceptionBase {
        return userService.findUserByEmail(email);
    }

}
