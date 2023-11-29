package com.gamix.exceptions.image;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class ErrorCreatingImage extends ExceptionBase {
    public ErrorCreatingImage() {
        super(ExceptionMessage.ERROR_CREATING_IMAGE);
    }
}
