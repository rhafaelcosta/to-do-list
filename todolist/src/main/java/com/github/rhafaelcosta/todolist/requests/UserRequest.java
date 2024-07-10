package com.github.rhafaelcosta.todolist.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(@NotBlank String name, @NotBlank @Email String email, @NotNull Boolean active) {

}
