import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    static long index = 0;

    Map<Long, Task> taskMap = new HashMap<>();
    Map<Long, SubTask> subTaskMap = new HashMap<>();
    Map<Long, Epic> epicMap = new HashMap<>();

    /**
     * Создаем задачу
     */
    protected void createTask(String description, String name, Enum<Status> statusEnum) {
        long newIndex = increaseIntId();
        Task task = new Task(newIndex, description, name, statusEnum);
        taskMap.put(newIndex, task);
    }

    /**
     * Создаем подзадачу
     */
    protected void createSubTask(String description, String name, Enum<Status> statusEnum, Epic epicId) {
        long newIndex = increaseIntId();
        SubTask subTask = new SubTask(newIndex, description, name, statusEnum, epicId);
        subTaskMap.put(newIndex, subTask);
    }

    /**
     * Создаем эпик-задачу
     */
    protected void createEpic(String description, String name, Enum<Status> statusEnum, List<SubTask> subTaskList) {
        long newIndex = increaseIntId();
        Epic epic = new Epic(newIndex, description, name, statusEnum, subTaskList);
        epicMap.put(newIndex, epic);
    }

    /**
     * Обновляем эпик
     *
     * @param epic эпик, который надо обновить
     */
    public void updateEpic(Epic epic) {

    }

    /**
     * обновляем статусы эпик-задач
     */
    protected void updateEpicStatus(Epic epic) {

        boolean doneStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> subTask.getStatusEnum().equals(Status.DONE));

        boolean progressStatus = epic.getSubTaskList()
                .stream()
                .anyMatch(subTask -> subTask.getStatusEnum().equals(Status.IN_PROGRESS));

        if (doneStatus) {
            epic.setStatusEnum(Status.DONE);
        } else if (progressStatus) {
            epic.setStatusEnum(Status.IN_PROGRESS);
        } else {
            epic.setStatusEnum(Status.NEW);
        }
    }

    /**
     * увеличивает уникальный идентификатор
     */
    protected Long increaseIntId() {
        return index++;
    }

    /**
     * Возвразаем все задачи
     *
     * @return карты всех задач
     */
    protected Object returnAllTasks() {
        return !taskMap.isEmpty() ? taskMap : null;
    }

    /**
     * Возвращаем все подзадачи
     *
     * @return карту всех подзадач
     */
    protected Object returnAllSubTasks() {
        return !subTaskMap.isEmpty() ? subTaskMap : null;
    }

    /**
     * Возвращаем все эпики
     *
     * @return карту всех эпиков
     */
    protected Object returnAllEpics() {
        return !epicMap.isEmpty() ? epicMap : null;
    }

    /**
     * Удаляем все задачи
     */
    protected void removeAllTasks() {
        taskMap.clear();
    }

    /**
     * Удаляем все подзадачи
     */
    protected void removeAllSubTasks() {
        subTaskMap.clear();
        //TODO обновить статус эпиков
    }

    /**
     * Удаяляем всем эпики
     */
    protected void removeAllEpics() {
        epicMap.clear();
        //TODO удалить все подзадачи
    }

    /**
     * Возвращаем задачу по уникальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     * @return определенная задача
     */
    protected Task returnTaskById(long taskId) {
        return !taskMap.isEmpty() ? taskMap.get(taskId) : null;
    }

    /**
     * Возвращаем подзадачу по уникальному идентификатору
     *
     * @param subTaskId уникальный идентификатор подзадачи
     * @return определенная подзадача
     */
    protected SubTask returnSubTaskById(long subTaskId) {
        return !subTaskMap.isEmpty() ? subTaskMap.get(subTaskId) : null;
    }

    /**
     * Возвращаем эпик по уникальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     * @return определенный эпик
     */
    protected Epic returnEpicById(long epicId) {
        return !epicMap.isEmpty() ? epicMap.get(epicId) : null;
    }

    /**
     * Удаляем задачу по универсальному идентификатору
     *
     * @param taskId уникальный идентификатор задачи
     */
    protected void removeTaskById(long taskId) {
        if (!taskMap.isEmpty()) {
            taskMap.remove(taskId);
        }
    }

    /**
     * Удаляем подзадачу по универсальному идентификатору
     *
     * @param subTaskId уникальный идентификатор подзадачи
     */
    protected void removeSubTaskById(long subTaskId) {
        if (!subTaskMap.isEmpty()) {
            subTaskMap.remove(subTaskId);
        }
    }

    /**
     * Удаляем эпик по универсальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     */
    protected void removeEpicById(long epicId) {
        if (!epicMap.isEmpty()) {
            epicMap.remove(epicId);
        }
    }

    /**
     * Возвращаем список подзадач для конкретного эпика
     *
     * @param epicId уникальный идентификатор эпика
     * @return список подзадач
     */
    protected List<SubTask> returnSubTasksForEpicById(long epicId) {
        return null;
    }
}
