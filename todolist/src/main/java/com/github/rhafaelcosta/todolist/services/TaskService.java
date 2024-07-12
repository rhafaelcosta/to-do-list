package com.github.rhafaelcosta.todolist.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.rhafaelcosta.todolist.enums.SeverityType;
import com.github.rhafaelcosta.todolist.enums.TaskStatusType;
import com.github.rhafaelcosta.todolist.exceptions.EnumNotFoundException;
import com.github.rhafaelcosta.todolist.models.Task;
import com.github.rhafaelcosta.todolist.repositories.TaskRepository;
import com.github.rhafaelcosta.todolist.requests.TaskRequest;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

    private UserService userService;
    private TaskRepository taskRepository;

    public TaskService(UserService userService, TaskRepository taskRepository) {
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
    public Task save(TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
        var task = convertTaskRequestToEntityRequest(null, request);

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
    public Task save(Long id, TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
        var task = convertTaskRequestToEntityRequest(id, request);

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
    private Task convertTaskRequestToEntityRequest(Long id, TaskRequest request) throws EntityNotFoundException, EnumNotFoundException {
        var task = new Task();

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

        return task;
    }

}
