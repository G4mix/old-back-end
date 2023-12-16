package com.gamix.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.gamix.exceptions.ExceptionBase;
import graphql.ErrorClassification;
import graphql.GraphQLError;

public class ControllerUtils {
    
    public static ResponseEntity<Object> throwError(ExceptionBase ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("error", ex.getError());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}
