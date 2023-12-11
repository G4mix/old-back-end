package com.gamix.enums;

import lombok.Getter;

@Getter
public enum ExpirationTime {
    ACCESS_TOKEN(3600000), REFRESH_TOKEN(86400000), REMEMBER_ME(2592000000L);

    private final long value;

    ExpirationTime(long value) {
        this.value = value;
    }

}
