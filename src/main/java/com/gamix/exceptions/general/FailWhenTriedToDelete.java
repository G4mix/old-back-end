package com.gamix.exceptions.general;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class FailWhenTriedToDelete extends ExceptionBase {
    public FailWhenTriedToDelete() {
        super(ExceptionMessage.FAIL_WHEN_TRIED_TO_DELETE);
    }
}
