package com.gamix.exceptions.parameters.icon;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class IconUrlTooLong extends ExceptionBase {
    public IconUrlTooLong() {
        super(ExceptionMessage.ICON_URL_TOO_LONG);
    }
}