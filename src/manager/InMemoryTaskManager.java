package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private static long index = 0;
    private Map<Long, Epic> epicsMap = new HashMap<>();
    private Map<Long, SubTask> subTasksMap = new HashMap<>();
    private Map<Long, Task> tasksMap = new HashMap<>();

    private HistoryManager historyManager = Managers.getDefaultHistory();

    /**
     * увеличивает уникальный идентификатор
     */
    public Long increaseIntId() {
        return index++;
    }


    @Override
    public long createTask(Task task) {
        long newTaskId = increaseIntId();
        task.setId(newTaskId);
        tasksMap.put(newTaskId, task);
        return newTaskId;
    }

    @Override
    public long createEpic(Epic epic) {
        long newEpicId = increaseIntId();
        epic.setId(newEpicId);
        epicsMap.put(newEpicId, epic);
        return newEpicId;
    }

    @Override
    public long createSubTask(SubTask subTask) {

        if (!epicsMap.containsKey(subTask.getEpicId())) {
            return 0;
        }

        long newSubTaskId = increaseIntId();
        subTask.setId(newSubTaskId);
        subTasksMap.put(newSubTaskId, subTask);

        long epicId = subTask.getEpicId();
        Epic epic = epicsMap.get(epicId);
        epic.getSubTaskList().add(newSubTaskId);

        updateEpicStatus(epic);
        return newSubTaskId;
    }


    @Override
    public void startTask(Task task) {
        task.setStatusEnum(Status.IN_PROGRESS);
    }

    @Override
    public void startSubTask(SubTask subTask) {
        subTask.setStatusEnum(Status.IN_PROGRESS);
        Epic epic = returnEpicById(subTask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public void endTask(Task task) {
        task.setStatusEnum(Status.DONE);
    }

    @Override
    public void endSubTask(SubTask subTask) {
        subTask.setStatusEnum(Status.DONE);
        Epic epic = returnEpicById(subTask.getEpicId());
        updateEpicStatus(epic);
    }


    @Override
    public void updateTask(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {

        if (Objects.isNull(subTask)) {
            return;
        }

        long epicId = subTask.getEpicId();
        if (!epicsMap.containsKey(epicId)) {
            return;
        }
        if (subTasksMap.containsKey(subTask.getId())) {
            subTasksMap.put(subTask.getId(), subTask);
        }

        updateEpicStatus(returnEpicById(epicId));
    }

    @Override
    public void updateEpicStatus(Epic epic) {

        if (epicsMap.isEmpty()) {
            epic.setStatusEnum(Status.NEW);
            return;
        }

        boolean doneStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> returnSubTaskById(subTask).getStatusEnum().equals(Status.DONE));

        boolean newStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> returnSubTaskById(subTask).getStatusEnum().equals(Status.NEW));

        if (doneStatus) {
            epic.setStatusEnum(Status.DONE);
        } else if (newStatus) {
            epic.setStatusEnum(Status.NEW);
        } else {
            epic.setStatusEnum(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> returnAllTasks() {
        return !tasksMap.isEmpty() ? new ArrayList<>(tasksMap.values()) : new ArrayList<>();
    }

    @Override
    public List<Epic> returnAllEpics() {
        return !epicsMap.isEmpty() ? new ArrayList<>(epicsMap.values()) : new ArrayList<>();
    }

    @Override
    public List<SubTask> returnAllSubTasks() {
        return !subTasksMap.isEmpty() ? new ArrayList<>(subTasksMap.values()) : new ArrayList<>();
    }

    @Override
    public void removeAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void removeAllEpics() {
        removeAllSubTasks();
        epicsMap.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasksMap.clear();

        for (Map.Entry<Long, Epic> epic : epicsMap.entrySet()) {
            epic.getValue().setStatusEnum(Status.NEW);
        }
    }

    @Override
    public Task returnTaskById(long taskId) {
        if (!tasksMap.isEmpty()) {
            Task task = tasksMap.get(taskId);
            historyManager.addTask(task);
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Epic returnEpicById(long epicId) {

        if (!epicsMap.isEmpty()) {
            Epic epic = epicsMap.get(epicId);
            historyManager.addTask(epic);
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public SubTask returnSubTaskById(long subTaskId) {
        if (!subTasksMap.isEmpty()) {
            SubTask subTask = subTasksMap.get(subTaskId);
            historyManager.addTask(subTask);
            return subTask;
        } else {
            return null;
        }
    }

    @Override
    public List<Long> returnSubTasksForEpicById(long epicId) {
        if (!epicsMap.isEmpty()) {
            if (epicsMap.containsKey(epicId)) {
                Epic epic = epicsMap.get(epicId);
                return epic.getSubTaskList();
            }
        }
        return null;
    }

    @Override
    public void removeTaskById(long taskId) {
        if (!tasksMap.isEmpty()) {
            tasksMap.remove(taskId);
        }
    }

    @Override
    public void removeEpicById(long epicId) {
        if (!epicsMap.isEmpty()) {
            epicsMap.remove(epicId);
        }
    }

    @Override
    public void removeSubTaskById(long subTaskId) {
        if (!subTasksMap.isEmpty()) {
            subTasksMap.remove(subTaskId);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
