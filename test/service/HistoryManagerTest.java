package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import service.HistoryManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {

    protected T historyManager;

    @Test
    public void HistoryManagerAddTest() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 1, 19, 0);
        Task task1 = new Task(0, "Task1", "Test", Status.NEW, time1, 1);
        Task task2 = new Task(1, "Task2", "Test", Status.NEW, time2, 1);

        historyManager.add(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));

        historyManager.add(task2);
        history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void HistoryManagerRemoveTest() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 1, 19, 0);
        Task task1 = new Task(0, "Task1", "Test", Status.NEW, time1, 1);
        Task task2 = new Task(1, "Task2", "Test", Status.NEW, time2, 1);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void HistoryManagerGetHistoryTest() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 1, 19, 0);
        Task task1 = new Task(0, "Task1", "Test", Status.NEW, time1, 1);
        Task task2 = new Task(1, "Task2", "Test", Status.NEW, time2, 1);

        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(task2);
        assertEquals(expectedHistory, history);
    }
}