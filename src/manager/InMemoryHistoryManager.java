package manager;

import entities.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    public final int DEEP_OF_HISTORY = 11;
    public final List<Task> historyList = new ArrayList<>();


    @Override
    public void add(Task task) {
    }

    @Override
    public List<Task> getHistory() {
        if (historyList.size() > DEEP_OF_HISTORY) {
            historyList.remove(0);
        }

        return historyList;
    }
}
