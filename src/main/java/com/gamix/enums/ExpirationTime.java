package com.gamix.enums;

public enum ExpirationTime {
    ACCESS_TOKEN(60000), REFRESH_TOKEN(70000), REMEMBER_ME(80000);
    //ACCESS_TOKEN(3600000), REFRESH_TOKEN(604800000), REMEMBER_ME(2592000000L);
    private final long value;

    ExpirationTime(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
