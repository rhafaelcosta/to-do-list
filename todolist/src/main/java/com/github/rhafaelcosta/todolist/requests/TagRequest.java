package com.github.rhafaelcosta.todolist.requests;

import jakarta.validation.constraints.NotBlank;

public record TagRequest(@NotBlank String name) {
}