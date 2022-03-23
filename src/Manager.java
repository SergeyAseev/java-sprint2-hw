import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager {

    static long index = 0;

    Map<Long, Task> tasksMap = new HashMap<>();
    Map<Long, SubTask> subTasksMap = new HashMap<>();
    Map<Long, Epic> epicsMap = new HashMap<>();

    /**
     * Создаем задачу
     * @return
     */
    protected Task createTask(String description, String name, Enum<Status> statusEnum) {
        long newIndex = increaseIntId();
        Task task = new Task(newIndex, description, name, statusEnum);
        tasksMap.put(newIndex, task);
        return task;
    }

    /**
     * Создаем подзадачу
     * @return
     */
    protected SubTask createSubTask(String description, String name, Enum<Status> statusEnum, long epicId) {
        long newIndex = increaseIntId();
        SubTask subTask = new SubTask(newIndex, description, name, statusEnum, epicId);
        subTasksMap.put(newIndex, subTask);
        return subTask;
    }

    /**
     * Создаем эпик-задачу
     * @return
     */
    protected Epic createEpic(String description, String name, Enum<Status> statusEnum, List<SubTask> subTaskList) {
        long newIndex = increaseIntId();
        Epic epic = new Epic(newIndex, description, name, statusEnum, subTaskList);
        epicsMap.put(newIndex, epic);
        return epic;
    }

    /**
     * Обновляем эпик
     *
     * @param epic эпик, который надо обновить
     */
    public void updateEpic(Epic epic) {
        epicsMap.put(increaseIntId(), epic);
    }

    /**
     * Обновляем подзадачу
     * @param subTask
     */
    public void updateSubTask(SubTask subTask) {

    }

    /**
     * Обновляем задачу
     * @param task
     */
    public void updateTask(Task task) {
        tasksMap.put(increaseIntId(), task);
    }

    /**
     * обновляем статусы эпик-задач
     */
    protected void updateEpicStatus(Epic epic) {

        if (epicsMap.isEmpty()) {
            epic.setStatusEnum(Status.NEW);
            return;
        }

        boolean doneStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> subTask.getStatusEnum().equals(Status.DONE));

        boolean newStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> subTask.getStatusEnum().equals(Status.NEW));

        boolean progressStatus = epic.getSubTaskList()
                .stream()
                .anyMatch(subTask -> subTask.getStatusEnum().equals(Status.IN_PROGRESS));

        if (doneStatus) {
            epic.setStatusEnum(Status.DONE);
        } else if (newStatus) {
            epic.setStatusEnum(Status.NEW);
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
        return !tasksMap.isEmpty() ? tasksMap : null;
    }

    /**
     * Возвращаем все подзадачи
     *
     * @return карту всех подзадач
     */
    protected Object returnAllSubTasks() {
        return !subTasksMap.isEmpty() ? subTasksMap : null;
    }

    /**
     * Возвращаем все эпики
     *
     * @return карту всех эпиков
     */
    protected Object returnAllEpics() {
        return !epicsMap.isEmpty() ? epicsMap : null;
    }

    /**
     * Удаляем все задачи
     */
    protected void removeAllTasks() {
        tasksMap.clear();
    }

    /**
     * Удаляем все подзадачи
     */
    protected void removeAllSubTasks() {
        subTasksMap.clear();

        for (Map.Entry<Long, Epic> epic: epicsMap.entrySet()) {
            epic.getValue().setStatusEnum(Status.NEW);
        }
    }

    /**
     * Удаляем все эпики и их подзадачи
     */
    protected void removeAllEpics() {

        removeAllSubTasks();
        epicsMap.clear();
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
     * Возвращаем подзадачу по уникальному идентификатору
     *
     * @param subTaskId уникальный идентификатор подзадачи
     * @return определенная подзадача
     */
    protected SubTask returnSubTaskById(long subTaskId) {
        return !subTasksMap.isEmpty() ? subTasksMap.get(subTaskId) : null;
    }

    /**
     * Возвращаем эпик по уникальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     * @return определенный эпик
     */
    protected Epic returnEpicById(long epicId) {
        return !epicsMap.isEmpty() ? epicsMap.get(epicId) : null;
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

    /**
     * Удаляем подзадачу по универсальному идентификатору
     *
     * @param subTaskId уникальный идентификатор подзадачи
     */
    protected void removeSubTaskById(long subTaskId) {
        if (!subTasksMap.isEmpty()) {
            subTasksMap.remove(subTaskId);
        }
    }

    /**
     * Удаляем эпик по универсальному идентификатору
     *
     * @param epicId уникальный идентификатор эпика
     */
    protected void removeEpicById(long epicId) {
        if (!epicsMap.isEmpty()) {
            epicsMap.remove(epicId);
        }
    }

    /**
     * Возвращаем список подзадач для конкретного эпика
     *
     * @param epicId уникальный идентификатор эпика
     * @return список подзадач
     */
    protected List<SubTask> returnSubTasksForEpicById(long epicId) {

        if (!epicsMap.isEmpty()) {
            if (epicsMap.containsKey(epicId)) {
                Epic epic = epicsMap.get(epicId);

                String name = epic.getName();

                return epic.getSubTaskList();
            }
        }
        return null;
    }
}
