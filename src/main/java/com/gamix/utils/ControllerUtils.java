package com.gamix.utils;

import com.gamix.exceptions.ExceptionBase;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ControllerUtils {

    public static ResponseEntity<Object> throwError(ExceptionBase ex) {
        return ResponseEntity.status(ex.getStatus()).body(getErrorHashMap(ex));
    }

    public static Map<String, String> getErrorHashMap(ExceptionBase ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("error", ex.getError());
        return errorResponse;
    }
}
