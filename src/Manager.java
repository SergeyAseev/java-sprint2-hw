import java.util.HashMap;
import java.util.Map;

public class Manager {

    static long index = 0;

    static EpicManager epicManager;
    static SubTaskManager subTaskManager;
    static TaskManager taskManager;

    Map<Long, Epic> epicsMap = new HashMap<>();
    Map<Long, SubTask> subTasksMap = new HashMap<>();
    Map<Long, Task> tasksMap = new HashMap<>();


    /**
     * увеличивает уникальный идентификатор
     */
    public Long increaseIntId() {
        return index++;
    }






























}
