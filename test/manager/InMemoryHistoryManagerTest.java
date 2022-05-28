package manager;

import entities.Task;
import enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

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

        Task task11 = new Task(11, "Тестовое описание task1", "Тест task11", Status.NEW,
                LocalDateTime.of(2022, 4, 1, 0, 0), 15);
        Task task22 = new Task(12, "Тестовое описание task2", "Тест task22", Status.NEW,
                LocalDateTime.of(2022, 4, 2, 0, 0), 15);

        historyManager.addHistory(task11);
        historyManager.addHistory(task22);
        historyManager.addHistory(task22);

        assertEquals(2, historyManager.getHistory().size(), "Был добавлен дубль в историю");
    }

    @Test
    void removeFromHistoryTest() {

        Task task11 = new Task(11, "Тестовое описание task1", "Тест task11", Status.NEW,
                LocalDateTime.of(2022, 4, 1, 0, 0), 15);
        Task task22 = new Task(12, "Тестовое описание task2", "Тест task22", Status.NEW,
                LocalDateTime.of(2022, 4, 2, 0, 0), 15);
        Task task33 = new Task(13, "Тестовое описание task3", "Тест task33", Status.NEW,
                LocalDateTime.of(2022, 4, 2, 0, 0), 15);

        historyManager.addHistory(task11);
        historyManager.addHistory(task22);
        historyManager.addHistory(task33);
        historyManager.removeHistory(task22);

        assertEquals(2, historyManager.getHistory().size(), "Неправильное удаление из середины истории");
    }

    @Test
    void removeFirstTaskInHistoryTest() {

        Task task11 = new Task(11, "Тестовое описание task1", "Тест task11", Status.NEW,
                LocalDateTime.of(2022, 4, 1, 0, 0), 15);
        Task task22 = new Task(12, "Тестовое описание task2", "Тест task22", Status.NEW,
                LocalDateTime.of(2022, 4, 2, 0, 0), 15);
        Task task33 = new Task(13, "Тестовое описание task3", "Тест task33", Status.NEW,
                LocalDateTime.of(2022, 4, 2, 0, 0), 15);

        historyManager.addHistory(task11);
        historyManager.addHistory(task22);
        historyManager.addHistory(task33);
        historyManager.removeHistory(task11);

        assertEquals(2, historyManager.getHistory().size(), "Неправильное удаление первого элемента" +
                " истории");
    }

    @Test
    void removeLastTaskInHistoryTest() {

        Task task11 = new Task(11, "Тестовое описание task1", "Тест task11", Status.NEW,
                LocalDateTime.of(2022, 4, 1, 0, 0), 15);
        Task task22 = new Task(12, "Тестовое описание task2", "Тест task22", Status.NEW,
                LocalDateTime.of(2022, 4, 2, 0, 0), 15);
        Task task33 = new Task(13, "Тестовое описание task3", "Тест task33", Status.NEW,
                LocalDateTime.of(2022, 4, 2, 0, 0), 15);

        historyManager.addHistory(task11);
        historyManager.addHistory(task22);
        historyManager.addHistory(task33);
        historyManager.removeHistory(task33);

        assertEquals(2, historyManager.getHistory().size(), "Неправильное удаление последнего элемента" +
                " истории");
    }

}