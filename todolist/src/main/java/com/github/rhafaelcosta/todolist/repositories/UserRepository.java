package com.github.rhafaelcosta.todolist.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.rhafaelcosta.todolist.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * This functionality is used to find the user by email (case insensitive) and
     * exclude the one with the specified ID.
     *
     * @param email the email of the user to find
     * @param id    the ID of the user to exclude from the search
     * @return an Optional containing the found user, or empty if no user is found
     */
    Optional<User> findByEmailIgnoreCaseAndIdNot(String email, Long id);

}
