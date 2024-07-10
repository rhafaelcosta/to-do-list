package com.github.rhafaelcosta.todolist.responses;

import com.github.rhafaelcosta.todolist.models.User;

public record UserResponse(Long id, String name, String email, Boolean active) {

    public UserResponse(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getActive());
    }

}