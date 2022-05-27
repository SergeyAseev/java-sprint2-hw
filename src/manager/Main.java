package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;

import java.io.File;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        //TaskManager taskManager = Managers.getDefault()
        TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Task task1 = new Task("Тестовое описание task1", "Тест task1", Status.NEW,
                LocalDateTime.of(2022, 5,30,0,0), 15);
        Task task2 = new Task("Тестовое описание task2", "Тест task2", Status.NEW,
                LocalDateTime.of(2022, 5,29,0,0), 15);
        long taskId1 = taskManager.createTask(task1);
        long taskId2 = taskManager.createTask(task2);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW);
        Epic epic2 = new Epic("Тестовое описание epic2", "Test epic2", Status.NEW);
        long epicId1 = taskManager.createEpic(epic1);
        long epicId2 = taskManager.createEpic(epic2);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.NEW, epicId1,
                LocalDateTime.of(2022, 5,26,0,0), 15);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.NEW, epicId2,
                LocalDateTime.of(2022, 5,27,0,0), 15);
        SubTask subTask3 = new SubTask("Тестовое описание subTask3", "Тест subTask3", Status.NEW, epicId2,
                LocalDateTime.of(2022, 5,28,0,0), 15);
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
/*        System.out.println(taskManager.getEpicById(epicId2).getStartTime());
        System.out.println(taskManager.getEpicById(epicId2).getDuration());
        System.out.println(taskManager.getEpicById(epicId2).getEndTime());*/

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
