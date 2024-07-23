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

import com.github.rhafaelcosta.todolist.exceptions.EnumNotFoundException;
import com.github.rhafaelcosta.todolist.requests.TaskRequest;
import com.github.rhafaelcosta.todolist.responses.ErrorResponse;
import com.github.rhafaelcosta.todolist.responses.TaskDetailResponse;
import com.github.rhafaelcosta.todolist.responses.TaskResponse;
import com.github.rhafaelcosta.todolist.services.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Endpoints for Managing Tasks")
public class TaskController {

    private TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
	@Operation(
		summary = "Fetch all tasks",
		description = "fetches all tasks entities and their data from data source",
		responses = {
			@ApiResponse(responseCode = "200", description = "successful operation",
				content = @Content(schema = @Schema(implementation = TaskResponse.class))
			)
		}
	)
	public ResponseEntity<List<TaskResponse>> listar() {
		var tasks = taskService.listAll()
                            .stream()
                            .map(TaskResponse::new)
                            .collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

    @GetMapping("/{id}")
	@Operation(
		summary = "Fetch the task by id",
		description = "Fetches the task entity by its unique identifier from the data source.",
		responses = {
			@ApiResponse(responseCode = "200", description = "successful operation",
				content = @Content(schema = @Schema(implementation = TaskDetailResponse.class))
			),
			@ApiResponse(responseCode = "404", description = "Task not found",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<TaskDetailResponse> findById(@PathVariable Long id) throws EntityNotFoundException {
		var task = taskService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new TaskDetailResponse(task));
	}

	@PostMapping
	@Operation(
		summary = "Create a new task",
		description = "Creates a new task entity based on the provided request data.",
		responses = {
			@ApiResponse(responseCode = "201", description = "Task created successfully",
				content = @Content(schema = @Schema(implementation = TaskResponse.class))
			),
			@ApiResponse(responseCode = "409", description = "Task already exists",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<TaskResponse> insert(@RequestBody @Valid TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
		var task = this.taskService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(new TaskResponse(task));
	}

	@PutMapping("/{id}")
	@Operation(
		summary = "Update a task by id",
		description = "Updates an existing task entity identified by the given id with the provided request data.",
		responses = {
			@ApiResponse(responseCode = "200", description = "Task updated successfully",
				content = @Content(schema = @Schema(implementation = TaskResponse.class))
			),
			@ApiResponse(responseCode = "404", description = "Task not found",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
			@ApiResponse(responseCode = "409", description = "Task already exists",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody @Valid TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
		var task = this.taskService.save(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(new TaskResponse(task));
	}

	@DeleteMapping("/{id}")
	@Operation(
		summary = "Delete a task by id",
		description = "Deletes the task entity identified by the given id.",
		responses = {
			@ApiResponse(responseCode = "204", description = "Task deleted successfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "Task not found",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<String> delete(@PathVariable Long id) throws EntityNotFoundException {
		this.taskService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
