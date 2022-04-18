package entities;

import java.util.ArrayList;
import java.util.List;

import enums.Status;

public class Epic extends Task {

    private List<Long> subTaskList = new ArrayList<>();

    public Epic(String description, String name, Enum<Status> statusEnum) {
        super(description, name, statusEnum);
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
}
