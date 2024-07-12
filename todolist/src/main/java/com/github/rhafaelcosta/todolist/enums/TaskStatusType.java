package com.github.rhafaelcosta.todolist.enums;

import java.util.Arrays;

import com.github.rhafaelcosta.todolist.exceptions.EnumNotFoundException;

public enum TaskStatusType {

    ACTIVE   (1, "Active"),
    ON_HOLD  (2, "On Hold"),
    PROPOSED (3, "Proposed"),
    RESOLVED (4, "Resolved");

    private final Integer code;
    private final String description;

    TaskStatusType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TaskStatusType getTaskStatusTypeByCode(Integer code) throws EnumNotFoundException {
        return Arrays.stream(TaskStatusType.values())
                     .filter(status -> status.code.equals(code))
                     .findFirst()
                     .orElseThrow( () -> new EnumNotFoundException("Invalid TaskStatusType code: " + code));
    }
}
