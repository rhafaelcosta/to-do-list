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
import com.github.rhafaelcosta.todolist.models.Tag;
import com.github.rhafaelcosta.todolist.requests.TagRequest;
import com.github.rhafaelcosta.todolist.responses.TagResponse;
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
	public ResponseEntity<List<TagResponse>> listar() {
		var tags = tagService.findAll()
							 .stream()
							 .map(TagResponse::new)
							 .collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(tags);
	}

    @GetMapping("/{id}")
	public ResponseEntity<TagResponse> findById(@PathVariable Long id) throws EntityNotFoundException {
		var tag = tagService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(new TagResponse(tag));
	}

	@PostMapping
	public ResponseEntity<TagResponse> insert(@RequestBody @Valid TagRequest request) throws EntityAlreadyExistsException {
		var tag = this.tagService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(new TagResponse(tag));
	}

	@PutMapping("/{id}")
	public ResponseEntity<TagResponse> update(@PathVariable Long id, @RequestBody @Valid TagRequest request) throws EntityNotFoundException, EntityAlreadyExistsException {
		var tag = this.tagService.save(id, request);
		return ResponseEntity.status(HttpStatus.OK).body(new TagResponse(tag));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) throws EntityNotFoundException {
		this.tagService.delete(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
