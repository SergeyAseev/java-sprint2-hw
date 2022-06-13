package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Epic;
import entities.SubTask;
import entities.Task;
import kv.KVTaskClient;
import manager.FileBackedTasksManager;
import manager.ManagerSaveException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient kvTaskClient;

    private final Gson gson = new Gson();

    public HTTPTaskManager(int port) {
        super();
        this.kvTaskClient = new KVTaskClient(port);
        load();
    }

    @Override
    public void save() {
        try {
            kvTaskClient.put("tasks/task", gson.toJson(tasks));
            kvTaskClient.put("tasks/epic", gson.toJson(epics));
            kvTaskClient.put("tasks/subtask", gson.toJson(subTasks));
            kvTaskClient.put("tasks/history", gson.toJson(historyManager));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public void load() {
        try {
            final String taskJson = kvTaskClient.load("tasks/task");
            tasks = gson.fromJson(taskJson, new TypeToken<HashMap<Long, Task>>() {
            }.getType());
            final String epicJson = kvTaskClient.load("tasks/epic");
            epics = gson.fromJson(epicJson, new TypeToken<HashMap<Long, Epic>>() {
            }.getType());
            final String subtaskJson = kvTaskClient.load("tasks/subtask");
            subTasks = gson.fromJson(subtaskJson, new TypeToken<HashMap<Long, SubTask>>() {
            }.getType());
            final String historyJson = kvTaskClient.load("tasks/history");
            historyManager = gson.fromJson(historyJson, new TypeToken<HashMap<List<Task>, Long>>() {
            }.getType());
        } catch (ManagerSaveException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }
}
