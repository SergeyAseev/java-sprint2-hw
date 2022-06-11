package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected long index = 0;
    protected Map<Long, Epic> epics = new HashMap<>();
    protected Map<Long, SubTask> subTasks = new HashMap<>();
    protected Map<Long, Task> tasks = new HashMap<>();

    protected Set<Task> treeSet = new TreeSet<>(Comparator.nullsLast(Comparator.comparing(Task::getStartTime)));
    Managers managers = new Managers();
    protected HistoryManager historyManager = managers.getDefaultHistory();

    /**
     * увеличивает уникальный идентификатор
     */
    public long increaseId() {
        return index++;
    }

    @Override
    public long createTask(Task task) {
        checkCrossForTasks(task);
        long newTaskId = increaseId();
        task.setId(newTaskId);

        if (tasks == null) {
            tasks = new HashMap<>();
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

        if (epics == null) {
            epics = new HashMap<>();
        }
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

        if (subTasks == null) {
            subTasks = new HashMap<>();
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
    public void updateTask(Task newTask) {

        try {
            checkCrossForTasks(newTask);
            if (!tasks.containsKey(newTask.getId())) {
                return;
            }
            Task oldTask = tasks.get(newTask.getId());
            treeSet.remove(oldTask); //удаляем старую задачу
            tasks.put(oldTask.getId(), newTask); //записываем новую задачу, сохраняя старый ID согласно ТЗ
            treeSet.add(newTask);
        } catch (RuntimeException e) {
            System.out.println("Невозможно обновить задачу: " + newTask.getId());
        }

    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {

        if (Objects.isNull(newSubTask)) {
            return;
        }

        long epicId = newSubTask.getEpicId();
        try {
            if (!epics.containsKey(epicId)) {
                return;
            }
            checkCrossForTasks(newSubTask);
            SubTask oldSubTask = subTasks.get(newSubTask.getId());
            oldSubTask.setName(newSubTask.getName());
            oldSubTask.setDescription(newSubTask.getDescription());
            oldSubTask.setStatusEnum(newSubTask.getStatusEnum());
            oldSubTask.setStartTime(newSubTask.getStartTime());
            oldSubTask.setDuration(newSubTask.getDuration());
            Epic updatedEpic = getEpicById(newSubTask.getEpicId());
            calculateEpicTime(updatedEpic);
            treeSet.add(newSubTask);

            if (subTasks.containsKey(newSubTask.getId())) {
                subTasks.put(newSubTask.getId(), newSubTask);
            }
            updateEpicStatus(getEpicById(epicId));
        } catch (RuntimeException e) {
            System.out.println("Невозможно обновить подзадачу: " + newSubTask.getId());
        }
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
        if (historyManager == null) {
            historyManager = new Managers().getDefaultHistory();
        }
        historyManager.addHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(long epicId) {
        Epic epic = epics.get(epicId);
        if (historyManager == null) {
            historyManager = new Managers().getDefaultHistory();
        }
        historyManager.addHistory(epic);
        return epic;
    }

    @Override
    public SubTask getSubTaskById(long subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        if (historyManager == null) {
            historyManager = new Managers().getDefaultHistory();
        }
        historyManager.addHistory(subTask);
        return subTask;
    }

    @Override
    public List<Long> returnSubTasksForEpicById(long epicId) {

        if (epics == null) {
            epics = new HashMap<>();
        }
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
            historyManager.removeHistory(task);
            tasks.remove(taskId);
        }
    }

    @Override
    public void removeEpicById(long epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Long currentId : epic.getSubTaskList()) {
                historyManager.removeHistory(subTasks.get(currentId));
                subTasks.remove(currentId);
            }
            historyManager.removeHistory(epic);
            epics.remove(epicId);
        }
    }


    @Override
    public void removeSubTaskById(long subTaskId) {
        SubTask subTask = subTasks.get(subTaskId);
        if (subTask != null) {
            historyManager.removeHistory(subTask);
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

        if (returnSubTasksForEpicById(epic.getId()).size() == 0) {
            return;
        }

        List<Long> subTaskList = epic.getSubTaskList();

        List<LocalDateTime> startTimes = new ArrayList<>();
        List<LocalDateTime> endTimes = new ArrayList<>();
        int subTaskDuration = 0;
        for (long subTaskId : subTaskList) {
            SubTask subTask = getSubTaskById(subTaskId);

            startTimes.add(subTask.getStartTime());
            subTaskDuration += subTask.getDuration();
        }

        Comparator<LocalDateTime> localDateTimeComparator = LocalDateTime::compareTo;

        epic.setStartTime(startTimes.stream().min(localDateTimeComparator).orElse(null));
        epic.setEpicEndTime(epic.getStartTime().plusMinutes(subTaskDuration));
        epic.setDuration(subTaskDuration);

    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return treeSet;
    }

    public void checkCrossForTasks(Task task) {

        //блок на случай пустого времени старта
        if (task.getStartTime() == null) {
            //Task latestTask = treeSet.stream().max(Comparator.nullsLast(Comparator.comparing(Task::getStartTime)));
            Set<Task> tempSet = getPrioritizedTasks();
            LocalDateTime latestTaskTime = LocalDateTime.MIN;
            for (Task tempTask : tempSet) {
                if (tempTask.getStartTime().plusMinutes(tempTask.getDuration()).isAfter(latestTaskTime)) {
                    latestTaskTime = tempTask.getStartTime().plusMinutes(tempTask.getDuration());
                }
            }
            task.setStartTime(latestTaskTime.plusMinutes(5));
        }
        //

        Set<Task> tempTreeSet = getPrioritizedTasks();

        LocalDateTime timeStartTime = task.getStartTime();
        LocalDateTime timeEndTime = task.getStartTime().plusMinutes(task.getDuration());

        for (Task tempTask : tempTreeSet) {
            if ((timeStartTime.isAfter(tempTask.getStartTime()) && timeStartTime.isBefore(tempTask.getStartTime().plusMinutes(tempTask.getDuration()))) ||
                    (timeEndTime.isAfter(tempTask.getStartTime()) && timeStartTime.isBefore(tempTask.getStartTime().plusMinutes(tempTask.getDuration()))) ){
                throw new RuntimeException("Пересечение задач не допускается!" + tempTask.getName());
            }
        }

    }
}
