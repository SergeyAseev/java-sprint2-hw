package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import entities.*;
import enums.Status;
import kv.KVServer;
import kv.KVTaskClient;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class Main {

    static final int PORT = 8080;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();
    public static void main(String[] args) throws IOException, InterruptedException {

        //TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new HttpTaskServer.TaskHandler());
        httpServer.start();
        new KVServer().start();
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8078/register");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        KVTaskClient kvTaskClient = new KVTaskClient(); // TODO и что мне с тобой тут делать?

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String apiToken = response.body();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        TaskManager taskManager = new HTTPTaskManager(8078);
        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW,
                LocalDateTime.of(2022, 5,24,1,0), 15);
        Task task2 = new Task("Тестовое описание task2", "Тест task2", Status.NEW,
                LocalDateTime.of(2022, 5,25,2,0), 15);
        long taskId1 = taskManager.createTask(task1);
        long taskId2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        Epic epic2 = new Epic("Тестовое описание epic2", "Test epic2", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);
        long epicId2 = taskManager.createEpic(epic2);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.NEW, epicId1,
                null, 15);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.NEW, epicId2,
                LocalDateTime.of(2022, 5,27,0,0), 15);
        SubTask subTask3 = new SubTask("Тестовое описание subTask3", "Тест subTask3", Status.NEW, epicId2,
                LocalDateTime.of(2022, 4,28,0,0), 15);
        long subTaskId1 = taskManager.createSubTask(subTask1);
        long subTaskId2 = taskManager.createSubTask(subTask2);
        long subTaskId3 = taskManager.createSubTask(subTask3);

        //блок для теста истории и записи в файл
        taskManager.getTaskById(taskId1);
        taskManager.getTaskById(taskId2);
        taskManager.getEpicById(epicId1);
        taskManager.getEpicById(epicId2);
        taskManager.getSubTaskById(subTaskId1);
        taskManager.getSubTaskById(subTaskId2);
        taskManager.getSubTaskById(subTaskId3);
        //printForTest(taskManager);
        kvTaskClient.put(apiToken, gson.toJson(taskManager.returnAllTasks()));
        kvTaskClient.load(apiToken);
    }

    public static void printForTest(TaskManager taskManager) {

        System.out.println("Все задачи:");
        System.out.println(taskManager.returnAllTasks());
        System.out.println("Все эпики:");
        System.out.println(taskManager.returnAllEpics());
        System.out.println("Все подзадачи:");
        System.out.println(taskManager.returnAllSubTasks());
        System.out.println("История вызовов:");
        System.out.println(taskManager.getHistory());
    }
}
