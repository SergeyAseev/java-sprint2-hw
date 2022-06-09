package entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.FileBackedTasksManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import kv.KVTaskClient;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient kvTaskClient;

    private final Gson gson = new Gson();

    public HTTPTaskManager(String url) throws IOException, InterruptedException {
        this.kvTaskClient = new KVTaskClient(url);
        load();
    }

    public HTTPTaskManager() {
    }

    @Override
    public void save() {
        super.save();
        try {
            kvTaskClient.put("tasks/task", gson.toJson(tasks));
            kvTaskClient.put("tasks/epic", gson.toJson(epics));
            kvTaskClient.put("tasks/subtask", gson.toJson(subTasks));
            kvTaskClient.put("tasks/history", gson.toJson(historyManager));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void load() {
        try {
            tasks = gson.fromJson(kvTaskClient.load("tasks/task"), new TypeToken<HashMap<Long, Task>>() {
            }.getType());
            tasks = gson.fromJson(kvTaskClient.load("tasks/epic"), new TypeToken<HashMap<Long, Epic>>() {
            }.getType());
            tasks = gson.fromJson(kvTaskClient.load("tasks/subtask"), new TypeToken<HashMap<Long, SubTask>>() {
            }.getType());
            tasks = gson.fromJson(kvTaskClient.load("tasks/history"), new TypeToken<HashMap<List, Long>>() {
            }.getType());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
