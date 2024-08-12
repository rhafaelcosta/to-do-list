package com.github.rhafaelcosta.todolist.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.github.rhafaelcosta.todolist.enums.SeverityType;
import com.github.rhafaelcosta.todolist.enums.TaskStatusType;
import com.github.rhafaelcosta.todolist.exceptions.EnumNotFoundException;
import com.github.rhafaelcosta.todolist.filters.TaskFilter;
import com.github.rhafaelcosta.todolist.models.Tag;
import com.github.rhafaelcosta.todolist.models.Task;
import com.github.rhafaelcosta.todolist.repositories.TaskRepository;
import com.github.rhafaelcosta.todolist.repositories.specifications.TaskSpecification;
import com.github.rhafaelcosta.todolist.requests.TaskRequest;
import com.github.rhafaelcosta.todolist.responses.TaskResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

/**
 * Service class for managing tasks.
 */
@Service
public class TaskService {

    private TagService tagService;
    private UserService userService;
    private TaskRepository taskRepository;

    public TaskService(TagService tagService, UserService userService, TaskRepository taskRepository) {
        this.tagService = tagService;
        this.userService = userService;
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves a paginated list of all tasks based on the provided filter and pagination information.
     *
     * This method first fetches the filtered and paginated tasks, maps them to a list of {@link TaskResponse} objects,
     * and then wraps the result in a {@link PageImpl} object.
     *
     * @param filter   a {@link TaskFilter} object containing filtering criteria. If null, no filters are applied.
     * @param pageable a {@link Pageable} object containing pagination information (page number, size, and sorting).
     * @return a {@link Page} of {@link TaskResponse} objects representing the requested page of tasks.
     */
    public Page<TaskResponse> getPaginatedTasksByFilter(TaskFilter filter, Pageable pageable) {
        // Get the filtered and paginated list of tasks
        var data =  findTasksByFilter(filter, pageable);

        // Convert each task entity to a TaskResponse DTO
        var tasks = data.stream().map(TaskResponse::new).collect(Collectors.toList());

        // Count the total number of tasks matching the filter criteria
        var totalOfTask = countTasksByFilter(filter);

        return new PageImpl<>(tasks, pageable, totalOfTask);
    }

    /**
     * Counts the total number of tasks that match the given filter criteria.
     *
     * If the filter is null, this method returns the total count of all tasks in the repository.
     * Otherwise, it applies the filtering criteria specified in the {@link TaskFilter} object.
     *
     * @param filter a {@link TaskFilter} object containing filtering criteria. If null, no filters are applied.
     * @return the total number of tasks that match the filtering criteria.
     */
    public Long countTasksByFilter(TaskFilter filter) {

        if (filter == null) {
            return taskRepository.count();
        }

        Specification<Task> specification = Specification.where(TaskSpecification.hasUserId(filter.userId()))
                .and(TaskSpecification.hasTitle(filter.title()))
                .and(TaskSpecification.hasPriority(filter.priority()))
                .and(TaskSpecification.hasSeverityTypeCode(filter.severityTypeCode()))
                .and(TaskSpecification.hasTaskStatusTypeCode(filter.taskStatusTypeCode()));

        return taskRepository.count(specification);
    }

    /**
     * Finds a task by its ID.
     *
     * @param id the ID of the task to find.
     * @return the found {@link Task} object.
     * @throws EntityNotFoundException if no task is found with the given ID.
     */
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Task not found with id: %d", id)));
    }

    /**
     * Saves a new task to the repository.
     *
     * @param request the request object containing the details of the task to save.
     * @return the saved {@link Task} object.
     * @throws EntityNotFoundException if a related entity (e.g., owner) is not found.
     * @throws EnumNotFoundException   if an enum value provided in the request is invalid.
     */
    @Transactional
    public Task save(TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
        var task = convertTaskRequestToEntityRequest(null, request, true);

        taskRepository.save(task);
        return task;
    }

