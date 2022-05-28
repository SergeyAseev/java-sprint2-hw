package manager;

import entities.Task;

import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    List<Task> getHistory();

    void removeHistory(Task task);
}
