package manager;

import entities.Task;
import enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest{

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void initInMemoryHistoryTaskManager() {
        historyManager = new InMemoryHistoryManager();
    }
    @Test
    void emptyHistoryTest() {
        assertEquals(0, historyManager.getHistory().size(), "История должна быть пустой");
    }

    @Test
    void tryToMakeDoubleInHistoryTest() {

        Task task11 = new Task(11,"Тестовое описание task1", "Тест task11", Status.NEW,
                LocalDateTime.of(2022, 4,1,0,0), 15);
        Task task22 = new Task(12,"Тестовое описание task2", "Тест task22", Status.NEW,
                LocalDateTime.of(2022, 4,2,0,0), 15);

        historyManager.addHistory(task11);
        historyManager.addHistory(task22);
        historyManager.addHistory(task22);
        System.out.println(historyManager.getHistory());

        //TODO не работает правильно добавление элемента в конец. Может не только в конец
        assertEquals(2, historyManager.getHistory().size(), "Был добавлен дубль в историю");
    }

    @Test
    void removeFromHistoryTest() {

/*        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW, LocalDateTime.now(), 15);
        Task task2 = new Task("Тестовое описание task2", "Тест task2", Status.NEW, LocalDateTime.now(), 15);
        Task task3 = new Task("Тестовое описание task3", "Тест task3", Status.NEW, LocalDateTime.now(), 15);
        Task task4 = new Task("Тестовое описание task4", "Тест task4", Status.NEW, LocalDateTime.now(), 15);
        long taskId1 = taskManager.createTask(task1);
        long taskId2 = taskManager.createTask(task2);
        long taskId3 = taskManager.createTask(task3);
        long taskId4 = taskManager.createTask(task4);

        assertEquals(0, taskManager.getHistory().size(), "История должна быть пустой");
        taskManager.getTaskById(taskId1);
        taskManager.getTaskById(taskId2);
        taskManager.getTaskById(taskId3);
        taskManager.getTaskById(taskId4);
        System.out.println(taskManager.getHistory());

        assertEquals(4, taskManager.getHistory().size(), "История должна быть не пустой");*/
    }

    @Test
    void removeFirstTaskInHistoryTest() {

/*        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW, LocalDateTime.now(), 15);
        Task task2 = new Task("Тестовое описание task2", "Тест task2", Status.NEW, LocalDateTime.now(), 15);
        Task task3 = new Task("Тестовое описание task3", "Тест task3", Status.NEW, LocalDateTime.now(), 15);
        long taskId1 = taskManager.createTask(task1);
        long taskId2 = taskManager.createTask(task2);
        long taskId3 = taskManager.createTask(task3);

        taskManager.getTaskById(taskId1);
        taskManager.getTaskById(taskId2);
        taskManager.getTaskById(taskId3);

        taskManager.removeTaskById(taskId1);

        System.out.println(taskManager.getHistory());
        //assertEquals(taskManager.getHistory(), task2, "");
        assertEquals(2, taskManager.getHistory().size(), "История должна состоять из двух задач");*/
    }

}