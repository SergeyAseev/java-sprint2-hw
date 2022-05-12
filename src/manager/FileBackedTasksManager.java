package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;
import enums.TaskType;

import java.io.*;
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
        return super.createTask(task);
    }

    @Override
    public long createEpic(Epic epic) {
        save();
        return super.createEpic(epic);
    }

    @Override
    public long createSubTask(SubTask subTask) {
        save();
        return super.createSubTask(subTask);
    }

    @Override
    public Task getTaskById(long taskId) {
        save();
        return super.getTaskById(taskId);
    }

    @Override
    public Epic getEpicById(long epicId) {
        save();
        return super.getEpicById(epicId);
    }

    @Override
    public SubTask getSubTaskById(long subTaskId) {
        save();
        return super.getSubTaskById(subTaskId);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
    }

    @Override
    public void removeTaskById(long taskId) {
        save();
        super.removeTaskById(taskId);
    }

    @Override
    public void removeEpicById(long epicId) {
        save();
        super.removeEpicById(epicId);
    }

    @Override
    public void removeSubTaskById(long subTaskId) {
        save();
        super.removeSubTaskById(subTaskId);
    }

    public String toString(Task task) {
        return task.getId() /*+ task.gettype*/ + task.getName() + task.getStatusEnum() + task.getDescription() + task.getEpicId();
    }

    //TODO а какой метод вызывать-то? createTask или запись в карту?
    //Метод создания задачи из строки
    private Task fromString(String value) {

        String[] taskData = value.split(",");
        TaskType taskType = TaskType.valueOf(taskData[1]);
        long taskId = Long.parseLong(taskData[0]);
        String taskDescription = taskData[3];
        String taskName = taskData[2];
        Status taskStatus = Status.valueOf(taskData[4]);

        switch (taskType) {
            case TASK:
                Task task = new Task(taskDescription, taskName, taskStatus);
                createTask(task);
                tasks.put(taskId, task);
                return task;
            case EPIC:
                Epic epic = new Epic(taskDescription, taskName, taskStatus);
                createEpic(epic);
                epics.put(taskId, epic);
                return epic;
            case SUBTASK:
                long epicId = Long.parseLong(taskData[5]);
                SubTask subTask = new SubTask(taskDescription, taskName, taskStatus, epicId);
                createSubTask(subTask);
                subTasks.put(taskId, subTask);
                return subTask;
        }
        return null;
    }

    //Сохранение в файл
    private void save() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Task> entry : tasks.entrySet()) {
            //sb.append(entry.getValue().toString());
            sb.append(toString(entry.getValue()));
            sb.append("\\n");
        }
        for (Map.Entry<Long, Epic> entry : epics.entrySet()) {
            sb.append(entry.getValue().toString());
            sb.append("\\n");
        }
        for (Map.Entry<Long, SubTask> entry : subTasks.entrySet()) {
            sb.append(entry.getValue().toString());
            sb.append("\\n\\n");
        }
        historyToString(historyManager);
        try {
            new FileWriter(file).write(sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл");
        }
    }

    //Восстановление из файла
    private void load() {
        long maxId = 0;
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Пропускаем заголовок
            while (true) {
                String line = reader.readLine();
                if (line.isEmpty()) {
                    break;
                }
                final Task task = fromString(line);
                final long id = task.getId();
                if (maxId < id) {
                    maxId = id;
                }
                System.out.println();
/*                if (task.getТип() == TaskType.TASK) {
                    tasks.put(id, task);
				} else if (task.getТип() == TaskType.EPIC) {
                    subTasks.put(id, (SubTask) task);
                } else if (task.getType() == TaskType.SUBTASK) {
                    epics.put(id, (Epic) task);
                }*/
            }

            String line = reader.readLine();
/*            List<Long> idhistory = historyFromString(line);
            for (Long id : idhistory) {
                if (tasks.containsKey(id)) {
                    tasks.put()
                } else if (subTasks.containsKey(id)) {
                    subTasks.put();
                } else if (epics.containsKey(id)) {
                    epics.put();
                }
            }*/
        } catch (IOException e) {
            throw new ManagerSaveException("ошибка"); // TODO ManagerSaveException
        }
        // генератор
        //генераторИД = maxId;

    }

    //Сохранения менеджера истории в файл
    static String historyToString(HistoryManager historyManager) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            stringBuilder.append(task.getId());
        }
        return stringBuilder.toString();
    }

    //Восстановление менеджера истории из файла
    //TODO сохранять все таки ID и переписывать старое или убирать статик?
    static List<Task> historyFromString(String value) {
        String[] tasksId = value.split(",");
        List<Task> historyList = new ArrayList<>();
        for (String taskId : tasksId) {
            //historyList.add();
        }
        return historyList;
    }

    //Восстанавление данных менеджера из файла при запуске программы
    public static FileBackedTasksManager loadFromFile(File file) {
        return new FileBackedTasksManager(file, true);
    }

}
