package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

public class InMemoryTaskManager implements TaskManager{

    static long index = 0;
    static List<Task> historyList = new ArrayList<>();
    final static int DEEP_OF_HISTORY = 11;

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

    /**
     * Создаем задачу
     *
     * @return экземпляр задачи
     */
    @Override
    public Task createTask(String description, String name, Enum<Status> statusEnum) {
        long newIndex = increaseIntId();
        Task task = new Task(newIndex, description, name, statusEnum);
        tasksMap.put(newIndex, task);
        return task;
    }

    /**
     * Переводим статус задачи в "в процессе"
     *
     * @param task экземпляр задачи
     */
    @Override
    public void startTask(Task task) {
        task.setStatusEnum(Status.IN_PROGRESS);
    }

    /**
     * Переводим статус задачи в "сделано"
     *
     * @param task экземпляр задачи
     */
    @Override
    public void endTask(Task task) {
        task.setStatusEnum(Status.DONE);
    }

    /**
     * Обновляем задачу
     *
     * @param task экземпляр задачи
     */
    @Override
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    /**
     * Возвразаем все задачи
     *
     * @return карты всех задач
     */
    @Override
    public Object returnAllTasks() {
        return !tasksMap.isEmpty() ? tasksMap : null;
    }

    /**
     * Удаляем все задачи
     */
    @Override
    public void removeAllTasks() {
        tasksMap.clear();
    }

    /**
     * Возвращаем задачу по уникальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     * @return определенная задача
     */
    @Override
    public Task returnTaskById(long taskId) {
        if (!tasksMap.isEmpty()) {
            getHistory();
            historyList.add(tasksMap.get(taskId));
            return tasksMap.get(taskId);
        } else {
            return null;
        }
    }

    /**
     * Удаляем задачу по универсальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     */
    @Override
    public void removeTaskById(long taskId) {
        if (!tasksMap.isEmpty()) {
            tasksMap.remove(taskId);
        }
    }

    public List<Task> getHistory() {
        if (historyList.size() == DEEP_OF_HISTORY) {
            historyList.remove(0);
        }

        return historyList;
    }
}
