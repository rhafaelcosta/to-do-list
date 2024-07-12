package com.github.rhafaelcosta.todolist.responses;

import com.github.rhafaelcosta.todolist.enums.SeverityType;

public record SeverityResponse(Integer id, String name) {

    public SeverityResponse(SeverityType severity) {
        this(severity.getCode(), severity.getDescription());
    }

}
