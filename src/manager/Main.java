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

        InMemoryTaskManager manager = new InMemoryTaskManager();
        //TaskManager taskManager = new TaskManager();
        SubTaskManager subTaskManager = new SubTaskManager();
        EpicManager epicManager = new EpicManager();

        Task task1 = manager.createTask("Тестовое описание 1", "Тест 1", Status.NEW);
        Task task2 = manager.createTask("Тестовое описание 2", "Тест 2", Status.NEW);

        SubTask subTask1 = subTaskManager.createSubTask("Тестовое описание 3", "Тест 3", Status.NEW, 4);
        SubTask subTask2 = subTaskManager.createSubTask("Тестовое описание 4", "Тест 4", Status.NEW, 4);

        List<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(subTask1);
        subTaskList.add(subTask2);

        Epic epic1 = epicManager.createEpic("test", "test", Status.NEW, Collections.singletonList(subTask1));
        Epic epic2 = epicManager.createEpic("test", "test", Status.NEW, subTaskList);

        System.out.println(manager.returnAllTasks());
        System.out.println(manager.getHistory());
        System.out.println(manager.returnTaskById(0));
        System.out.println(manager.returnTaskById(1));
        System.out.println(epicManager.returnEpicById(4));
        System.out.println(epicManager.returnEpicById(5));
        System.out.println(subTaskManager.returnSubTaskById(2));
        System.out.println(subTaskManager.returnSubTaskById(3));
        System.out.println(manager.getHistory());

    }
}