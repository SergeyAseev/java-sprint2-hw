import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        Manager manager = new Manager();

        Task task1 = manager.createTask( "Тестовое описание 1", "Тест 1", Status.NEW);
        Task task2 = manager.createTask( "Тестовое описание 2", "Тест 2", Status.NEW);

        SubTask subTask1 = manager.createSubTask("Тестовое описание 3", "Тест 3", Status.NEW, 4);
        SubTask subTask2 = manager.createSubTask("Тестовое описание 4", "Тест 4", Status.NEW, 4);

        List<SubTask> subTaskList = new ArrayList<>();
        subTaskList.add(subTask1);
        subTaskList.add(subTask2);


        Epic epic1 = manager.createEpic( "test", "test", Status.NEW, Collections.singletonList(subTask1));
        Epic epic2 = manager.createEpic( "test", "test", Status.NEW, subTaskList);

        HashMap<Long, Epic> arrayList = (HashMap<Long, Epic>) manager.returnAllEpics();
        System.out.println(arrayList);
    }
}
