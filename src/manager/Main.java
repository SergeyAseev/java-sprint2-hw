package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW);
        Task task2 = new Task("Тестовое описание task2", "Тест task2", Status.NEW);
        long taskId1 = taskManager.createTask(task1);
        long taskId2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        Epic epic2 = new Epic("Тестовое описание epic2", "Test epic2", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);
        long epicId2 = taskManager.createEpic(epic2);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.NEW, epicId2);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.NEW, epicId2);
        SubTask subTask3 = new SubTask("Тестовое описание subTask3", "Тест subTask3", Status.NEW, epicId2);
        long subTaskId1 = taskManager.createSubTask(subTask1);
        long subTaskId2 = taskManager.createSubTask(subTask2);
        long subTaskId3 = taskManager.createSubTask(subTask3);

        //блок для теста истории

        taskManager.getTaskById(taskId1);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId2);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epicId1);
        taskManager.getEpicById(epicId2);
        taskManager.removeEpicById(epicId2); //todo исправить удаление хвоста

        printForTest(taskManager);
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
