package com.github.rhafaelcosta.todolist.responses;

import com.github.rhafaelcosta.todolist.models.Tag;

public record TagResponse(Long id, String name) {

    public TagResponse(Tag tag) {
        this(tag.getId(), tag.getName());
    }

}