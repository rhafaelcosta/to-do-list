package com.github.rhafaelcosta.todolist.controllers;

import com.github.rhafaelcosta.todolist.exceptions.EntityAlreadyExistsException;
import com.github.rhafaelcosta.todolist.requests.TagRequest;
import com.github.rhafaelcosta.todolist.responses.ErrorResponse;
import com.github.rhafaelcosta.todolist.responses.TagResponse;
import com.github.rhafaelcosta.todolist.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tags")
@Tag(name = "Tags", description = "Endpoints for Managing Tags")
public class TagController {

    private final TagService tagService;

    TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation(
            summary = "Fetch all tags",
            description = "fetches all tags entities and their data from data source",
            responses = {
                    @ApiResponse(responseCode = "200", description = "successful operation",
                            content = @Content(schema = @Schema(implementation = TagResponse.class))
                    )
            }
    )
    public ResponseEntity<Page<TagResponse>> listAll(@RequestParam(required = false) String name, Pageable pageable) {
        var tags = tagService.getPaginatedTagsByFilter(name, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Fetch the tag by id",
            description = "Fetches the tag entity by its unique identifier from the data source.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "successful operation",
                            content = @Content(schema = @Schema(implementation = TagResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Tag not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<TagResponse> findById(@PathVariable Long id) throws EntityNotFoundException {
        var tag = tagService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new TagResponse(tag));
    }

    @PostMapping
    @Operation(
            summary = "Create a new tag",
            description = "Creates a new tag entity based on the provided request data.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tag created successfully",
                            content = @Content(schema = @Schema(implementation = TagResponse.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Tag already exists",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<TagResponse> insert(@RequestBody @Valid TagRequest request) throws EntityAlreadyExistsException {
        var tag = this.tagService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new TagResponse(tag));
    }

    @PutMapping(value = "/{id}")
    @Operation(
            summary = "Update a tag by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tag updated successfully",
                            content = @Content(schema = @Schema(implementation = TagResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Tag not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Tag already exists",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<TagResponse> update(@PathVariable Long id, @RequestBody @Valid TagRequest request) throws EntityNotFoundException, EntityAlreadyExistsException {
        var tag = this.tagService.save(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(new TagResponse(tag));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a tag by id",
            description = "Deletes the tag entity identified by the given id.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tag deleted successfully", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Tag not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public ResponseEntity<String> delete(@PathVariable Long id) throws EntityNotFoundException {
        this.tagService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
