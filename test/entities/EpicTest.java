package entities;

import enums.Status;
import manager.FileBackedTasksManager;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {


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

}