package com.github.rhafaelcosta.todolist.enums;

import java.util.Arrays;

public enum SeverityType {

    CRITICAL (1, "Critical"),
    HIGH     (2, "High"),
    MEDIUM   (3, "Medium"),
    LOW      (4, "Low");

    private final Integer code;
    private final String description;

    SeverityType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static SeverityType getSeverityTypeByCode(Integer code) {
        return Arrays.stream(SeverityType.values())
                     .filter(status -> status.code.equals(code))
                     .findFirst()
                     .orElseThrow( () -> new IllegalArgumentException("Invalid SeverityType code: " + code));
    }

}
