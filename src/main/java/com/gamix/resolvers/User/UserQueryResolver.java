package com.gamix.resolvers.User;

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
        List<User> users = userService.findAllUsers(skip, limit);
        return users;
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByToken() throws Exception {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String accessToken = authorizationHeader.substring(7);
        User user = userService.findUserByToken(accessToken);
        return user;
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByUsername(@Argument String username) throws ExceptionBase {
        User user = userService.findUserByUsername(username);
        return user;
    }

    @PreAuthorize("hasAuthority('USER')")
    @QueryMapping
    User findUserByEmail(@Argument String email) throws ExceptionBase {
        User user = userService.findUserByEmail(email);
        return user;
    }

}
