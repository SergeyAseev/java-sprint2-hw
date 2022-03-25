package manager;

import java.util.HashMap;
import java.util.Map;

import epic.Epic;
import epic.EpicManager;
import subTusk.SubTask;
import subTusk.SubTaskManager;
import task.Task;
import task.TaskManager;

public class Manager {

    static long index = 0;

    public static EpicManager epicManager;
    public static SubTaskManager subTaskManager;
    public static TaskManager taskManager;

    public Map<Long, Epic> epicsMap = new HashMap<>();
    public Map<Long, SubTask> subTasksMap = new HashMap<>();
    public Map<Long, Task> tasksMap = new HashMap<>();

    /**
     * увеличивает уникальный идентификатор
     */
    public Long increaseIntId() {
        return index++;
    }
}
