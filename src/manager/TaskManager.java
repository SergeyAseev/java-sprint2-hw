package manager;

import entities.Task;
import enums.Status;

public interface TaskManager {

    /**
     * Создаем задачу
     *
     * @return экземпляр задачи
     */
    public Task createTask(String description, String name, Enum<Status> statusEnum);

    /**
     * Переводим статус задачи в "в процессе"
     *
     * @param task экземпляр задачи
     */
    public void startTask(Task task);

    /**
     * Переводим статус задачи в "сделано"
     *
     * @param task экземпляр задачи
     */
    public void endTask(Task task);

    /**
     * Обновляем задачу
     *
     * @param task экземпляр задачи
     */
    public void updateTask(Task task);

    /**
     * Возвразаем все задачи
     *
     * @return карты всех задач
     */
    public Object returnAllTasks();

    /**
     * Удаляем все задачи
     */
    public void removeAllTasks();

    /**
     * Возвращаем задачу по уникальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     * @return определенная задача
     */
    public Task returnTaskById(long taskId);

    /**
     * Удаляем задачу по универсальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     */
    public void removeTaskById(long taskId);


}
