package manager;

import entities.Epic;
import entities.SubTask;
import entities.Task;
import enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

abstract class ManagerTest<T extends TaskManager> {

    protected T taskManager;
    private Task task;
    private Epic epic;
    private Epic epic1;
    private SubTask subTask;

    public void init() {
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

    @Test
    void createTask() {
        assertNotNull(taskManager.returnAllTasks(), "Задачи не возвращаются");
    }

    @Test
    void createEpic() {
        assertNotNull(taskManager.returnAllEpics(), "Эпики не возвращаются");
    }

    @Test
    void createSubTask() {
        assertNotNull(taskManager.returnAllSubTasks(), "Подзадачи не возвращаются");
    }

    @Test
    void startTask() {
        taskManager.startTask(task);

        assertEquals(Status.IN_PROGRESS, task.getStatusEnum(), "У задачи должен быть статус 'В процессе' ");
    }

    @Test
    void startSubTask() {
        taskManager.startSubTask(subTask);

        assertEquals(Status.IN_PROGRESS, subTask.getStatusEnum(), "У подЗадачи должен быть статус 'В процессе' ");
        assertEquals(Status.IN_PROGRESS, epic.getStatusEnum(), "У эпика должен быть статус 'В процессе' ");
    }

    @Test
    void endTask() {
        taskManager.endTask(task);

        assertEquals(Status.DONE, task.getStatusEnum(), "У задачи должен быть статус 'Завершено' ");
    }

    @Test
    void endSubTask() {
        taskManager.endSubTask(subTask);

        assertEquals(Status.DONE, subTask.getStatusEnum(), "У подЗадачи должен быть статус 'Завершено' ");
    }

    @Test
    void updateTask() {
        List<Task> taskList = taskManager.returnAllTasks();
        taskManager.updateTask(task);

        assertEquals(1, taskList.size(), "Должна быть одна задача");
    }

    @Test
    void updateEpic() {
        List<Epic> epicList = taskManager.returnAllEpics();
        taskManager.updateEpic(epic);

        assertEquals(2, epicList.size(), "Должен быть один эпик");
    }

    @Test
    void updateSubTask() {
        List<SubTask> subTaskList = taskManager.returnAllSubTasks();
        taskManager.updateSubTask(subTask);

        assertEquals(1, subTaskList.size(), "Должна быть одна подзадача");
    }

    @Test
    void returnAllTasks() {
        assertNotNull(taskManager.returnAllTasks(), "Задачи не возвращаются");
        assertEquals(1, taskManager.returnAllTasks().size(), "Неверное кол-во Задач");
    }

    @Test
    void returnAllEpics() {
        assertNotNull(taskManager.returnAllEpics(), "Эпики не возвращаются");
        assertEquals(2, taskManager.returnAllEpics().size(), "Неверное кол-во Эпиков");
    }

    @Test
    void returnAllSubTasks() {
        assertNotNull(taskManager.returnAllSubTasks(), "Подзадачи не возвращаются");
        assertEquals(1, taskManager.returnAllSubTasks().size(), "Неверное кол-во Подзадач");
    }

    @Test
    void removeAllTasks() {
        taskManager.removeAllTasks();

        assertEquals(0, taskManager.returnAllTasks().size(), "Не все задачи удалены");
    }

    @Test
    void removeAllEpics() {
        taskManager.removeAllEpics();

        assertEquals(0, taskManager.returnAllEpics().size(), "Не все эпики удалены");
        assertEquals(0, taskManager.returnAllSubTasks().size(), "Не все подзадачи удалены");

    }

    @Test
    void removeAllSubTasks() {
        taskManager.removeAllSubTasks();

        assertEquals(0, taskManager.returnAllSubTasks().size(), "Не все подзадачи удалены");
    }

    @Test
    void getTaskById() {
        assertEquals(task, taskManager.getTaskById(task.getId()), "Не совпадают Задачи");
    }

    @Test
    void getEpicById() {
        assertEquals(epic, taskManager.getEpicById(epic.getId()), "Не совпадают Эпики");
    }

    @Test
    void getSubTaskById() {
        assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()), "Не совпадают Подзадачи");
    }

    @Test
    void removeTaskById() {
        taskManager.removeTaskById(task.getId());

        assertEquals(0, taskManager.returnAllTasks().size(), "Конкретная задача не удалена");
    }

    @Test
    void removeEpicById() {
        taskManager.removeEpicById(epic.getId());

        assertEquals(1, taskManager.returnAllEpics().size(), "Конкретный Эпик не удален");
    }

    @Test
    void removeSubTaskById() {
        taskManager.removeSubTaskById(subTask.getId());

        assertEquals(0, taskManager.returnAllSubTasks().size(), "Конкретная ПодЗадача не удалена");
    }

    @Test
    @DisplayName("empty list of subTask for epic status test")
    void emptyListOfSubTaskCheckEpicStatusTest() {
        //TaskManager taskManager = new FileBackedTasksManager(new File("task.csv"), false);

        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW, LocalDateTime.now(), 15);
        long epicId1 = taskManager.createEpic(epic1);

        Epic savedEpic = taskManager.getEpicById(epicId1);
        assertEquals(Status.NEW, savedEpic.getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in NEW status in epic test")
    void allSubTaskInNewStatusCheckEpicStatusTest() {
        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW, LocalDateTime.now(), 15);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.NEW,
                epicId1, LocalDateTime.of(2022, 4,9,0,0), 15);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.NEW,
                epicId1, LocalDateTime.of(2022, 4,10,0,0), 15);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        Epic savedEpic = taskManager.getEpicById(epicId1);
        assertEquals(Status.NEW, savedEpic.getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in DONE status in epic test")
    void allSubTaskInDoneStatusCheckEpicStatusTest() {
        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW, LocalDateTime.now(), 15);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.DONE,
                epicId1, LocalDateTime.of(2022, 4,7,0,0), 15);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.DONE,
                epicId1, LocalDateTime.of(2022, 4,8,0,0), 15);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(Status.DONE, taskManager.getEpicById(epicId1).getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in NEW and DONE status in epic test")
    void allSubTaskInNewAndDoneStatusCheckEpicStatusTest() {
        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW, LocalDateTime.now(), 15);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.NEW,
                epicId1, LocalDateTime.of(2022, 4,5,0,0), 15);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.DONE,
                epicId1, LocalDateTime.of(2022, 4,6,0,0), 15);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epicId1).getStatusEnum());
    }

    @Test
    @DisplayName("all subTask in PROGRESS status in epic test")
    void allSubTaskInProgressStatusCheckEpicStatusTest() {
        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW, LocalDateTime.now(), 15);
        long epicId1 = taskManager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Тестовое описание subTask1", "Тест subTask1", Status.IN_PROGRESS,
                epicId1, LocalDateTime.of(2022, 4,3,0,0), 15);
        SubTask subTask2 = new SubTask("Тестовое описание subTask2", "Тест subTask2", Status.IN_PROGRESS,
                epicId1, LocalDateTime.of(2022, 4,4,0,0), 15);
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epicId1).getStatusEnum());
    }

    @Test
    void epicWithoutSubTask() {
        Epic epic1 = new Epic("Тестовое описание epic1", "Test epic1", Status.NEW, LocalDateTime.now(), 15);
        long epicId1 = taskManager.createEpic(epic1);

        assertEquals(Status.NEW, taskManager.getEpicById(epicId1).getStatusEnum());
    }

    @Test
    void subTaskHasEpicTest() {
        SubTask savedSubTask = taskManager.getSubTaskById(subTask.getId());

        assertNotNull(savedSubTask.getEpicId(), "Не может быть подзадачи без эпика");
    }

    @Test
    void getTaskWithWrongIdTest() {
        long wrongTaskId = - 100;
        try {
            taskManager.getTaskById(wrongTaskId);
        } catch (RuntimeException e) {
            System.out.println("Не должны получать задачу с неверный идентификатором");
        }
    }

    @Test
    void tryToMakeSubTaskWithoutEpic() {

        subTask = new SubTask("Test for subTaskWithoutEpic", "subTaskWithoutEpic",
                Status.NEW, -100, LocalDateTime.now(), 15);

        try {
            taskManager.createSubTask(subTask);
        } catch (RuntimeException e) {
            System.out.println("Ошибка: Попытка создать подзадачу без эпика");
        }

    }

    @Test
    void getSubTaskForEpicByEpicIdTest() {

        List<Long> subTaskIdFromEpic = taskManager.returnSubTasksForEpicById(epic.getId());
        assertEquals(subTaskIdFromEpic.size(),epic.getSubTaskList().size(), "Вернулось неправильное кол-во " +
                "поздазач для эпика");

        List<Long> subTaskIdFromWrongEpicId = taskManager.returnSubTasksForEpicById(-100);
        assertEquals(0, subTaskIdFromWrongEpicId.size(), "Вернулись подзадачи к эпику, " +
                "у которого некорректный ID");
    }

    @Test
    void checkTimesFieldsForTask() {

        Task savedTask = taskManager.getTaskById(task.getId());

        assertEquals(15, savedTask.getDuration(), "Неправильно записалась длительность задачи");
        assertEquals(LocalDateTime.of(2022, 5,24,0,0),
                savedTask.getStartTime(), "Неправильно записалось время старта задачи");
    }

    @Test
    void checkSortingTest() {

        task = new Task("Test for addingNewTask","testAddNewTask2", Status.NEW,
                LocalDateTime.of(2022, 4,22,0,0), 15);
        taskManager.createTask(task);

        LocalDateTime minStartTime = LocalDateTime.MAX;
        Task taskWithMinTime = null;
        for (Task task : taskManager.returnAllTasks()) {
            if (task.getStartTime().isBefore(minStartTime)) {
                minStartTime = task.getStartTime();
                taskWithMinTime = task;
            }
        }

        assertEquals(taskWithMinTime, taskManager.getPrioritizedTasks().stream().findFirst().get(), "Неправильная сортировка по " +
                "времени возрастанию: неверный самый первый элемент");
    }

    @Test
    void checkCreatingTaskWithNullStartTimeTest() {

        task = new Task("Test for addingNewTask","testAddNewTask2", Status.NEW,
                null, 15);
        taskManager.createTask(task);

        assertNotNull(task);
    }

    @Test
    void updateTaskTest() {

        List<Task> oldTasksList = taskManager.returnAllTasks();
        long oldTaskId = task.getId();
        Task newTask = new Task(oldTaskId,"Test for updatedTask","UpdatedTask", Status.NEW,
                null, 15);
        taskManager.updateTask(newTask);

        assertNotNull(newTask, "Обновленная задача не создалась");
        List<Task> newTasksList = taskManager.returnAllTasks();

        assertEquals(oldTasksList.size(), newTasksList.size(), "Количество задач после обновления должно совпадать" +
                " с кол-ом задач до обновления");
    }

}