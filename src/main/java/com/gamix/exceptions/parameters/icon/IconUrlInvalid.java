package com.gamix.exceptions.parameters.icon;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class IconUrlInvalid extends ExceptionBase {
    public IconUrlInvalid() {
        super(ExceptionMessage.ICON_URL_INVALID);
    }
}
