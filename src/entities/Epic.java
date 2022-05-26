package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
}
