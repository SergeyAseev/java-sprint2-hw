package entities;

import enums.Status;

public class Task {

    protected Long id;

    protected String description;

    protected String name;

    protected Enum<Status> statusEnum;

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

    @Override
    public String toString() {
        return name;
    }
}
