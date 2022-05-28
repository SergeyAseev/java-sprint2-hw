package manager;

import entities.Task;
import enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends ManagerTest<InMemoryTaskManager>{

    File file = new File("task.csv");
    @BeforeEach
    void initFileTest() {
        taskManager = new FileBackedTasksManager(file);
        super.init();
    }

    @Test
    void loadFromFileTest() {

        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW,
                LocalDateTime.of(2022, 5,25,2,0), 15);
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        assertEquals(fileBackedTasksManager.getTaskById(task1.getId()), task1, "Задача из файла не " +
                "равна созданной задаче");

    }

    @Test
    void managerSaveExceptionTest() {

        File fileIncorrect = new File("incorrect.csv");

        try {
            new FileBackedTasksManager(fileIncorrect, true);
        } catch (RuntimeException e) {
            System.out.println("Тест работы ManagerSaveException для некорректного имени файла");
        }

    }
}