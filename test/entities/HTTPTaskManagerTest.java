package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import enums.Status;
import kv.KVServer;
import kv.KVTaskClient;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {
    private TaskManager taskManager;
    private KVServer kvServer;
    private Task task;
    private Epic epic;
    private SubTask subTask;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    void init() {
        taskManager = new InMemoryTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager = new HTTPTaskManager(8078);
            httpTaskServer.getInstance().start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        task = new Task("Test for addingNewTask","testAddNewTask1", Status.NEW,
                LocalDateTime.now(), 15);
        taskManager.createTask(task);
        epic = new Epic("Test for addingNewEpic", "testAddNewEpic", Status.NEW);
        taskManager.createEpic(epic);
        subTask = new SubTask("Test for addingNewSubTask", "testAddNewSubTask",
                Status.NEW, epic.getId(), LocalDateTime.now().plusDays(1), 15);
        taskManager.createSubTask(subTask);
    }

    @AfterEach
    void stop() {
       kvServer.stop();
    }

    @Test
    void createTaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task = new Task("t1", "T1", Status.NEW, LocalDateTime.now().plusDays(2), 15);
        taskManager.createTask(task);
        String json = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void createEpicTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic");
        Epic epic = new Epic("e1", "E1", Status.NEW);
        taskManager.createEpic(epic);
        String json = gson.toJson(epic);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void createSubTaskTest() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        SubTask s1 = new SubTask("t1", "T1", Status.NEW, epic.getId(),LocalDateTime.now().plusDays(6), 5);
        taskManager.createSubTask(s1);
        String json = gson.toJson(s1);

        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getTaskTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        Task task = new Task("t1", "T1", Status.NEW, LocalDateTime.now().plusDays(2), 15);
        taskManager.createTask(task);
        String json = gson.toJson(task);

        HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/?id="+task.getId()))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }

    @Test
    void createSubTaskNoIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        SubTask subTask = new SubTask("st1", "ST1", Status.NEW, 1, LocalDateTime.now(), 5);
        String json = gson.toJson(subTask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        long tempId = task.getId();
        URI url = URI.create("http://localhost:8080/tasks/task/?id="+tempId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeSubTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        long tempId = subTask.getId();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + tempId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }

    @Test
    void removeEpicByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        long tempId = epic.getId();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id="+tempId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        assertEquals(200, response.statusCode());
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

}