package com.github.rhafaelcosta.todolist.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.rhafaelcosta.todolist.enums.SeverityType;
import com.github.rhafaelcosta.todolist.enums.TaskStatusType;
import com.github.rhafaelcosta.todolist.exceptions.EnumNotFoundException;
import com.github.rhafaelcosta.todolist.models.Tag;
import com.github.rhafaelcosta.todolist.models.Task;
import com.github.rhafaelcosta.todolist.repositories.TaskRepository;
import com.github.rhafaelcosta.todolist.requests.TaskRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TaskService {

    private TagService tagService;
    private UserService userService;
    private TaskRepository taskRepository;

    public TaskService(TagService tagService, UserService userService, TaskRepository taskRepository) {
        this.tagService =  tagService;
        this.userService =  userService;
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves all tasks from the repository.
     *
     * @return a list of all tasks.
     */
    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    /**
     * Finds a task by its ID.
     *
     * @param id the ID of the task to find.
     * @return the found task.
     * @throws EntityNotFoundException if no task is found with the given ID.
     */
    public Task findById(Long id) {
        return taskRepository.findById(id)
                             .orElseThrow( () -> new EntityNotFoundException(String.format("Task not found with id: %d", id)));
    }

    /**
     * Saves a new task to the repository.
     *
     * @param request the request object containing the details of the task to save.
     * @return the saved task.
     * @throws EntityNotFoundException if a related entity (e.g., owner) is not found.
     * @throws EnumNotFoundException if an enum value provided in the request is invalid.
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
     * @param id the ID of the task to update.
     * @param request the request object containing the updated details of the task.
     * @return the updated task.
     * @throws EntityNotFoundException if the task or a related entity (e.g., owner) is not found.
     * @throws EnumNotFoundException if an enum value provided in the request is invalid.
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
     * @param id the ID of the task to update, or null if creating a new task.
     * @param request the request object containing the task details.
     * @return the task entity.
     * @throws EntityNotFoundException if a related entity (e.g., owner) is not found.
     * @throws EnumNotFoundException if an enum value provided in the request is invalid.
     */
    private Task convertTaskRequestToEntityRequest(Long id, TaskRequest request, Boolean includeTags) throws EntityNotFoundException, EnumNotFoundException {
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

        var tags =  convertAndValidateRequestToTag(request);

        task.getTags().clear();
        task.getTags().addAll(tags);

        return task;
    }

    /**
     * Converts and validates the tags in the provided TaskRequest to a list of Tag entities.
     *
     * @param request the TaskRequest containing the tags to be converted and validated
     * @return a list of Tag entities corresponding to the tags in the TaskRequest
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

}
