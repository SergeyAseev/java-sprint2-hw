package entities;

import enums.Status;

public class SubTask extends Task {

    protected long epicId;

    public long getEpicId() {
        return epicId;
    }

    public void setEpicId(long epicId) {
        this.epicId = epicId;
    }

    public SubTask(long id, String description, String name, Enum<Status> statusEnum, long newEpicId) {
        super(id, description, name, statusEnum);
        this.epicId = newEpicId;
    }

}
