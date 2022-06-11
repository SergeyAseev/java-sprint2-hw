package manager;

import entities.Epic;
import entities.Task;
import enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends ManagerTest<InMemoryTaskManager>{

    final File file = new File("task.csv");
    final File fileForEmptyTests = new File("fileForEmptyTests.csv");
    @BeforeEach
    void initFileTest() {
        taskManager = new FileBackedTasksManager(file);
        super.init();
    }

    @Test
    @Disabled
    void loadFromFileTest() {

        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW,
                LocalDateTime.of(2022, 5,25,2,0), 15);
        taskManager.createTask(task1);
        taskManager.getTaskById(task1.getId());

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.loadFromFile(file);
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

    @Test
    @Disabled
    void epicWithoutSubTaskSaveAndLoadTest() {

        Epic epic11 = new Epic("description for EpicWithoutSubTask", "EpicWithoutSubTask", Status.NEW,
                LocalDateTime.of(2022, 1,1,0,0), 15);
        taskManager.createEpic(epic11);
        taskManager.getEpicById(epic11.getId());

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.loadFromFile(file);
        assertEquals(fileBackedTasksManager.getEpicById(epic11.getId()), epic11, "Эпик из файла не " +
                "равен созданному");
    }

    @Test
    @Disabled
    void emptyHistorySaveTest() {

        TaskManager fileTaskManager = new FileBackedTasksManager().loadFromFile(fileForEmptyTests);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.loadFromFile(new File("fileForEmptyTests.csv"));

        assertEquals(fileTaskManager.getHistory().size(), fileBackedTasksManager.getHistory().size(),
                "История должна быть пустой");
    }

    @Test
    @Disabled
    void emptyHistoryLoadTest() {

        TaskManager fileTaskManager = new FileBackedTasksManager().loadFromFile(fileForEmptyTests);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.loadFromFile(new File("fileForEmptyTests.csv"));

        assertEquals(fileTaskManager.getHistory().size(), fileBackedTasksManager.getHistory().size(),
                "История должна быть пустой");
    }

    @Test
    @Disabled
    void emptyTaskListSaveAndLoadTest() {

        //save
        TaskManager fileTaskManager = new FileBackedTasksManager().loadFromFile(fileForEmptyTests);
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.loadFromFile(new File("fileForEmptyTests.csv"));

        fileTaskManager.removeAllTasks();
        fileBackedTasksManager.removeAllTasks();

        assertEquals(fileTaskManager.returnAllTasks().size(), fileBackedTasksManager.returnAllTasks().size(),
                "В списке задач ничего не должно быть");

        //load
        TaskManager fileTaskManagerLoad = new FileBackedTasksManager(new File("fileForEmptyTests.csv"), true);
        FileBackedTasksManager fileBackedTasksManagerLoad = new FileBackedTasksManager();
        fileBackedTasksManagerLoad.loadFromFile(new File("fileForEmptyTests.csv"));

        assertEquals(fileTaskManagerLoad.returnAllTasks().size(), fileBackedTasksManagerLoad.returnAllTasks().size(),
                "В списке задач ничего не должно быть");
    }


}