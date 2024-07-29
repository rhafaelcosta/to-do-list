package com.github.rhafaelcosta.todolist.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.github.rhafaelcosta.todolist.converters.SeverityTypeConverter;
import com.github.rhafaelcosta.todolist.converters.TaskStatusTypeConverter;
import com.github.rhafaelcosta.todolist.enums.SeverityType;
import com.github.rhafaelcosta.todolist.enums.TaskStatusType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TASK")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = false)
    private Integer priority;

    @Column(name = "severity_type", nullable = false)
    @Convert(converter = SeverityTypeConverter.class)
    private SeverityType severityType;

    @Column(name = "status_type", nullable = false)
    @Convert(converter = TaskStatusTypeConverter.class)
    private TaskStatusType taskStatusType;

    @CreationTimestamp
	@Column(name = "create_at")
	private LocalDateTime createAt;

	@UpdateTimestamp
	@Column(name = "update_at")
	private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TASK_TAG",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public SeverityType getSeverityType() {
        return severityType;
    }

    public void setSeverityType(SeverityType severityType) {
        this.severityType = severityType;
    }

    public TaskStatusType getTaskStatusType() {
        return taskStatusType;
    }

    public void setTaskStatusType(TaskStatusType taskStatusType) {
        this.taskStatusType = taskStatusType;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
