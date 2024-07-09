package com.github.rhafaelcosta.todolist.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.rhafaelcosta.todolist.exceptions.EntityAlreadyExistsException;
import com.github.rhafaelcosta.todolist.models.Tag;
import com.github.rhafaelcosta.todolist.repositories.TagRepository;
import com.github.rhafaelcosta.todolist.requests.TagRequest;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TagService {

    private TagRepository tagRepository;

    TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * This method provides the list of tags.
     * @return
     */
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    /**
     * This method finds the tag by ID.
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public Tag findById(Long id) throws EntityNotFoundException {
        return tagRepository.findById(id)
                            .orElseThrow( () ->
                                new EntityNotFoundException(String.format("Tag not found with id: %d", id))
                            );
    }

    /**
     * This method adds a new tag
     * @param request
     * @return
     * @throws EntityAlreadyExistsException
     */
    public Tag save(TagRequest request) throws EntityAlreadyExistsException {
        // Call the method to verify the existence of a tag with this name
        this.verifyTagNameAlreadyExists(request);

        var tag = tagRepository.save(new Tag(request.name()));
        return tag;
    }

    /**
     * This method updates the tag's information.
     * @param id
     * @param request
     * @return
     * @throws Exception
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
     * This method deletes the tag using its ID.
     * @param id
     * @throws EntityNotFoundException
     */
    public void delete(Long id) throws EntityNotFoundException {
        var tag = findById(id);
        this.tagRepository.deleteById(tag.getId());
    }

    /**
     * This method verifies whether a tag with the same name already exists.
     * @param request
     * @throws EntityAlreadyExistsException
     */
    private void verifyTagNameAlreadyExists(TagRequest request) throws EntityAlreadyExistsException {
        var tag = this.tagRepository.findByNameIgnoreCase(request.name());

        if (tag.isPresent()) {
            throw new EntityAlreadyExistsException(String.format("This tag name '%s' is already in use!", request.name()));
        }
    }

}
