package com.github.rhafaelcosta.todolist.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.github.rhafaelcosta.todolist.models.Task;

public class TaskSpecification {

    public static Specification<Task> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) -> userId == null ? null
                : criteriaBuilder.equal(root.get("owner").get("id"), userId);
    }

    public static Specification<Task> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> title == null ? null
                : criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Task> hasPriority(Integer priority) {
        return (root, query, criteriaBuilder) -> priority == null ? null
                : criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasSeverityTypeCode(Long severityTypeCode) {
        return (root, query, criteriaBuilder) -> severityTypeCode == null ? null
                : criteriaBuilder.equal(root.get("severityType"), severityTypeCode);
    }

    public static Specification<Task> hasTaskStatusTypeCode(Long taskStatusTypeCode) {
        return (root, query, criteriaBuilder) -> taskStatusTypeCode == null ? null
                : criteriaBuilder.equal(root.get("taskStatusType"), taskStatusTypeCode);
    }

}
