package manager;

import entities.HTTPTaskManager;
import kv.KVServer;

public class Managers {

    public static TaskManager getDefault() {
        return new HTTPTaskManager(KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
