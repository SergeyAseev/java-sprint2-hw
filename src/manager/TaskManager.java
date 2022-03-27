package manager;

import entities.Task;
import enums.Status;

public class TaskManager extends Manager {

    /**
     * Создаем задачу
     *
     * @return экземпляр задачи
     */
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
    public void startTask(Task task) {
        task.setStatusEnum(Status.IN_PROGRESS);
    }

    /**
     * Переводим статус задачи в "сделано"
     *
     * @param task экземпляр задачи
     */
    public void endTask(Task task) {
        task.setStatusEnum(Status.DONE);
    }

    /**
     * Обновляем задачу
     *
     * @param task экземпляр задачи
     */
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    /**
     * Возвразаем все задачи
     *
     * @return карты всех задач
     */
    public Object returnAllTasks() {
        return !tasksMap.isEmpty() ? tasksMap : null;
    }

    /**
     * Удаляем все задачи
     */
    public void removeAllTasks() {
        tasksMap.clear();
    }

    /**
     * Возвращаем задачу по уникальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     * @return определенная задача
     */
    public Task returnTaskById(long taskId) {
        return !tasksMap.isEmpty() ? tasksMap.get(taskId) : null;
    }

    /**
     * Удаляем задачу по универсальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     */
    public void removeTaskById(long taskId) {
        if (!tasksMap.isEmpty()) {
            tasksMap.remove(taskId);
        }
    }


}
