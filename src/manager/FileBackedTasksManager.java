package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.TaskType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    //TODO а зачем он нам, если в классах уже переопределили?
    //Метод сохранения задач в строку
    private String toString(Task task) {
        return "";
    }

    //TODO а какой метод вызывать-то? createTask или запись в карту?
    //Метод создания задачи из строки
    private Task fromString(String value) {

        String[] taskData = value.split(",");
        TaskType taskType = TaskType.valueOf(taskData[1]);
        switch (taskType) {
            case TASK:
            case SUBTASK:
            case EPIC:
        }
        return null;
    }

    //Сохранение в файл
    private void save() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, Task> entry : tasks.entrySet()) {
            sb.append(entry.getValue().toString());
            //sb.append(toString(entry.getValue()));
            sb.append("\\n");
        }
        for (Map.Entry<Long, Epic> entry : epics.entrySet()) {
            sb.append(entry.getValue().toString());
            //sb.append(toString(entry.getValue()));
            sb.append("\\n");
        }
        for (Map.Entry<Long, SubTask> entry : subTasks.entrySet()) {
            sb.append(entry.getValue().toString());
            //sb.append(toString(entry.getValue()));
            sb.append("\\n\\n");
        }
        historyToString(historyManager);
        try {
            new FileWriter(file).write(sb.toString());
        } catch (IOException e) {
            //TODO кастомный выброс
        }
    }

    //Восстановление из файла
    private void load() {

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
