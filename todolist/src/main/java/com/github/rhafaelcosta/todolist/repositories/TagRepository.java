package com.github.rhafaelcosta.todolist.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rhafaelcosta.todolist.models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNameIgnoreCase(String name);

}
