package com.github.rhafaelcosta.todolist.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.github.rhafaelcosta.todolist.models.Tag;

public class TagSpecification {

    public static Specification<Tag> hasName(String name) {
        return (root, query, criteriaBuilder) -> name == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

}
