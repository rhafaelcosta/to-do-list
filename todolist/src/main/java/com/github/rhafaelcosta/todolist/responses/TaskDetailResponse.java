package com.github.rhafaelcosta.todolist.responses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.rhafaelcosta.todolist.models.Task;

public record TaskDetailResponse(
    Long id,
    String title,
    String description,
    UserResponse user,
    Integer priority,
    SeverityResponse severityType,
    TaskStatusResponse taskStatusType,
    List<TagResponse> tags
    ) {

    public TaskDetailResponse(Task task) {
        this(task.getId(), task.getTitle(), task.getDescription(), new UserResponse(task.getOwner()),
        task.getPriority(), new SeverityResponse(task.getSeverityType()), new TaskStatusResponse(task.getTaskStatusType()), new ArrayList<>());

        var list = task.getTags().stream().map(TagResponse::new).collect(Collectors.toList());
        this.tags.addAll(list);
    }

}
