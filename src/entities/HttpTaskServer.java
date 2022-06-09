package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import enums.Status;
import enums.TaskType;
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

public class HttpTaskServer {

    private static TaskManager taskManager;
    private static HttpServer httpServer;
    private static Gson gson;
    static final int PORT = 8080;

    private HttpTaskServer() {

    }

    public static HttpServer getInstance() throws IOException, InterruptedException {

        if (httpServer == null) {
            taskManager = Managers.getDefault();
            //taskManager = Managers.getDefault("http://localhost:8078/register/");
            //taskManager = new HTTPTaskManager("http://localhost:8078/register/");
            gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                    .registerTypeAdapter(Status.class, new EnumAdapter())
                    .create();

            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", HttpTaskServer::getAllTasks);
            httpServer.createContext("/tasks/task", HttpTaskServer::getTask);
            httpServer.createContext("/tasks/epic", HttpTaskServer::getEpic);
            httpServer.createContext("/tasks/subtask", HttpTaskServer::getSubTask);
            httpServer.createContext("/tasks/history", HttpTaskServer::getHistory);
        }
        return httpServer;
    }

    private static void getHistory(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");

            String strResponse = "";
            if (httpExchange.getRequestMethod().equals("GET")) {
                strResponse = gson.toJson(taskManager.getHistory());
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                strResponse = gson.toJson("Неверный метод");
                httpExchange.sendResponseHeaders(405,0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getSubTask(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            String strResponse = "";
            String method = httpExchange.getRequestMethod();
            Map<String, String> queryParamMap = getParams(httpExchange);

            switch(method) {
                case "GET":
                    //тут будем получать
                    if (!queryParamMap.isEmpty()) {
                        try {
                            SubTask subTask = taskManager.getSubTaskById(Long.parseLong(queryParamMap.get("id")));
                            if (Objects.nonNull(subTask)) {
                                strResponse = gson.toJson(subTask);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Подзадача не найдена");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Что-то пошло не так" + e.getMessage());
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson(taskManager.returnAllSubTasks());
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                case "POST":
                    String jsonStr = new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
                    if (!queryParamMap.isEmpty()) {
                        // тут будем обновлять
                        try {
                            SubTask subTask = gson.fromJson(jsonStr, new TypeToken<SubTask>() {}.getType());
                            subTask.setId(Long.parseLong(queryParamMap.get("id")));
                            strResponse = gson.toJson(subTask);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (Exception e) {
                            strResponse = gson.toJson("Подзадача не обновилась" + e.getMessage());
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        // тут будем создавать
                        try {
                            SubTask subTask = gson.fromJson(jsonStr, new TypeToken<SubTask>() {}.getType());
                            taskManager.createSubTask(subTask);
                            if (subTask != null) {
                                strResponse = gson.toJson(subTask);
                                httpExchange.sendResponseHeaders(201, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Подзадача не создалась" + e.getMessage());
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
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                default:
                    strResponse = gson.toJson("Что-то пошло не так с запросом");
                    httpExchange.sendResponseHeaders(400, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getEpic(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            String strResponse = "";
            String method = httpExchange.getRequestMethod();
            Map<String, String> queryParamMap = getParams(httpExchange);

            switch(method) {
                case "GET":
                    //тут будем получать
                    if (!queryParamMap.isEmpty()) {
                        try {
                            Epic epic = taskManager.getEpicById(Long.parseLong(queryParamMap.get("id")));
                            if (Objects.nonNull(epic)) {
                                strResponse = gson.toJson(epic);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Эпик не найден");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Что-то пошло не так" + e.getMessage());
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson(taskManager.returnAllEpics());
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                case "POST":
                    String jsonStr = new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
                    if (!queryParamMap.isEmpty()) {
                        // тут будем обновлять
                        try {
                            Epic epic = gson.fromJson(jsonStr, new TypeToken<Epic>() {}.getType());
                            epic.setId(Long.parseLong(queryParamMap.get("id")));
                            strResponse = gson.toJson(epic);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (Exception e) {
                            strResponse = gson.toJson("Эпик не обновилась" + e.getMessage());
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        // тут будем создавать
                        try {
                            Epic epic = gson.fromJson(jsonStr, new TypeToken<Epic>() {}.getType());
                            taskManager.createEpic(epic);
                            if (epic != null) {
                                strResponse = gson.toJson(epic);
                                httpExchange.sendResponseHeaders(201, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Эпик не создался" + e.getMessage());
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
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                default:
                    strResponse = gson.toJson("Что-то пошло не так с запросом");
                    httpExchange.sendResponseHeaders(400, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getTask(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");
            String strResponse = "";
            String method = httpExchange.getRequestMethod();
            Map<String, String> queryParamMap = getParams(httpExchange);

            switch(method) {
                case "GET":
                    //тут будем получать
                    if (!queryParamMap.isEmpty()) {
                        try {
                            Task task = taskManager.getTaskById(Long.parseLong(queryParamMap.get("id")));
                            if (Objects.nonNull(task)) {
                                strResponse = gson.toJson(task);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Задача не найдена");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Задачи с таким ID нет " + e.getMessage());
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson(taskManager.returnAllTasks());
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                case "POST":
                    String jsonStr = new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
                    if (!queryParamMap.isEmpty()) {
                        // тут будем обновлять
                        try {
                            Task task = gson.fromJson(jsonStr, new TypeToken<Task>() {}.getType());
                            task.setId(Long.parseLong(queryParamMap.get("id")));
                            strResponse = gson.toJson(task);
                            httpExchange.sendResponseHeaders(200, 0);
                        } catch (Exception e) {
                            strResponse = gson.toJson("Задача не обновилась" + e.getMessage());
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        // тут будем создавать
                        try {
                            //Task task = gson.fromJson(jsonStr, Task.class);
                            Task task = gson.fromJson(jsonStr, new TypeToken<Task>() {}.getType());
                            taskManager.createTask(task);// TODO падает с NPE.
                            if (task != null) {
                                strResponse = gson.toJson(task);
                                // попадает
                                httpExchange.sendResponseHeaders(201, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Задача не создалась" + e.getMessage());
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
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {
                                strResponse = gson.toJson("Что-то пошло не так");
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        } catch (Exception e) {
                            strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                            httpExchange.sendResponseHeaders(400, 0);
                        }
                    } else {
                        strResponse = gson.toJson("Чтобы удалить что-то ненужное, надо сначала создать что-то ненужное");
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                default:
                    strResponse = gson.toJson("Что-то пошло не так с запросом");
                    httpExchange.sendResponseHeaders(400, 0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getAllTasks(HttpExchange httpExchange) {
        try (OutputStream outputStream = httpExchange.getResponseBody()) {
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");

            String strResponse = "";
            if (httpExchange.getRequestMethod().equals("GET")) {
                strResponse = gson.toJson(returnAll());
                //strResponse = gson.toJson(taskManager.returnAllTasks()) + gson.toJson(taskManager.returnAllSubTasks())
                //        + gson.toJson(taskManager.returnAllEpics());
                httpExchange.sendResponseHeaders(200, 0);
            } else {
                strResponse = gson.toJson("Неверный метод");
                httpExchange.sendResponseHeaders(405,0);
            }
            outputStream.write(strResponse.getBytes());
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static Map<String, String> getParams(HttpExchange httpExchange) {
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

    private static Class<? extends Task> getClassByType(TaskType taskType) {
        switch (taskType) {
            case Task:
                return Task.class;
            case Epic:
                return Epic.class;
            case SubTask:
                return SubTask.class;
            default:
                return null;
        }
    }

    private static String returnAll() {
        return gson.toJson(taskManager.returnAllTasks()) + gson.toJson(taskManager.returnAllSubTasks())
                + gson.toJson(taskManager.returnAllEpics());
    }

/*    public static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента."); //убеждаемся, что хоть что-то работает

            //заготовка
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            String[] pathSplit = path.split("/");
            String query = httpExchange.getRequestURI().getQuery();
            InputStream inputStream = httpExchange.getRequestBody();
            String bodyStr = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            //
            RequestMapping requestMapping = new RequestMapping();
            Response response = requestMapping.response(method, pathSplit, query, bodyStr);

            httpExchange.sendResponseHeaders(response.code, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.response.getBytes());
            }
        }
    }*/
}