package entities;

import com.google.gson.Gson;
import enums.Status;
import manager.FileBackedTasksManager;
import manager.TaskManager;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

public class RequestMapping {

    private final TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), true);

    private final Gson gson = new Gson();

    protected Response response (String method, String[] pathSplit, String query, String body) {

        String response = "";
        int responseCode = 500;
        String firstPath = pathSplit[1]; //tasks
        String secondPath = "";
        long id = 0;
        if (pathSplit.length > 2) {
            secondPath = pathSplit[2]; /* /history /task /epic /subtask  */
            if (query != null) {
                String[] splitQuery = query.split("=");
                id = Long.parseLong(splitQuery[1]);
            }
        }

        switch (method) {
            case "GET":
                if (firstPath.equals("tasks")) {
                    if (Objects.equals(secondPath, "")) {
                        response = returnAll();
                        responseCode = 200;
                    } else {
                        if ("task".equals(secondPath)) {
                            response = returnTaskById(taskManager.getTaskById(id));
                            responseCode = response != null ? 200 : 500;
                        } else if ("epic".equals(secondPath)) {
                            response = returnEpicById(taskManager.getEpicById(id));
                            responseCode = response != null ? 200 : 500;
                        } else if ("subtask".equals(secondPath)) {
                            response = returnSubTaskById(taskManager.getSubTaskById(id));
                            responseCode = response != null ? 200 : 500;
                        } else if ("history".equals(secondPath)) {
                            response = returnHistory();
                            responseCode = response != null ? 200 : 500;
                        } else {
                            responseCode = 400;
                        }
                    }
                } else {
                    responseCode = 400;
                }
                break;
            case "POST":
                break;
            case "DELETE":
                if (firstPath.equals("tasks")) {
                    if ("task".equals(secondPath)) {
                        deleteTaskById(id);
                        response = "Удалена задача с ID: " + id;
                        responseCode = 200;
                    } else if ("epic".equals(secondPath)) {
                        deleteEpicById(id);
                        response = "Удален эпик с ID: " + id;
                        responseCode = 200;
                    } else if ("subtask".equals(secondPath)) {
                        deleteSubTaskById(id);
                        response = "Удалена подзадача с ID: " + id;
                        responseCode = 200;
                    } else {
                        responseCode = 400;
                    }
                } else {
                    responseCode = 400;
                }
                break;
            default:
                response = "temp error";
        }

        return new Response(response, responseCode);
    }

    private void deleteSubTaskById(long subTaskId) {
        taskManager.removeSubTaskById(subTaskId);
    }

    private void deleteEpicById(long epicId) {
        taskManager.removeEpicById(epicId);
    }

    private void deleteTaskById(long taskId) {
        taskManager.removeTaskById(taskId);
    }

    private String returnAll() {
        return gson.toJson(taskManager.returnAllTasks()) + gson.toJson(taskManager.returnAllSubTasks())
                + gson.toJson(taskManager.returnAllEpics());
    }

    private String returnTaskById(Task task) {
        return gson.toJson(taskManager.getTaskById(task.getId()));
    }

    private String returnEpicById(Epic epic) {
        return gson.toJson(taskManager.getEpicById(epic.getId()));
    }

    private String returnSubTaskById(SubTask subTask) {
        return gson.toJson(taskManager.getSubTaskById(subTask.getId()));
    }

    private String returnHistory() {
        return  gson.toJson(taskManager.getHistory());
    }
}

class Response {
    String response;
    int code;

    Response (String response, int code) {
        this.response = response;
        this.code = code;
    }
}
