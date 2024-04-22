package com.gamix.resolvers.user;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.user.UserNotFoundByEmail;
import com.gamix.exceptions.user.UserNotFoundByUsername;
import com.gamix.models.User;
import com.gamix.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class UserQueryResolver implements GraphQLQueryResolver {
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    @QueryMapping
    User findUserByToken() {
        String accessToken = httpServletRequest.getHeader("Authorization");
        return userService.findUserByToken(accessToken);
    }

/*    @QueryMapping
    User findUserByUsername(@Argument String username) throws ExceptionBase {
        return userService.findByUsername(username).orElseThrow(UserNotFoundByUsername::new);
    }

    @QueryMapping
    User findUserByEmail(@Argument String email) throws ExceptionBase {
        return userService.findByEmail(email).orElseThrow(UserNotFoundByEmail::new);
    }*/

}
