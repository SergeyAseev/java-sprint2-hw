package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import entities.Epic;
import enums.Status;
import entities.SubTask;
import entities.Task;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.createTask("Тестовое описание 1", "Тест 1", Status.NEW);
        Task task2 = taskManager.createTask("Тестовое описание 2", "Тест 2", Status.NEW);

        SubTask subTask1 = taskManager.createSubTask("Тестовое описание 3", "Тест 3", Status.NEW, 4);
        SubTask subTask2 = taskManager.createSubTask("Тестовое описание 4", "Тест 4", Status.NEW, 4);

        List<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(subTask1);
        subTaskList.add(subTask2);

        Epic epic1 = taskManager.createEpic("test", "test", Status.NEW, Collections.singletonList(subTask1));
        Epic epic2 = taskManager.createEpic("test", "test", Status.NEW, subTaskList);

        taskManager.returnTaskById(task1.getId());
        taskManager.returnEpicById(epic1.getId());
        taskManager.returnSubTaskById(subTask1.getId());

        printForTest(taskManager);

    }


    public static void printForTest(TaskManager taskManager) {
        System.out.println("Все задачи:");
        System.out.println(taskManager.returnAllTasks());
        System.out.println("Все эпики:");
        System.out.println(taskManager.returnAllEpics());
        System.out.println("Все подзадачи:");
        System.out.println(taskManager.returnAllSubTasks());
        System.out.println(taskManager.getHistory());

    }
}
