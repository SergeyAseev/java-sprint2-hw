package entities;

import enums.Status;
import enums.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected Long id;

    protected String description;

    protected String name;

    protected Status statusEnum;

    protected LocalDateTime startTime;

    protected int duration;

    public Task(long id, String description, String name, Status statusEnum, LocalDateTime startTime, int duration) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.statusEnum = statusEnum;
        this.startTime = startTime;
        this.duration = duration;
    }
    public Task(String description, String name, Status statusEnum, LocalDateTime startTime, int duration) {
        this.description = description;
        this.name = name;
        this.statusEnum = statusEnum;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String description, String name, Status statusEnum) {
        this.description = description;
        this.name = name;
        this.statusEnum = statusEnum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(Status statusEnum) {
        this.statusEnum = statusEnum;
    }

    public TaskType getTaskType() {
        return TaskType.Task;
    }

    @Override
    public String toString() {
        return name;
    }

    public Long getEpicId() {
        return null;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(description, task.description)
                && Objects.equals(name, task.name)
                && Objects.equals(statusEnum, task.statusEnum)
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name, statusEnum, startTime, duration);
    }
}
