package com.gamix.exceptions.image;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class HandleImagesError extends ExceptionBase {
    public HandleImagesError() {
        super(ExceptionMessage.HANDLE_IMAGE_ERROR);
    }
}