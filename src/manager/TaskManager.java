package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public interface TaskManager {

    // Создаем объекты по типам
    long createTask(Task task);

    long createEpic(Epic epic);

    long createSubTask(SubTask subTask);

    // Переводим статус объектов в "в процессе"
    void startTask(Task task);

    void startSubTask(SubTask subTask);

    // Переводим статус объектов в "сделано"
    void endTask(Task task);

    void endSubTask(SubTask subTask);

    //Обновляем объекты по типу
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void updateEpicStatus(Epic epic);

    // Возвращаем все объекты по типам
    List<Task> returnAllTasks();

    List<Epic> returnAllEpics();

    List<SubTask> returnAllSubTasks();

    // Удаляем объекты
    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    // Возвращаем объекты по уникальному идентификатору
    Task getTaskById(long taskId);

    Epic getEpicById(long epicId);

    SubTask getSubTaskById(long subTaskId);

    List<Long> returnSubTasksForEpicById(long epicId);

    // Удаляем объекты по универсальному идентификатору
    void removeTaskById(long taskId);

    void removeEpicById(long epicId);

    void removeSubTaskById(long subTaskId);

    // Получаем историю
    List<Task> getHistory();

    void calculateEpicTime(Epic epic);

    Set getPrioritizedTasks();

}
