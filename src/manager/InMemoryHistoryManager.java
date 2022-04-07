package manager;

import entities.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    public final int DEEP_OF_HISTORY = 10;
    private final List<Task> historyList = new ArrayList<>();


    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) {
            return;
        }
        historyList.add(task);
        if (historyList.size() > DEEP_OF_HISTORY) {
            historyList.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
