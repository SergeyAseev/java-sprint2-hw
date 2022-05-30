package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import enums.Status;
import enums.TaskType;

public class Epic extends Task {

    private List<Long> subTaskList = new ArrayList<>();

    private LocalDateTime epicEndTime;

    public Epic(String description, String name, Enum<Status> statusEnum) {
        super(description, name, statusEnum);
    }

    public Epic(String description, String name, Enum<Status> statusEnum, LocalDateTime startTime, int duration) {
        super(description, name, statusEnum, startTime, duration);
    }

    public List<Long> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<Long> subTaskList) {
        this.subTaskList = subTaskList;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public TaskType getTaskType() {
        super.getTaskType();
        return TaskType.Epic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskList, epic.getSubTaskList());
    }

    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }
}
