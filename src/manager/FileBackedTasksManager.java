package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;
import enums.TaskType;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTasksManager() {
        this(new File("task.csv"), false);
    }

    public FileBackedTasksManager(File file) {
        this(file, false);
    }

    public FileBackedTasksManager(File file, boolean load) {
        this.file = file;
        if (load) {
            load();
        }
    }

    @Override
    public long createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public long createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public long createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public Task getTaskById(long taskId) {
        super.getTaskById(taskId);
        save();
        return tasks.get(taskId);
    }

    @Override
    public Epic getEpicById(long epicId) {
        super.getEpicById(epicId);
        save();
        return epics.get(epicId);
    }

    @Override
    public SubTask getSubTaskById(long subTaskId) {
        super.getSubTaskById(subTaskId);
        save();
        return subTasks.get(subTaskId);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();

    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void removeTaskById(long taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(long epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubTaskById(long subTaskId) {
        super.removeSubTaskById(subTaskId);
        save();
    }

    @Override
    public void startTask(Task task) {
        super.startTask(task);
        save();
    }

    @Override
    public void startSubTask(SubTask subTask) {
        super.startSubTask(subTask);
        save();
    }

    @Override
    public void endTask(Task task) {
        super.endTask(task);
        save();
    }

    @Override
    public void endSubTask(SubTask subTask) {
        super.endSubTask(subTask);
        save();
    }

    //Метод создания задачи из строки
    private Task fromString(String value) {

        String[] taskData = value.split(",");
        TaskType taskType = TaskType.valueOf(taskData[1]);
        long taskId = Long.parseLong(taskData[0]);
        String taskDescription = taskData[4];
        String taskName = taskData[2];
        Status taskStatus = Status.valueOf(taskData[3]);
        LocalDateTime taskStartTime = LocalDateTime.parse(taskData[6]);
        int taskDuration = Integer.parseInt(taskData[7]);

        switch (taskType) {
            case Task:
                Task task = new Task(taskDescription, taskName, taskStatus, taskStartTime, taskDuration);
                task.setId(taskId);
                return task;
            case Epic:
                Epic epic = new Epic(taskDescription, taskName, taskStatus, taskStartTime, taskDuration);
                epic.setId(taskId);
                return epic;
            case SubTask:
                long epicId = Long.parseLong(taskData[5]);
                SubTask subTask = new SubTask(taskDescription, taskName, taskStatus, epicId, taskStartTime, taskDuration);
                subTask.setId(taskId);
                return subTask;
        }
        return null;
    }

    //переводим задачи в строковую форму
    public String toString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + "," + task.getStatusEnum() + ","
                + task.getDescription() + "," + task.getEpicId() + ","
                + task.getStartTime() + "," + task.getDuration();
    }

    //Сохранение в файл
    private void save() {

        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append("id,type,name,status,description,epic,startTime,duration");
            writer.newLine();
            for (Map.Entry<Long, Task> entry : tasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, Epic> entry : epics.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            for (Map.Entry<Long, SubTask> entry : subTasks.entrySet()) {
                writer.append(toString(entry.getValue()));
                writer.newLine();
            }
            writer.newLine();
            writer.append(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл" + file.getName());
        }
    }

    //Восстановление из файла
    private void load() {
        long indexFromHistory = 0L;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Пропускаем заголовок
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                final Task task = fromString(line);
                final long tempId = task.getId();

                if (task.getTaskType() == TaskType.Task) {
                    tasks.put(tempId, task);
                } else if (task.getTaskType() == TaskType.Epic) {
                    epics.put(tempId, (Epic) task);
                } else if (task.getTaskType() == TaskType.SubTask) {
                    subTasks.put(tempId, (SubTask) task);
                }

                if (indexFromHistory < tempId) {
                    indexFromHistory = tempId;
                }
            }
            index = indexFromHistory;

            String line = reader.readLine();
            List<Long> listOfHistory = historyFromString(line);
            for (Long id : listOfHistory) {
                if (tasks.containsKey(id)) {
                    getTaskById(id);
                } else if (epics.containsKey(id)) {
                    getEpicById(id);
                } else if (subTasks.containsKey(id)) {
                    getSubTaskById(id);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при восстановлении из файла" + file.getName());
        }
    }

    //Сохранения менеджера истории в файл
    private static String historyToString(HistoryManager historyManager) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Task> historyList = historyManager.getHistory();
        for (int i = 0; i < historyList.size(); i++) {
            stringBuilder.append(historyList.get(i).getId());
            if (i != historyList.size() - 1) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }

    //Восстановление менеджера истории из файла
    static List<Long> historyFromString(String value) {
        String[] tasksId = value.split(",");
        List<Long> historyList = new ArrayList<>();
        for (String taskId : tasksId) {
            historyList.add(Long.valueOf(taskId));
        }
        return historyList;
    }

    //Восстанавление данных менеджера из файла при запуске программы
    public static FileBackedTasksManager loadFromFile(File file) {
        return new FileBackedTasksManager(file, true);
    }

}
