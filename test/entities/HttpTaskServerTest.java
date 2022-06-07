package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import enums.Status;
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

class HttpTaskServerTest<T extends TaskManager> {

    protected T taskManager;
    private Task task;
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;
    private HttpServer httpServer;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    void initHttp() throws IOException {

        HttpServer.create();
        httpServer.bind(new InetSocketAddress(8078), 0);
        httpServer.createContext("/tasks", new HttpTaskServer.TaskHandler());
        httpServer.start();

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
    void getTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        final List<Task> tasks = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Не верное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

}