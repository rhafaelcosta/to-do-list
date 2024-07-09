package com.github.rhafaelcosta.todolist.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rhafaelcosta.todolist.models.TaskTag;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {

    List<TaskTag> findAllByTagId(Long id);
}
