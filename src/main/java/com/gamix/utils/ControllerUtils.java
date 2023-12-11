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

    public static GraphQLError throwGraphQLError(ExceptionBase ex) {
        return GraphQLError.newError()
            .errorType(ErrorClassification
                    .errorClassification(ex.getStatus().toString()))
            .message(ex.getMessage())
           .build();
    }

    public static String calculateETag(Long version) {
        try {
            String data = String.valueOf(version);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes());

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
