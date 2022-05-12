package manager;

public class Managers {

    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
