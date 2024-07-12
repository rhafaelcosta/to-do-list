package com.github.rhafaelcosta.todolist.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rhafaelcosta.todolist.exceptions.EntityAlreadyExistsException;
import com.github.rhafaelcosta.todolist.requests.UserRequest;
import com.github.rhafaelcosta.todolist.responses.UserResponse;
import com.github.rhafaelcosta.todolist.services.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> listAll() {
        var users = this.userService
                        .listar()
                        .stream()
                        .map(UserResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        var user = this.userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponse(user));
    }

    @PostMapping()
    public ResponseEntity<UserResponse> save(@RequestBody @Valid UserRequest request) throws EntityAlreadyExistsException {
        var user = this.userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> save(@PathVariable Long id, @RequestBody @Valid UserRequest request) throws EntityNotFoundException, EntityAlreadyExistsException {
        var user = this.userService.save(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) throws EntityNotFoundException {
        this.userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
