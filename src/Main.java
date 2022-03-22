import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        Manager manager = new Manager();

        manager.createTask( "Тестовое описание 1", "Тест 1", Status.NEW);
        manager.createTask( "Тестовое описание 2", "Тест 2", Status.NEW);
        //manager.createEpic( "test", "test", Status.NEW, List);
        //manager.createEpic( "test", "test", Status.NEW);

/*        Map<Long, Task> taskList1 = (Map<Long, Task>) manager.returnTaskById(1);
        System.out.println(taskList1);*/

    }
}
