package com.gamix.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.gamix.exceptions.BackendException;

public class ControllerUtils {
    public static ResponseEntity<Object> throwError(BackendException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
}
