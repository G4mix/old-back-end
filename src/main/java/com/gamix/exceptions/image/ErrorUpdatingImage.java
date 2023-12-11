package com.gamix.exceptions.image;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class ErrorUpdatingImage extends ExceptionBase {
    public ErrorUpdatingImage() {
        super(ExceptionMessage.ERROR_UPDATING_IMAGE);
    }
}
