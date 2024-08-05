package com.github.rhafaelcosta.todolist.services;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.rhafaelcosta.todolist.exceptions.EntityAlreadyExistsException;
import com.github.rhafaelcosta.todolist.models.Tag;
import com.github.rhafaelcosta.todolist.repositories.TagRepository;
import com.github.rhafaelcosta.todolist.repositories.specifications.TagSpecification;
import com.github.rhafaelcosta.todolist.requests.TagRequest;
import com.github.rhafaelcosta.todolist.responses.TagResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TagService {

    private TagRepository tagRepository;

    TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Retrieves a paginated list of all tags.
     *
     * This method uses the {@link Pageable} interface to enable pagination of the result set.
     * The {@link Pageable} object should contain information about the page number, page size, and sorting options.
     *
     * @param pageable a {@link Pageable} object containing pagination information
     * @return a {@link Page} of {@link Tag} objects representing the requested page of tags
     */
    public Page<TagResponse> getPaginatedTagsByFilter(String name, Pageable pageable) {
        Page<Tag> tagPagination;
        if (name != null && !name.isEmpty()) {
            tagPagination = tagRepository.findByNameContaining(name, pageable);
        } else {
            tagPagination = tagRepository.findAll(pageable);
        }

        var tags = tagPagination.stream().map(TagResponse::new).collect(Collectors.toList());
        var totalOfTags = countTagsByFilter(name);

        return new PageImpl<>(tags, pageable, totalOfTags);
    }

    /**
     * Counts the total number of tags that match the given name filter.
     *
     * If the name filter is null or empty, this method returns the total count of all tags in the repository.
     * Otherwise, it applies the filtering criteria based on the tag name.
     *
     * @param name the name of the tag to filter by. If null or empty, no filters are applied.
     * @return the total number of tags that match the given name filter, or the total count of all tags if no filter is applied.
     */
    public Long countTagsByFilter(String name) {
        if (name == null || name.isEmpty()) {
            return tagRepository.count();
        }

        Specification<Tag> specification = Specification.where(TagSpecification.hasName(name));
        return tagRepository.count(specification);
    }

    /**
     * Finds a tag by its ID.
     *
     * @param id the ID of the tag to be found
     * @return the found {@link Tag} object
     * @throws EntityNotFoundException if no tag is found with the given ID
     */
    public Tag findById(Long id) throws EntityNotFoundException {
        return tagRepository.findById(id)
                            .orElseThrow( () ->
                                new EntityNotFoundException(String.format("Tag not found with id: %d", id))
                            );
    }

    /**
     * Saves a new tag.
     *
     * @param request the request object containing the name of the tag to be saved
     * @return the saved {@link Tag} object
     * @throws EntityAlreadyExistsException if a tag with the same name already exists
     */
    public Tag save(TagRequest request) throws EntityAlreadyExistsException {
        // Call the method to verify the existence of a tag with this name
        this.verifyTagNameAlreadyExists(request);

        var tag = tagRepository.save(new Tag(request.name()));
        return tag;
    }

    /**
     * Updates an existing tag.
     *
     * @param id the ID of the tag to be updated
     * @param request the request object containing the new name of the tag
     * @return the updated {@link Tag} object
     * @throws EntityNotFoundException if no tag is found with the given ID
     * @throws EntityAlreadyExistsException if a tag with the new name already exists
    */
    public Tag save(Long id, TagRequest request) throws EntityNotFoundException, EntityAlreadyExistsException {
        // Call the method to verify the existence of a tag
        var tag = findById(id);
        // Call the method to verify the existence of a tag with this name
        this.verifyTagNameAlreadyExists(request);

        tag.setName(request.name());
        tagRepository.save(tag);

        return tag;
    }

    /**
     * Deletes a tag by its ID.
     *
     * @param id the ID of the tag to be deleted
     * @throws EntityNotFoundException if no tag is found with the given ID
    */
    public void delete(Long id) throws EntityNotFoundException {
        var tag = findById(id);
        this.tagRepository.deleteById(tag.getId());
    }

    /**
     * Verifies if a tag with the same name already exists.
     *
     * @param request the request object containing the name to be checked
     * @throws EntityAlreadyExistsException if a tag with the same name already exists
    */
    private void verifyTagNameAlreadyExists(TagRequest request) throws EntityAlreadyExistsException {
        var tag = this.tagRepository.findByNameIgnoreCase(request.name());

        if (tag.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("This tag name '%s' is already in use!", request.name()));
        }
    }

}
