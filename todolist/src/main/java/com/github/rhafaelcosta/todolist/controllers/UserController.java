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
import com.github.rhafaelcosta.todolist.responses.ErrorResponse;
import com.github.rhafaelcosta.todolist.responses.UserResponse;
import com.github.rhafaelcosta.todolist.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for Managing Users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(
        summary = "Fetch all users",
        description = "fetches all users entities and their data from data source",
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation",
			    content = @Content(schema = @Schema(implementation = UserResponse.class))
		    )
        }
    )
    public ResponseEntity<List<UserResponse>> listAll() {
        var users = this.userService
                        .listar()
                        .stream()
                        .map(UserResponse::new)
                        .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Fetch the user by id",
        description = "Fetches the user entity by its unique identifier from the data source.",
        responses = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        var user = this.userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponse(user));
    }

    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user entity based on the provided request data.",
        responses = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "User already exists",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    public ResponseEntity<UserResponse> save(@RequestBody @Valid UserRequest request) throws EntityAlreadyExistsException {
        var user = this.userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a user by id",
        description = "Updates an existing user entity identified by the given id with the provided request data.",
        responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "User already exists",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    public ResponseEntity<UserResponse> save(@PathVariable Long id, @RequestBody @Valid UserRequest request) throws EntityNotFoundException, EntityAlreadyExistsException {
        var user = this.userService.save(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponse(user));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a user by id",
        description = "Deletes the user entity identified by the given id.",
        responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        }
    )
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) throws EntityNotFoundException {
        this.userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
