package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    static final int MAX_TASKS = 10;
    protected List<Task> taskHistory = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (taskHistory.size() < MAX_TASKS) {
            taskHistory.add(task);
        } else {
            taskHistory.remove(0);
            taskHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }
}
