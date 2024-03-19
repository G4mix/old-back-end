package com.gamix.resolvers.user;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.security.JwtManager;
import com.gamix.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class UserMutationResolver implements GraphQLMutationResolver {
    private final UserService userService;
    private final HttpServletRequest httpServletRequest;

    @MutationMapping
    void deleteAccount() throws ExceptionBase {
        String token = httpServletRequest.getHeader("Authorization");
        Integer id = JwtManager.getIdFromToken(token);
        try {
            userService.findUserById(id);
        } catch (ExceptionBase ex) {
            return;
        }
        userService.deleteAccount(id);
    }
}
