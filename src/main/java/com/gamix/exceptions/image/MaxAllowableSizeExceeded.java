package com.gamix.exceptions.image;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class MaxAllowableSizeExceeded extends ExceptionBase {
    public MaxAllowableSizeExceeded() {
        super(ExceptionMessage.MAX_ALLOWABLE_SIZE_EXCEEDED);
    }
}
