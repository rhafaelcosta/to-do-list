package com.github.rhafaelcosta.todolist.requests;

import java.util.List;

import com.github.rhafaelcosta.todolist.responses.TagResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequest(

    @NotBlank
    String title,

    String description,

    @NotNull
    Long userId,

    @NotNull
    Integer priority,

    @NotNull
    Integer severityType,

    @NotNull
    Integer taskStatusType,

    List<TagResponse> tags) {

}
