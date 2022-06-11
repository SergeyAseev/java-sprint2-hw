package manager;

import entities.HTTPTaskManager;
import kv.KVServer;

public class Managers {

    public TaskManager getDefault() {
        return new HTTPTaskManager(KVServer.PORT);
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
