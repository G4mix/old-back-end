package com.gamix.exceptions.parameters.icon;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class IconNull extends ExceptionBase{
    public IconNull() {
        super(ExceptionMessage.ICON_NULL);
    }
}
