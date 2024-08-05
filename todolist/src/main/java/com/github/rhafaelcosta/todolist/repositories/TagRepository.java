package com.github.rhafaelcosta.todolist.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.github.rhafaelcosta.todolist.models.Tag;

/**
* Repository interface for {@link Tag} instances.
* Provides basic CRUD operations due to the extension of {@link JpaRepository}.
* Includes custom methods to find tags.
*/
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    /**
     * Retrieves an {@link Optional} containing a {@link Tag} with the specified name, ignoring case.
     *
     * @param name the name of the tag to search for
     * @return an {@link Optional} containing the found {@link Tag}, or {@link Optional#empty()} if no tag was found
    */
    Optional<Tag> findByNameIgnoreCase(String name);

    /**
     * Retrieves a {@link Page} of {@link Tag} objects whose names contain the specified string.
     * The search is case-insensitive.
     *
     * @param name the string to search for within tag names
     * @param pageable the pagination information
     * @return a {@link Page} of {@link Tag} objects whose names contain the specified string
    */
    Page<Tag> findByNameContaining(String name, Pageable pageable);

}
