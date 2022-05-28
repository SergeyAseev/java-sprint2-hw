package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected static long index = 0; // final нельзя, так как при чтении из файла мы присваиваем новый максимальный id
    protected final Map<Long, Epic> epics = new HashMap<>();
    protected final Map<Long, SubTask> subTasks = new HashMap<>();
    protected final Map<Long, Task> tasks = new HashMap<>();

    protected Set treeSet = new TreeSet<>();

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    /**
     * увеличивает уникальный идентификатор
     */
    public long increaseId() {
        return index++;
    }

    @Override
    public long createTask(Task task) {
        //сортировка + проверка пересечения времен
        checkCrossForTasks(task);
        long newTaskId = increaseId();
        task.setId(newTaskId);

        if (task.getStartTime() != null) {
            task.setEndTime(task.getStartTime().plusMinutes(task.getDuration()));
        } else {
            task.setStartTime(LocalDateTime.now());
            task.setEndTime(task.getStartTime().plusMinutes(task.getDuration()));
        }

        tasks.put(newTaskId, task);
        treeSet.add(task);
        return newTaskId;
    }

    @Override
    public long createEpic(Epic epic) {
        long newEpicId = increaseId();
        epic.setId(newEpicId);
        calculateEpicTime(epic);
        epics.put(newEpicId, epic);
        return newEpicId;
    }

    @Override
    public long createSubTask(SubTask subTask) {
        checkCrossForTasks(subTask);
        if (!epics.containsKey(subTask.getEpicId())) {
            return 0;
        }

        long newSubTaskId = increaseId();
        subTask.setId(newSubTaskId);

        if (subTask.getStartTime() != null) {
            subTask.setEndTime(subTask.getStartTime().plusMinutes(subTask.getDuration()));
        } else {
            subTask.setStartTime(LocalDateTime.now());
            subTask.setEndTime(subTask.getStartTime().plusMinutes(subTask.getDuration()));
        }
        subTasks.put(newSubTaskId, subTask);
        treeSet.add(subTask);

        long epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.getSubTaskList().add(newSubTaskId);
        calculateEpicTime(epic);

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
        Epic epic = getEpicById(subTask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public void endTask(Task task) {
        task.setStatusEnum(Status.DONE);
    }

    @Override
    public void endSubTask(SubTask subTask) {
        subTask.setStatusEnum(Status.DONE);
        Epic epic = getEpicById(subTask.getEpicId());
        updateEpicStatus(epic);
    }


    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {

        if (Objects.isNull(subTask)) {
            return;
        }

        long epicId = subTask.getEpicId();
        if (!epics.containsKey(epicId)) {
            return;
        }
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
        }

        updateEpicStatus(getEpicById(epicId));
    }

    @Override
    public void updateEpicStatus(Epic epic) {

        if (epics.isEmpty()) {
            epic.setStatusEnum(Status.NEW);
            return;
        }

        boolean doneStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> subTasks.get(subTask).getStatusEnum().equals(Status.DONE));

        boolean newStatus = epic.getSubTaskList()
                .stream()
                .allMatch(subTask -> subTasks.get(subTask).getStatusEnum().equals(Status.NEW));

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
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> returnAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> returnAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        removeAllSubTasks();
        epics.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTaskList().clear();
            epic.setStatusEnum(Status.NEW);
        }
    }

    @Override
    public Task getTaskById(long taskId) {
        Task task = tasks.get(taskId);
        historyManager.addHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(long epicId) {
        Epic epic = epics.get(epicId);
        historyManager.addHistory(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(long subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        historyManager.addHistory(subTask);
        return subTask;
    }

    @Override
    public List<Long> returnSubTasksForEpicById(long epicId) {

        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            return epic.getSubTaskList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void removeTaskById(long taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.removeHistory(taskId);
            tasks.remove(taskId);
        }
    }

    @Override
    public void removeEpicById(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Long currentId : epic.getSubTaskList()) {
                subTasks.remove(currentId);
                historyManager.removeHistory(currentId);
            }
            historyManager.removeHistory(epicId);
            epics.remove(epicId);
        }
    }


    @Override
    public void removeSubTaskById(long subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        if (subTask != null) {
            historyManager.removeHistory(subTaskId);
            subTasks.remove(subTaskId);

            Epic epic = epics.get(subTask.getEpicId());
            if (epic != null) {
                epic.getSubTaskList().remove(subTaskId);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void calculateEpicTime(Epic epic) {

        List<Long> subTaskList = epic.getSubTaskList();

        List<LocalDateTime> startTimes = new ArrayList<>();
        List<LocalDateTime> endTimes = new ArrayList<>();
        int subTaskDuration = 0;
        for (long subTaskId : subTaskList) {
            SubTask subTask = getSubTaskById(subTaskId);

            startTimes.add(subTask.getStartTime());
            endTimes.add(subTask.getEndTime());
            subTaskDuration += subTask.getDuration();
        }

        Comparator<LocalDateTime> localDateTimeComparator = LocalDateTime::compareTo;

        epic.setStartTime(startTimes.stream().min(localDateTimeComparator).orElse(null));
        epic.setEndTime(endTimes.stream().max(localDateTimeComparator).orElse(null));
        epic.setDuration(subTaskDuration);

    }

    @Override
    public Set getPrioritizedTasks() {

        List<Task> taskList = returnAllTasks();
        List<SubTask> subTaskList = returnAllSubTasks();
        treeSet= new TreeSet<>(Comparator.nullsLast(Comparator.comparing(Task::getStartTime)));

        treeSet.addAll(taskList);
        treeSet.addAll(subTaskList);

        return treeSet;
    }

    public void checkCrossForTasks(Task task) {

        Set<Task> tempTreeSet = getPrioritizedTasks();

        LocalDateTime timeStartTime = task.getStartTime();
        LocalDateTime timeEndTime = task.getStartTime().plusMinutes(task.getDuration());

        for (Task tempTask : tempTreeSet) {
            if ((timeStartTime.isAfter(tempTask.getStartTime()) && timeStartTime.isBefore(tempTask.getEndTime())) ||
                    (timeEndTime.isAfter(tempTask.getStartTime()) && timeStartTime.isBefore(tempTask.getEndTime())) ){
                throw new RuntimeException("Пересечение задач не допускается!" + tempTask.getName());
            }
        }

    }
}
