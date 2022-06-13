package manager;

import kv.KVServer;

public class Managers {

    // методы не статические, так как в противном случае очень многое ломалось. Было решено убрать данный модификатор
    // по рекомендации наставника в процессе разбора данного финального задания на вебинаре группы
    public TaskManager getDefault() {
        return new HTTPTaskManager(KVServer.PORT);
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
