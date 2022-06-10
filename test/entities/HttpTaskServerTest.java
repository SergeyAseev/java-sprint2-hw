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
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HTTPTaskManager taskManager;
    private HttpServer httpServer; //TODO точно верно?
    private Task task;
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    void init() {
        try {
            new KVServer().start();
            taskManager = new HTTPTaskManager(8078);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

/*        task = new Task("Test for addingNewTask","testAddNewTask1", Status.NEW,
                LocalDateTime.now(), 15);
        taskManager.createTask(task);

        epic = new Epic("Test for addingNewEpic", "testAddNewEpic", Status.NEW);
        taskManager.createEpic(epic);

        subTask = new SubTask("Test for addingNewSubTask", "testAddNewSubTask",
                Status.NEW, epic.getId(), LocalDateTime.now().plusDays(1), 15);
        taskManager.createSubTask(subTask);*/
    }

    @AfterEach
    void stop() {
        httpServer.stop(0);
    }

    @Test
    void clientTest() throws IOException, InterruptedException {

        Task task1 = new Task("descriptionTask1","nameTask1", Status.NEW,
                LocalDateTime.now(), 15);
        Task task2 = new Task("descriptionTask2","nameTask2", Status.NEW,
                LocalDateTime.now().plusDays(1), 15);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic = new Epic("descriptionEpic1", "nameEpic1", Status.NEW);
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("descriptionSubTask1", "nameSubTask1",
                Status.NEW, epic.getId(), LocalDateTime.now(), 15);
        taskManager.createSubTask(subTask);

        HTTPTaskManager newManager = new HTTPTaskManager(8078);
        assertEquals(taskManager.returnAllTasks().size(), newManager.returnAllTasks().size(),
                "Не совпадает кол-во задач");
        assertEquals(taskManager.returnAllEpics().size(), newManager.returnAllEpics().size(),
                "Не совпадает кол-во эпиков");
        assertEquals(taskManager.returnAllSubTasks().size(), newManager.returnAllSubTasks().size(),
                "Не совпадает кол-во подзадач");
    }

    @Test
    void createTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task = new Task(1,"t1", "T1", Status.NEW, LocalDateTime.now(), 15);
        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

 }