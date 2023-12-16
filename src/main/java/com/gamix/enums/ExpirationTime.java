package com.gamix.enums;

import lombok.Getter;

@Getter
public enum ExpirationTime {
    ACCESS_TOKEN(3600000L), REMEMBER_ME(259200000L);

    private final long value;

    ExpirationTime(long value) {
        this.value = value;
    }

}
