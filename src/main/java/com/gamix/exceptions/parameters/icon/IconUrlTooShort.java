package com.gamix.exceptions.parameters.icon;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class IconUrlTooShort extends ExceptionBase {
    public IconUrlTooShort() {
        super(ExceptionMessage.ICON_URL_TOO_SHORT);
    }
}