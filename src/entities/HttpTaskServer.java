package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private TaskManager taskManager;
    private HttpServer httpServer;
    private Gson gson;
    static final int PORT = 8080;

    public HttpTaskServer() {
        Managers managers = new Managers();
        taskManager = managers.getDefault();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
        init();
    }

    private void init() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServer.createContext("/tasks", this::getAllTasks);
        httpServer.createContext("/tasks/task", this::getTask);
        httpServer.createContext("/tasks/epic", this::getEpic);
        httpServer.createContext("/tasks/subtask", this::getSubTask);
        httpServer.createContext("/tasks/history", this::getHistory);
    }

    public HttpServer getInstance() throws IOException {

        if (httpServer == null) {
            HttpTaskServer httpTaskServer = new HttpTaskServer();
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", httpTaskServer::getAllTasks);
            httpServer.createContext("/tasks/task", httpTaskServer::getTask);
            httpServer.createContext("/tasks/epic", httpTaskServer::getEpic);
            httpServer.createContext("/tasks/subtask", httpTaskServer::getSubTask);
            httpServer.createContext("/tasks/history", httpTaskServer::getHistory);
        }
        return httpServer;
    }

    private void getHistory(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");

            String strResponse = "";
            if (httpExchange.getRequestMethod().equals("GET")) {
                strResponse = gson.toJson(taskManager.getHistory());
                sendText(httpExchange, strResponse);
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                strResponse = gson.toJson("Неверный метод");
                sendText(httpExchange, strResponse);
                httpExchange.sendResponseHeaders(405, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpExchange.close();
        }
    }

    private void getSubTask(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            String strResponse = "";
            String method = httpExchange.getRequestMethod();
            Map<String, String> queryParamMap = getParams(httpExchange);

            switch (method) {
                case "GET":
                    //тут будем получать
                    if (!queryParamMap.isEmpty()) {
                        try {
                            SubTask subTask = taskManager.getSubTaskById(Long.parseLong(queryParamMap.get("id")));
                            if (Objects.nonNull(subTask)) {
                                strResponse = gson.toJson(subTask);
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Подзадача не найдена");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Что-то пошло не так" + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson(taskManager.returnAllSubTasks());
                        sendText(httpExchange, strResponse);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                case "POST":
                    String jsonStr = new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
                    if (!queryParamMap.isEmpty()) {
                        // тут будем обновлять
                        try {
                            SubTask subTask = gson.fromJson(jsonStr, new TypeToken<SubTask>() {
                            }.getType());
                            subTask.setId(Long.parseLong(queryParamMap.get("id")));
                            strResponse = gson.toJson(subTask);
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (Exception e) {
                            strResponse = gson.toJson("Подзадача не обновилась" + e.getMessage());
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        // тут будем создавать
                        try {
                            SubTask subTask = gson.fromJson(jsonStr, new TypeToken<SubTask>() {
                            }.getType());
                            taskManager.createSubTask(subTask);
                            if (subTask != null) {
                                strResponse = gson.toJson(subTask);
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(201, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Подзадача не создалась" + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                case "DELETE":
                    if (!queryParamMap.isEmpty()) {
                        try {
                            SubTask subTask = taskManager.getSubTaskById(Long.parseLong(queryParamMap.get("id")));
                            if (subTask != null) {
                                taskManager.removeSubTaskById(subTask.getId());
                                strResponse = gson.toJson("Подзадача удалена");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                        sendText(httpExchange, strResponse);
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                default:
                    strResponse = gson.toJson("Что-то пошло не так с запросом");
                    httpExchange.sendResponseHeaders(400, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpExchange.close();
        }
    }

    private void getEpic(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            String strResponse = "";
            String method = httpExchange.getRequestMethod();
            Map<String, String> queryParamMap = getParams(httpExchange);

            switch (method) {
                case "GET":
                    //тут будем получать
                    if (!queryParamMap.isEmpty()) {
                        try {
                            Epic epic = taskManager.getEpicById(Long.parseLong(queryParamMap.get("id")));
                            if (Objects.nonNull(epic)) {
                                strResponse = gson.toJson(epic);
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Эпик не найден");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Что-то пошло не так" + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson(taskManager.returnAllEpics());
                        sendText(httpExchange, strResponse);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                case "POST":
                    String jsonStr = new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
                    if (!queryParamMap.isEmpty()) {
                        // тут будем обновлять
                        try {
                            Epic epic = gson.fromJson(jsonStr, new TypeToken<Epic>() {
                            }.getType());
                            epic.setId(Long.parseLong(queryParamMap.get("id")));
                            strResponse = gson.toJson(epic);
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (Exception e) {
                            strResponse = gson.toJson("Эпик не обновилась" + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        // тут будем создавать
                        try {
                            Epic epic = gson.fromJson(jsonStr, new TypeToken<Epic>() {
                            }.getType());
                            taskManager.createEpic(epic);
                            if (epic != null) {
                                strResponse = gson.toJson(epic);
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(201, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Эпик не создался" + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                case "DELETE":
                    if (!queryParamMap.isEmpty()) {
                        try {
                            Epic epic = taskManager.getEpicById(Long.parseLong(queryParamMap.get("id")));
                            if (epic != null) {
                                taskManager.removeSubTaskById(epic.getId());
                                strResponse = gson.toJson("Эпик удалеа");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                        sendText(httpExchange, strResponse);
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                default:
                    strResponse = gson.toJson("Что-то пошло не так с запросом");
                    httpExchange.sendResponseHeaders(400, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpExchange.close();
        }
    }

    private void getTask(HttpExchange httpExchange) {
        System.out.println(httpExchange.getRequestMethod());
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            String strResponse = "";
            String method = httpExchange.getRequestMethod();
            Map<String, String> queryParamMap = getParams(httpExchange);

            switch (method) {
                case "GET":
                    //тут будем получать
                    if (!queryParamMap.isEmpty()) {
                        try {
                            String qq = queryParamMap.get("id");
                            Task task = taskManager.getTaskById(Long.parseLong(queryParamMap.get("id")));
                            if (Objects.nonNull(task)) {
                                strResponse = gson.toJson(task);
                                sendText(httpExchange, strResponse);
                                return;
                            } else {
                                strResponse = gson.toJson("Задача не найдена");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            strResponse = gson.toJson("Задачи с таким ID нет " + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        sendText(httpExchange, "Нечего_возвращать");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                case "POST":
                    String jsonStr = new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
                    if (!queryParamMap.isEmpty()) {
                        // тут будем обновлять
                        try {
                            Task task = gson.fromJson(jsonStr, new TypeToken<Task>() {
                            }.getType());
                            task.setId(Long.parseLong(queryParamMap.get("id")));
                            strResponse = gson.toJson(task);
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (Exception e) {
                            strResponse = gson.toJson("Задача не обновилась" + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        // тут будем создавать
                        try {
                            Task task = gson.fromJson(jsonStr, new TypeToken<Task>() {
                            }.getType());
                            taskManager.createTask(task);
                            if (task != null) {
                                strResponse = gson.toJson(task);
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(201, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Задача не создалась" + e.getMessage());
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    }
                case "DELETE":
                    if (!queryParamMap.isEmpty()) {
                        try {
                            Task task = taskManager.getTaskById(Long.parseLong(queryParamMap.get("id")));
                            if (task != null) {
                                taskManager.removeTaskById(task.getId());
                                strResponse = gson.toJson("Задача удалена");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                sendText(httpExchange, strResponse);
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                            sendText(httpExchange, strResponse);
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                        sendText(httpExchange, strResponse);
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                default:
                    strResponse = gson.toJson("Что-то пошло не так с запросом");
                    httpExchange.sendResponseHeaders(400, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpExchange.close();
        }
    }

    private void getAllTasks(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");

            String strResponse = "";
            if (httpExchange.getRequestMethod().equals("GET")) {
                strResponse = gson.toJson(returnAll());
                sendText(httpExchange, strResponse);
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                strResponse = gson.toJson("Неверный метод");
                httpExchange.sendResponseHeaders(405, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        } finally {
            httpExchange.close();
        }
    }

    private Map<String, String> getParams(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();

        if (Objects.isNull(query)) {
            return new HashMap<>();
        }

        try {
            Map<String, String> paramsMap = new HashMap<>();
            for (String param : query.split("&")) {
                String[] splitStr = param.split("=");
                paramsMap.put(splitStr[0], splitStr[1]);
            }
            return paramsMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private String returnAll() {
        Map<Long, Task> commonMap = new HashMap<>();
        for (Task task : taskManager.returnAllTasks()) {
            commonMap.put(task.getId(), task);
        }
        for (Epic epic : taskManager.returnAllEpics()) {
            commonMap.put(epic.getId(), epic);
        }
        for (SubTask subTask : taskManager.returnAllSubTasks()) {
            commonMap.put(subTask.getId(), subTask);
        }
        return gson.toJson(commonMap);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}