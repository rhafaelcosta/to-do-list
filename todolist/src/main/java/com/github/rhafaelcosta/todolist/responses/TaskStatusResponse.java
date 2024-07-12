package com.github.rhafaelcosta.todolist.responses;

import com.github.rhafaelcosta.todolist.enums.TaskStatusType;

public record TaskStatusResponse(Integer id, String name) {

    public TaskStatusResponse(TaskStatusType taskStatus) {
        this(taskStatus.getCode(), taskStatus.getDescription());
    }

}
