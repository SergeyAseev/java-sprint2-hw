package entities;

import enums.Status;
import enums.TaskType;

import java.time.LocalDateTime;

public class Task {

    protected Long id;

    protected String description;

    protected String name;

    protected Enum<Status> statusEnum;

    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    protected int duration;

    public Task(String description, String name, Enum<Status> statusEnum, LocalDateTime startTime, int duration) {
        this.description = description;
        this.name = name;
        this.statusEnum = statusEnum;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String description, String name, Enum<Status> statusEnum) {
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

    public Enum<Status> getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(Enum<Status> statusEnum) {
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
