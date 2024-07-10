package com.github.rhafaelcosta.todolist.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.rhafaelcosta.todolist.exceptions.EntityAlreadyExistsException;
import com.github.rhafaelcosta.todolist.models.User;
import com.github.rhafaelcosta.todolist.repositories.UserRepository;
import com.github.rhafaelcosta.todolist.requests.UserRequest;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of all users currently stored in the database.
     *
     * @return a list containing all users
     */
    public List<User> listar() {
        return this.userRepository.findAll();
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user to retrieve
     * @return the found user
     * @throws EntityNotFoundException if no user with the given ID exists
     */
    public User findById(Long id) throws EntityNotFoundException {
        return this.userRepository
                   .findById(id)
                   .orElseThrow(() -> new EntityNotFoundException(String.format("User not found with id: %d", id)));
    }

    /**
     * Creates a new user based on the provided data.
     *
     * @param request the user data to save
     * @return the created user
     * @throws EntityAlreadyExistsException if the provided email is already in use by another user
     */
    public User save(UserRequest request) throws EntityAlreadyExistsException {
        verifyUserEmailAlreadyExists(null, request);

        var user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setActive(request.active());

        this.userRepository.save(user);

        return user;
    }

    /**
     * Updates the data of an existing user based on the provided ID.
     *
     * @param id the ID of the user to update
     * @param request the new user data
     * @return the updated user
     * @throws EntityNotFoundException if no user with the given ID exists
     * @throws EntityAlreadyExistsException if the provided email is already in use by another user
     */
    public User save(Long id, UserRequest request) throws EntityNotFoundException, EntityAlreadyExistsException {
        var user = findById(id);

        verifyUserEmailAlreadyExists(id, request);

        user.setName(request.name());
        user.setEmail(request.email());
        user.setActive(request.active());

        this.userRepository.save(user);

        return user;
    }

    /**
     * Deactivates an existing user based on the provided ID.
     *
     * @param id the ID of the user to deactivate
     * @throws EntityNotFoundException if no user with the given ID exists
     */
    public void delete(Long id) throws EntityNotFoundException {
        var user = findById(id);
        user.setActive(false);
        this.userRepository.save(user);
    }

    /**
     * Checks if the provided email is already in use by another user, excluding the user with the provided ID.
     *
     * @param id the ID of the current user (may be null if it's a new user)
     * @param request the user data to check
     * @throws EntityAlreadyExistsException if the provided email is already in use by another user
     */
    private void verifyUserEmailAlreadyExists(Long id, UserRequest request) throws EntityAlreadyExistsException {
        var user = this.userRepository.findByEmailIgnoreCaseAndIdNot(request.email(), id);

        if (user.isPresent()) {
            throw new EntityAlreadyExistsException(
                    String.format("This user email '%s' is already in use!", request.email()));
        }
    }

}
