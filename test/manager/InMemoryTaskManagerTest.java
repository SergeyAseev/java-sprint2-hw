package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    protected TaskManager taskManager = new InMemoryTaskManager();
    Task task;
    Epic epic;
    SubTask subTask;
    @BeforeEach
    void init() {
        taskManager = new InMemoryTaskManager();
        //todo Добавить новые поля времен
        task = new Task("Test for addingNewTask","testAddNewTask", Status.NEW, LocalDateTime.now(), 15);
        final long taskId = taskManager.createTask(task);
        final Task savedTask = taskManager.getTaskById(taskId);
        List<Task> taskList = taskManager.returnAllTasks();

        epic = new Epic("Test for addingNewEpic", "testAddNewEpic", Status.NEW, LocalDateTime.now(), 15);
        final long epicId = taskManager.createEpic(epic);
        final Epic savedEpic = taskManager.getEpicById(epicId);
        List<Epic> epicList = taskManager.returnAllEpics();

        subTask = new SubTask("Test for addingNewSubTask", "testAddNewSubTask", Status.NEW, epicId, LocalDateTime.now(), 15);
        final long subTaskId = taskManager.createSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTaskById(subTaskId);
        List<SubTask> subTaskList = taskManager.returnAllSubTasks();

        //возвращаются все задачи
        assertNotNull(taskList, "Задачи не возвращаются");
        assertNotNull(epicList, "Эпики не возвращаются");
        assertNotNull(subTaskList, "Подзадачи не возвращаются");
        //созданная задача и возвразенная через поиск по ID совпадают
        assertEquals(task, savedTask, "Не совпадают Задачи");
        assertEquals(epic, savedEpic, "Не совпадают Эпики");
        assertEquals(subTask, savedSubTask, "Не совпадают Подзадачи");
        //созданный ID и возвращенный ID совпадают
        assertEquals(taskId, savedTask.getId(), "Не совпадают идентификаторы Задач");
        assertEquals(epicId, savedEpic.getId(), "Не совпадают идентификаторы Эпиков");
        assertEquals(subTaskId, savedSubTask.getId(), "Не совпадают идентификаторы Подзадач");
        //в MAP хранится правильное кол-во задач
        assertEquals(1, taskList.size(), "Неверное кол-во Задач");
        assertEquals(1, epicList.size(), "Неверное кол-во Эпиков");
        assertEquals(1, subTaskList.size(), "Неверное кол-во Подзадач");
    }

    @Test
    void createTask() {
    }

    @Test
    void createEpic() {
    }

    @Test
    void createSubTask() {
    }

    @Test
    void startTask() {
    }

    @Test
    void startSubTask() {
    }

    @Test
    void endTask() {
    }

    @Test
    void endSubTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void updateSubTask() {
    }

    @Test
    void updateEpicStatus() {
    }

    @Test
    void returnAllTasks() {
    }

    @Test
    void returnAllEpics() {
    }

    @Test
    void returnAllSubTasks() {
    }

    @Test
    void removeAllTasks() {
        taskManager.removeAllTasks();
        List<Task> taskList = taskManager.returnAllTasks();
        assertEquals(0, taskList.size(), "Не все задачи удалены");
    }

    @Test
    void removeAllEpics() {
    }

    @Test
    void removeAllSubTasks() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void getEpicById() {
    }

    @Test
    void getSubTaskById() {
    }

    @Test
    void returnSubTasksForEpicById() {
    }

    @Test
    void removeTaskById() {
/*        TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Task task = new Task("Test for addingNewTask", "testAddNewTask", Status.NEW);
        long taskId = taskManager.createTask(task);*/

        taskManager.removeTaskById(taskId);
        List<Task> taskList = taskManager.returnAllTasks();
        assertEquals(0, taskList.size(), "Конкретная задача не удалена");
    }

    @Test
    void removeEpicById() {
    }

    @Test
    void removeSubTaskById() {
    }

    @Test
    void getHistory() {
    }
}