    /**
     * Updates an existing task in the repository.
     *
     * @param id      the ID of the task to update.
     * @param request the request object containing the updated details of the task.
     * @return the updated {@link Task} object.
     * @throws EntityNotFoundException if the task or a related entity (e.g., owner) is not found.
     * @throws EnumNotFoundException   if an enum value provided in the request is invalid.
     */
    @Transactional
    public Task save(Long id, TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
        var task = convertTaskRequestToEntityRequest(id, request, false);

        taskRepository.save(task);
        return task;
    }

    /**
     * This method deletes the task using its ID.
     *
     * @param id the ID of the task to delete.
     * @throws EntityNotFoundException if no task is found with the given ID.
     */
    public void delete(Long id) throws EntityNotFoundException {
        var tag = findById(id);
        this.taskRepository.deleteById(tag.getId());
    }

    /**
     * Converts a TaskRequest object to a Task entity.
     *
     * @param id      the ID of the task to update, or null if creating a new task.
     * @param request the request object containing the task details.
     * @return the {@link Task}  entity.
     * @throws EntityNotFoundException if a related entity (e.g., owner) is not found.
     * @throws EnumNotFoundException   if an enum value provided in the request is invalid.
     */
    private Task convertTaskRequestToEntityRequest(Long id, TaskRequest request, Boolean includeTags)
            throws EntityNotFoundException, EnumNotFoundException {
        Task task = new Task();
        task.setTags(new ArrayList<>());

        if (id != null) {
            // Call the method to verify the existence of a task
            task = findById(id);
        }

        var owner = userService.findById(request.userId());

        task.setOwner(owner);
        task.setTitle(request.title());
        task.setPriority(request.priority());
        task.setDescription(request.description());
        task.setSeverityType(SeverityType.getSeverityTypeByCode(request.severityType()));
        task.setTaskStatusType(TaskStatusType.getTaskStatusTypeByCode(request.taskStatusType()));

        var tags = convertAndValidateRequestToTag(request);

        task.getTags().clear();
        task.getTags().addAll(tags);

        return task;
    }

    /**
     * Converts and validates the tags in the provided TaskRequest to a list of Tag
     * entities.
     *
     * @param request the TaskRequest containing the tags to be converted and validated
     * @return a list of {@link Task} entities corresponding to the tags in the TaskRequest
     * @throws EntityNotFoundException if any tag ID in the TaskRequest is not found
     */
    private List<Tag> convertAndValidateRequestToTag(TaskRequest request) throws EntityNotFoundException {
        var tags = new ArrayList<Tag>();

        request.tags().forEach(t -> {
            var tag = tagService.findById(t.id());
            tags.add(tag);
        });

        return tags;
    }

    /**
     * Retrieves a paginated list of tasks that match the given filter criteria.
     *
     * If the filter is null, this method returns a paginated list of all tasks.
     * Otherwise, it applies the filtering criteria specified in the {@link TaskFilter} object and returns the matching tasks.
     *
     * @param filter   a {@link TaskFilter} object containing filtering criteria. If null, no filters are applied.
     * @param pageable a {@link Pageable} object containing pagination information (page number, size, and sorting).
     * @return a {@link Page} of {@link Task} objects representing the filtered and paginated tasks.
     */
    private Page<Task> findTasksByFilter(TaskFilter filter, Pageable pageable) {
        if (filter == null) {
            return taskRepository.findAll(pageable);
        }

        // Create a specification based on the provided filter
        Specification<Task> specification = Specification.where(TaskSpecification.hasUserId(filter.userId()))
                .and(TaskSpecification.hasTitle(filter.title()))
                .and(TaskSpecification.hasPriority(filter.priority()))
                .and(TaskSpecification.hasSeverityTypeCode(filter.severityTypeCode()))
                .and(TaskSpecification.hasTaskStatusTypeCode(filter.taskStatusTypeCode()));

        return taskRepository.findAll(specification, pageable);
    }

}
