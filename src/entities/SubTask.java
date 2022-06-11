package entities;

import enums.Status;
import enums.TaskType;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {

    private long epicId;

    @Override
    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }

    public SubTask(String description, String name, Status statusEnum, long newEpicId) {
        super(description, name, statusEnum);
        this.epicId = newEpicId;
    }

    public SubTask(String description, String name, Status statusEnum, long newEpicId,
                   LocalDateTime startTime, int duration) {
        super(description, name, statusEnum, startTime, duration);
        this.epicId = newEpicId;
    }

    @Override
    public TaskType getTaskType() {
        super.getTaskType();
        return TaskType.SubTask;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.getEpicId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

}
