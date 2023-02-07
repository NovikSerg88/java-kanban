package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    protected List<Task> taskHistory = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        taskHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        if (taskHistory.size() > 10) {
            taskHistory.remove(0);
            return taskHistory;
        } else {
            return taskHistory;
        }
    }
}
