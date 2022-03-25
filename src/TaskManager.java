public class TaskManager extends Manager {

    /**
     * Создаем задачу
     *
     * @return
     */
    protected Task createTask(String description, String name, Enum<Status> statusEnum) {
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
    protected void startTask(Task task) {
        task.setStatusEnum(Status.IN_PROGRESS);
    }

    /**
     * Переводим статус задачи в "сделано"
     *
     * @param task экземпляр задачи
     */
    protected void endTask(Task task) {
        task.setStatusEnum(Status.DONE);
    }

    /**
     * Обновляем задачу
     *
     * @param task
     */
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    /**
     * Возвразаем все задачи
     *
     * @return карты всех задач
     */
    protected Object returnAllTasks() {
        return !tasksMap.isEmpty() ? tasksMap : null;
    }

    /**
     * Удаляем все задачи
     */
    protected void removeAllTasks() {
        tasksMap.clear();
    }

    /**
     * Возвращаем задачу по уникальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     * @return определенная задача
     */
    protected Task returnTaskById(long taskId) {
        return !tasksMap.isEmpty() ? tasksMap.get(taskId) : null;
    }

    /**
     * Удаляем задачу по универсальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     */
    protected void removeTaskById(long taskId) {
        if (!tasksMap.isEmpty()) {
            tasksMap.remove(taskId);
        }
    }


}
