package entities;

import manager.FileBackedTasksManager;

import java.io.IOException;

import kv.KVTaskClient;
public class HTTPTaskManager extends FileBackedTasksManager {
    int port;

    KVTaskClient kvTaskClient;

    {
        try {
            kvTaskClient = new KVTaskClient();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HTTPTaskManager(int port) {
        this.port = port;
    }

    public HTTPTaskManager() {
    }

    @Override
    public void save() {
        super.save();
        //kvTaskClient.put(String key, String json);
    }

    @Override
    public void load() {
        super.load();
        //kvTaskClient.load();
    }
}
