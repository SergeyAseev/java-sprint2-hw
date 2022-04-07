package entities;

import java.util.ArrayList;
import java.util.List;

import enums.Status;

public class Epic extends Task {

    protected List<SubTask> subTaskList = new ArrayList<>();

    public Epic(long id, String description, String name, Enum<Status> statusEnum, List<SubTask> newSubTaskList) {
        super(id, description, name, statusEnum);

        this.subTaskList = newSubTaskList;
    }

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }
}
