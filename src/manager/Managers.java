package manager;

import entities.HTTPTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new HTTPTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
