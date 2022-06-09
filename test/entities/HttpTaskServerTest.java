package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import enums.Status;
import kv.KVServer;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HTTPTaskManager taskManager;
    private HttpServer httpServer;
    private Task task;
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    void initHttp() {
        try {
            new KVServer().start();
            taskManager = new HTTPTaskManager("http://localhost:8078");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        task = new Task("Test for addingNewTask","testAddNewTask1", Status.NEW,
                LocalDateTime.of(2022, 5,24,0,0), 15);
        taskManager.createTask(task);

        epic = new Epic("Test for addingNewEpic", "testAddNewEpic", Status.NEW);
        taskManager.createEpic(epic);

        epic1 = new Epic("description for EpicWithoutSubTask", "EpicWithoutSubTask", Status.NEW,
                LocalDateTime.of(2022, 1,1,0,0), 15);
        taskManager.createEpic(epic1);

        subTask = new SubTask("Test for addingNewSubTask", "testAddNewSubTask",
                Status.NEW, epic.getId(), LocalDateTime.of(2022, 4,23,0,0), 15);
        taskManager.createSubTask(subTask);
    }

    @AfterEach
    void stop() {
        httpServer.stop(1);
    }

    @Test
    void clientTest() throws IOException, InterruptedException {

        Task task1 = new Task("descriptionTask1","nameTask1", Status.NEW,
                LocalDateTime.of(2022, 6,5,0,0), 15);
        Task task2 = new Task("descriptionTask2","nameTask2", Status.NEW,
                LocalDateTime.of(2022, 6,6,0,0), 15);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic = new Epic("descriptionEpic1", "nameEpic1", Status.NEW);
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("descriptionSubTask1", "nameSubTask1",
                Status.NEW, epic.getId(), LocalDateTime.of(2022, 6,7,0,0), 15);
        taskManager.createSubTask(subTask);

        HTTPTaskManager testManager = new HTTPTaskManager("http://localhost:8078");
        assertEquals(taskManager.returnAllTasks().size(), testManager.returnAllTasks().size(),
                "Не совпадает кол-во задач");
        assertEquals(taskManager.returnAllEpics().size(), testManager.returnAllEpics().size(),
                "Не совпадает кол-во эпиков");
        assertEquals(taskManager.returnAllSubTasks().size(), testManager.returnAllSubTasks().size(),
                "Не совпадает кол-во подзадач");
    }

}