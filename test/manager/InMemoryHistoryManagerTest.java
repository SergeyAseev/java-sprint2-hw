package manager;

import entities.Task;
import enums.Status;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void emptyHistoryTest() {

        TaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW);
        Task task2 = new Task("Тестовое описание task2", "Тест task2", Status.NEW);
        Task task3 = new Task("Тестовое описание task3", "Тест task3", Status.NEW);
        Task task4 = new Task("Тестовое описание task4", "Тест task4", Status.NEW);
        long taskId1 = taskManager.createTask(task1);
        long taskId2 = taskManager.createTask(task2);
        long taskId3 = taskManager.createTask(task3);
        long taskId4 = taskManager.createTask(task4);

        assertEquals(0, taskManager.getHistory().size(), "История должна быть пустой");
        taskManager.getTaskById(taskId1);
        taskManager.getTaskById(taskId2);
        taskManager.getTaskById(taskId3);
        taskManager.getTaskById(taskId4);

        System.out.println(taskManager.returnAllTasks());
        System.out.println(taskManager.getHistory());

        //assertEquals(4, taskManager.returnAllTasks().size());
        //assertEquals(2, taskManager.getHistory().size(), "История должна быть не пустой");

    }

}