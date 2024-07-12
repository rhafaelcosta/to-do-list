package com.github.rhafaelcosta.todolist.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rhafaelcosta.todolist.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
