public class SubTask extends Task{


    private Epic epic;

    public SubTask(long id, String description, String name, Enum<Status> statusEnum, Epic newEpic) {
        super(id, description, name, statusEnum);
        this.epic = newEpic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }
}
