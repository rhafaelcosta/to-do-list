package com.github.rhafaelcosta.todolist.responses;

import com.github.rhafaelcosta.todolist.models.Task;

public record TaskResponse(Long id, String title, String description, UserResponse user, Integer priority,
        SeverityResponse severityType, TaskStatusResponse taskStatusType) {

    public TaskResponse(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription(), new UserResponse(task.getOwner()),
                task.getPriority(), new SeverityResponse(task.getSeverityType()), new TaskStatusResponse(task.getTaskStatusType()));
    }

}
