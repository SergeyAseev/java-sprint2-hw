package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

import java.util.List;

public interface TaskManager {

    // Создаем объекты по типам
    Task createTask(String description, String name, Enum<Status> statusEnum);

    Epic createEpic(String description, String name, Enum<Status> statusEnum, List<SubTask> subTaskList);

    SubTask createSubTask(String description, String name, Enum<Status> statusEnum, long epicId);

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

    // Возвразаем все объекты по типам
    Object returnAllTasks();

    Object returnAllEpics();

    Object returnAllSubTasks();

    // Удаляем объекты
    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    // Возвращаем объекты по уникальному идентификатору
    Task returnTaskById(long taskId);

    Epic returnEpicById(long epicId);

    SubTask returnSubTaskById(long subTaskId);

    List<SubTask> returnSubTasksForEpicById(long epicId);

    // Удаляем объекты по универсальному идентификатору
    void removeTaskById(long taskId);

    void removeEpicById(long epicId);

    void removeSubTaskById(long subTaskId);

    // Получаем историю
    List<Task> getHistory();
}
