package com.gamix.exceptions.userProfile;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;

public class UserProfileNotFound extends ExceptionBase {
    public UserProfileNotFound() {
        super(ExceptionMessage.USER_PROFILE_NOT_FOUND);
    }
}
