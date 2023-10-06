package com.gamix.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.gamix.exceptions.ExceptionBase;

public class ControllerUtils {
    public static ResponseEntity<Object> throwError(ExceptionBase ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("error", ex.getError());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}