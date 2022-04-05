package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager{

    static long index = 0;
    public Map<Long, Epic> epicsMap = new HashMap<>();
    public Map<Long, SubTask> subTasksMap = new HashMap<>();
    public Map<Long, Task> tasksMap = new HashMap<>();
    //TaskManager manager = Managers.getDefault();
    InMemoryHistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    /**
     * увеличивает уникальный идентификатор
     */
    public Long increaseIntId() {
        return index++;
    }


    @Override
    public Task createTask(String description, String name, Enum<Status> statusEnum) {
        long newIndex = increaseIntId();
        Task task = new Task(newIndex, description, name, statusEnum);
        tasksMap.put(newIndex, task);
        return task;
    }

    @Override
    public Epic createEpic(String description, String name, Enum<Status> statusEnum, List<SubTask> subTaskList) {
        long newIndex = increaseIntId();
        Epic epic = new Epic(newIndex, description, name, statusEnum, subTaskList);
        epicsMap.put(newIndex, epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(String description, String name, Enum<Status> statusEnum, long epicId) {
        long newIndex = increaseIntId();
        SubTask subTask = new SubTask(newIndex, description, name, statusEnum, epicId);
        subTasksMap.put(newIndex, subTask);
        return subTask;
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
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        subTasksMap.put(subTask.getId(), subTask);
    }

    @Override
    public void updateEpicStatus(Epic epic) {

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

        if (doneStatus) {
            epic.setStatusEnum(Status.DONE);
        } else if (newStatus) {
            epic.setStatusEnum(Status.NEW);
        } else {
            epic.setStatusEnum(Status.IN_PROGRESS);
        }
    }

    @Override
    public Object returnAllTasks() {
        return !tasksMap.isEmpty() ? tasksMap : null;
    }

    @Override
    public Object returnAllEpics() {
        return !epicsMap.isEmpty() ? epicsMap : null;
    }

    @Override
    public Object returnAllSubTasks() {
        return !subTasksMap.isEmpty() ? subTasksMap : null;
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
            inMemoryHistoryManager.getHistory();
            inMemoryHistoryManager.historyList.add(tasksMap.get(taskId));
            return tasksMap.get(taskId);
        } else {
            return null;
        }
    }

    @Override
    public Epic returnEpicById(long epicId) {

        if (!epicsMap.isEmpty()) {
            inMemoryHistoryManager.getHistory();
            inMemoryHistoryManager.historyList.add(epicsMap.get(epicId));
            return epicsMap.get(epicId);
        } else {
            return null;
        }
    }

    @Override
    public SubTask returnSubTaskById(long subTaskId) {
        if (!subTasksMap.isEmpty()) {
            inMemoryHistoryManager.getHistory();
            inMemoryHistoryManager.historyList.add(subTasksMap.get(subTaskId));
            return subTasksMap.get(subTaskId);
        } else {
            return null;
        }
    }

    @Override
    public List<SubTask> returnSubTasksForEpicById(long epicId) {
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
}
