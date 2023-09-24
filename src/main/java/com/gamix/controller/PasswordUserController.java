package com.gamix.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.options.CookieOptions;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.service.PasswordUserService;
import com.gamix.utils.CookieUtils;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

@Controller
public class PasswordUserController {
    @Autowired
    private PasswordUserService authService;

    @MutationMapping
    public Object signUpPasswordUser(
        @Argument("input") SignUpPasswordUserInput signUpPasswordUserInput
        // HttpServletRequest req
    ) throws ExceptionBase {
        try {
            JwtTokens jwtTokens = authService.signUpPasswordUser(signUpPasswordUserInput);

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens.accessToken(), 
                jwtTokens.refreshToken(), 
                new CookieOptions(false,false) // req.isSecure()
            );
    
            return cookieStrings;
        } catch(ExceptionBase ex) {
            throw ex;
        }
    }

    @MutationMapping
    public Object signInPasswordUser(
        @Argument("input") SignInPasswordUserInput signInPasswordUserInput
        // HttpServletRequest req,
    ) throws ExceptionBase {
        try {
            JwtTokens jwtTokens = authService.signInPasswordUser(
                signInPasswordUserInput
            );
            
            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                jwtTokens.accessToken(), 
                jwtTokens.refreshToken(),
                new CookieOptions(signInPasswordUserInput.rememberMe(), false) //req.isSecure()
            );

            return cookieStrings;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @MutationMapping
    public Object signOutPasswordUser(
        @Argument("input") SignOutPasswordUserInput signOutPasswordUserInput
    ) throws ExceptionBase {
        try {
            authService.signOutPasswordUser(signOutPasswordUserInput);
            Map<String, Object> responseBody = new HashMap<String, Object>();
            responseBody.put("success", true);
            return responseBody;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @MutationMapping
    public Object refreshTokenPasswodUser(
        // HttpServletRequest req,
        @Argument("refreshToken") String refreshToken
    ) throws ExceptionBase {
        try {
            JwtTokens refreshedTokens = authService.refreshToken(refreshToken);

            Map<String, String> cookieStrings = CookieUtils.generateCookies(
                refreshedTokens.accessToken(), 
                refreshedTokens.refreshToken(),
                new CookieOptions(refreshedTokens.rememberMe(), false) // req.isSecure()
            );
                
            return cookieStrings;
        } catch (ExceptionBase ex) {
            throw ex;
        }
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull Throwable ex, @NonNull DataFetchingEnvironment environment) {
        if (ex instanceof ExceptionBase) {
            return GraphQLError
                .newError()
                .errorType(ErrorClassification.errorClassification(((ExceptionBase) ex).getStatus().toString()))
                .message(ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
        }
        return GraphQLError
            .newError()
            .errorType(ErrorType.BAD_REQUEST)
            .message(ex.getMessage())
            .path(environment.getExecutionStepInfo().getPath())
            .location(environment.getField().getSourceLocation())
            .build();
    }
}