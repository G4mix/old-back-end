package com.gamix.config;

import com.gamix.exceptions.image.MaxAllowableSizeExceeded;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException() {
        return ResponseEntity.badRequest().body(new MaxAllowableSizeExceeded());
    }
}