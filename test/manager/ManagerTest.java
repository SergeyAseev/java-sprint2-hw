package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class ManagerTest<T extends TaskManager> {

    protected T taskManager;
    Task task;
    Epic epic;
    SubTask subTask;

    void init() {
        task = new Task("Test for addingNewTask","testAddNewTask", Status.NEW, LocalDateTime.now(), 15);
        taskManager.createTask(task);

        epic = new Epic("Test for addingNewEpic", "testAddNewEpic", Status.NEW, LocalDateTime.now(), 15);
        taskManager.createEpic(epic);

        subTask = new SubTask("Test for addingNewSubTask", "testAddNewSubTask",
                Status.NEW, epic.getId(), LocalDateTime.now(), 15);
        taskManager.createSubTask(subTask);
    }

    @Test
    void createTask() {
        final List<Task> taskList = taskManager.returnAllTasks();

        assertNotNull(taskList, "Задачи не возвращаются");
    }

    @Test
    void createEpic() {
        final List<Epic> epicList = taskManager.returnAllEpics();

        assertNotNull(epicList, "Эпики не возвращаются");
    }

    @Test
    void createSubTask() {
        final List<SubTask> subTaskList = taskManager.returnAllSubTasks();

        assertNotNull(subTaskList, "Подзадачи не возвращаются");
    }

    @Test
    void startTask() {
        task.setStatusEnum(Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, task.getStatusEnum(), "У задачи должен быть статус 'В процессе' ");
    }

    @Test
    void startSubTask() {
        subTask.setStatusEnum(Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, subTask.getStatusEnum(), "У подЗадачи должен быть статус 'В процессе' ");
    }

    @Test
    void endTask() {
        task.setStatusEnum(Status.DONE);

        assertEquals(Status.DONE, task.getStatusEnum(), "У задачи должен быть статус 'Завершено' ");
    }

    @Test
    void endSubTask() {
        subTask.setStatusEnum(Status.DONE);

        assertEquals(Status.DONE, subTask.getStatusEnum(), "У подЗадачи должен быть статус 'Завершено' ");
    }

    @Test
    void updateTask() {
        List<Task> taskList = taskManager.returnAllTasks();
        taskManager.updateTask(task);

        assertEquals(1, taskList.size(), "Должна быть одна задача");
    }

    @Test
    void updateEpic() {
        List<Epic> epicList = taskManager.returnAllEpics();
        taskManager.updateEpic(epic);

        assertEquals(1, epicList.size(), "Должен быть один эпик");
    }

    @Test
    void updateSubTask() {
        List<SubTask> subTaskList = taskManager.returnAllSubTasks();
        taskManager.updateSubTask(subTask);

        assertEquals(1, subTaskList.size(), "Должна быть одна подзадача");
    }

    @Test
    void returnAllTasks() {
        final List<Task> taskList = taskManager.returnAllTasks();

        assertNotNull(taskList, "Задачи не возвращаются");
        assertEquals(1, taskList.size(), "Неверное кол-во Задач");
    }

    @Test
    void returnAllEpics() {
        final List<Epic> epicList = taskManager.returnAllEpics();

        assertNotNull(epicList, "Эпики не возвращаются");
        assertEquals(1, epicList.size(), "Неверное кол-во Эпиков");
    }

    @Test
    void returnAllSubTasks() {
        final List<SubTask> subTaskList = taskManager.returnAllSubTasks();

        assertNotNull(subTaskList, "Подзадачи не возвращаются");
        assertEquals(1, subTaskList.size(), "Неверное кол-во Подзадач");
    }

    @Test
    void removeAllTasks() {
        taskManager.removeAllTasks();
        List<Task> taskList = taskManager.returnAllTasks();

        assertEquals(0, taskList.size(), "Не все задачи удалены");
    }

    @Test
    void removeAllEpics() {
        taskManager.removeAllEpics();
        List<Epic> epicList = taskManager.returnAllEpics();

        assertEquals(0, epicList.size(), "Не все эпики удалены");
    }

    @Test
    void removeAllSubTasks() {
        taskManager.removeAllSubTasks();
        List<SubTask> subTaskList = taskManager.returnAllSubTasks();

        assertEquals(0, subTaskList.size(), "Не все подзадачи удалены");
    }

    @Test
    void getTaskById() {
        final Task savedTask = taskManager.getTaskById(task.getId());

        assertEquals(task, savedTask, "Не совпадают Задачи");
    }

    @Test
    void getEpicById() {
        final Epic savedEpic = taskManager.getEpicById(epic.getId());

        assertEquals(epic, savedEpic, "Не совпадают Эпики");
    }

    @Test
    void getSubTaskById() {
        final SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertEquals(subTask, savedSubTask, "Не совпадают Подзадачи");
    }

    @Test
    void returnSubTasksForEpicById() {
    }

    @Test
    void removeTaskById() {
        taskManager.removeTaskById(task.getId());
        List<Task> taskList = taskManager.returnAllTasks();

        assertEquals(0, taskList.size(), "Конкретная задача не удалена");
    }

    @Test
    void removeEpicById() {
        taskManager.removeEpicById(epic.getId());
        List<Epic> epicList = taskManager.returnAllEpics();

        assertEquals(0, epicList.size(), "Конкретный Эпик не удален");
    }

    @Test
    void removeSubTaskById() {
        taskManager.removeSubTaskById(subTask.getId());
        List<SubTask> subTaskList = taskManager.returnAllSubTasks();

        assertEquals(0, subTaskList.size(), "Конкретная ПодЗадача не удалена");
    }

    @Test
    void getHistory() {
    }

    @Test
    @DisplayName("empty list of subTask for epic status test")
    void emptyListOfSubTaskCheckEpicStatusTest() {
        TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);

        Epic savedEpic = taskManager.getEpicById(epicId1);
        assertEquals(Status.NEW, savedEpic.getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in NEW status in epic test")
    void allSubTaskInNewStatusCheckEpicStatusTest() {
        TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.NEW, epicId1);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.NEW, epicId1);
        long subTaskId1 = taskManager.createSubTask(subTask1);
        long subTaskId2 = taskManager.createSubTask(subTask2);

        Epic savedEpic = taskManager.getEpicById(epicId1);
        assertEquals(Status.NEW, savedEpic.getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in DONE status in epic test")
    void allSubTaskInDoneStatusCheckEpicStatusTest() {
        TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.DONE, epicId1);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.DONE, epicId1);
        long subTaskId1 = taskManager.createSubTask(subTask1);
        long subTaskId2 = taskManager.createSubTask(subTask2);

        Epic savedEpic = taskManager.getEpicById(epicId1);
        assertEquals(Status.DONE, savedEpic.getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in NEW and DONE status in epic test")
    void allSubTaskInNewAndDoneStatusCheckEpicStatusTest() {
        TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.NEW, epicId1);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.DONE, epicId1);
        long subTaskId1 = taskManager.createSubTask(subTask1);
        long subTaskId2 = taskManager.createSubTask(subTask2);

        Epic savedEpic = taskManager.getEpicById(epicId1);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in PROGRESS status in epic test")
    void allSubTaskInProgressStatusCheckEpicStatusTest() {
        TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.IN_PROGRESS, epicId1);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.IN_PROGRESS, epicId1);
        long subTaskId1 = taskManager.createSubTask(subTask1);
        long subTaskId2 = taskManager.createSubTask(subTask2);

        Epic savedEpic = taskManager.getEpicById(epicId1);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatusEnum());
    }

    @Test
    void subTaskHasEpicTest() {
        SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());
        assertNotNull(savedSubTask.getEpicId(), "Не может быть подзадачи без эпика");
    }

    @Test
    void getTaskWIthWrongIdTest() {
        long wrongTaskId = - 100;
        Task savedTask = taskManager.getTaskById(wrongTaskId);

        assertNull(savedTask, "Не должны получать задачу с неверный идентификатором");
    }

}