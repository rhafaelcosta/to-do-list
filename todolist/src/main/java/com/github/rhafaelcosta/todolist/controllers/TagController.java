package com.github.rhafaelcosta.todolist.controllers;

import java.util.List;

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
import com.github.rhafaelcosta.todolist.models.Tag;
import com.github.rhafaelcosta.todolist.requests.TagRequest;
import com.github.rhafaelcosta.todolist.services.TagService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;

    TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
	public List<Tag> listar() {
		return tagService.findAll();
	}

    @GetMapping("/{id}")
	public Tag findById(@PathVariable Long id) throws EntityNotFoundException {
		return tagService.findById(id);
	}

	@PostMapping
	public ResponseEntity<Tag> insert(@RequestBody @Valid TagRequest request) {
		try {
			var tag = this.tagService.save(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(tag);
		} catch (EntityAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Tag> update(@PathVariable Long id, @RequestBody @Valid TagRequest request) {
		try {

			var tag = this.tagService.save(id, request);
			return ResponseEntity.status(HttpStatus.OK).body(tag);

		} catch (EntityAlreadyExistsException | EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		try {
			this.tagService.delete(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
