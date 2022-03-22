import java.util.List;

public class Epic extends Task{

    private List<SubTask> subTaskList;

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
