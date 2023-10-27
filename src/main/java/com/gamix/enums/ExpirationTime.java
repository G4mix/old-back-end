package com.gamix.enums;

public enum ExpirationTime {
    ACCESS_TOKEN(3600000), REFRESH_TOKEN(86400000), REMEMBER_ME(2592000000L);

    private final long value;

    ExpirationTime(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
