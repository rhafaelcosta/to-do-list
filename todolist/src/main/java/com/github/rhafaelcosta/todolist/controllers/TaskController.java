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
import com.github.rhafaelcosta.todolist.responses.TaskResponse;
import com.github.rhafaelcosta.todolist.services.TaskService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
	public ResponseEntity<List<TaskResponse>> listar() {
		var tasks = taskService.listAll()
                            .stream()
                            .map(TaskResponse::new)
                            .collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(tasks);
	}

    @GetMapping("/{id}")
	public ResponseEntity<TaskResponse> findById(@PathVariable Long id) throws EntityNotFoundException {
		var task = taskService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new TaskResponse(task));
	}

	@PostMapping
	public ResponseEntity<TaskResponse> insert(@RequestBody @Valid TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
		var task = this.taskService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(new TaskResponse(task));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TaskResponse> update(@PathVariable Long id, @RequestBody @Valid TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
		var task = this.taskService.save(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(new TaskResponse(task));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) throws EntityNotFoundException {
		this.taskService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
