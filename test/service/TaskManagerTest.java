package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    abstract T createManager();

    Task task1 = new Task(0, "Task1", "Test", Status.NEW);
    Epic epic1 = new Epic(1, "Epic1", "Test", Status.NEW);
    Subtask subtask1 = new Subtask(2, "Subtask1", "Test", Status.DONE, 1);
    Subtask subtask2 = new Subtask(3, "Subtask2", "Test", Status.DONE, 1);

    @BeforeEach
    public void beforeEach(){
        taskManager = createManager();
    }

    @Test
    void addTaskTest() {
        taskManager.addTask(task1);

        final Task savedTask = taskManager.getTask(0);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final Map<Integer, Task> tasks = taskManager.getListOfTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addSubtaskTest() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);

        final Subtask savedSubtask = taskManager.getSubtask(2);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают.");

        final Map<Integer, Subtask> subtasks = taskManager.getListOfSubtasks();
        final Map<Integer, Epic> epics = taskManager.getListOfEpics();

        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertNotNull(epics.get(1).getSubtasks(), "Подзадачи эпика не возвращаются.");

        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(1, epics.get(1).getSubtasks().size(), "Неверное количество подзадач в эпике.");

        assertEquals(subtask1, subtasks.get(2), "Подзадачи не совпадают.");
        assertEquals(subtask1, epics.get(1).getSubtasks().get(2), "Подзадачи не совпадают.");
    }

    @Test
    void addEpicTest() {
        taskManager.addEpic(epic1);
        final Epic savedEpic = taskManager.getEpic(1);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic1, savedEpic, "Эпики не совпадают.");

        final Map<Integer, Epic> epics = taskManager.getListOfEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(1), "Эпики не совпадают.");
    }

    @Test
    public void getHistoryTest() {
        final HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void deleteTaskTest() {
        taskManager.addTask(task1);
        final Map<Integer, Task> tasks = taskManager.getListOfTasks();
        int idActual = tasks.get(0).getId();
        int idExpected = 0;

        taskManager.deleteTask(idExpected);
        assertEquals(idExpected, idActual);
        assertTrue(tasks.isEmpty(), "Список задач не пустой");
    }

    @Test
    public void deleteSubtaskTest() {
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);

        final Map<Integer, Epic> epics = taskManager.getListOfEpics();
        final Map<Integer, Subtask> subtasks = taskManager.getListOfSubtasks();

        int epicId = subtasks.get(2).getEpicId();
        taskManager.deleteSubtask(2);

        assertNull(subtasks.get(2));
        assertTrue(subtasks.isEmpty(), "Список подзадач не пустой");
        assertTrue(epics.get(1).getSubtasks().isEmpty());
    }

    @Test
    public void deleteEpic() {
        final Map<Integer, Epic> epics = taskManager.getListOfEpics();
        final Map<Integer, Subtask> subtasks = taskManager.getListOfSubtasks();

        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);

        taskManager.deleteEpic(1);

        assertNull(epics.get(1));
        assertTrue(subtasks.isEmpty(), "Список подзадач не пустой");
    }

    @Test
    public void getEpicTimeTest() {

        subtask1.setStartTime(LocalDateTime.of(2023, 3, 30, 10, 0));
        subtask1.setDuration(10);
        subtask2.setStartTime(LocalDateTime.of(2023, 3, 31, 10, 0));
        subtask2.setDuration(20);

        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        // Стандартное поведение
        taskManager.getEpicTime(epic1.getId());
        assertEquals(LocalDateTime.of(2023, 3, 30, 10, 0), epic1.getStartTime());
        assertEquals(LocalDateTime.of(2023, 3, 31, 10, 0), epic1.getEndTime());
        assertEquals(30, epic1.getDuration());

        // С пустым списком задач
        epic1.getSubtasks().clear();
        taskManager.getEpicTime(epic1.getId());
        assertNull(epic1.getStartTime());
        assertNull(epic1.getEndTime());
        assertEquals(0, epic1.getDuration());

        // С неверным идентификатором задачи
        assertDoesNotThrow(() -> taskManager.getEpicTime(1));
    }

    @Test
    public void getPrioritizedTasksTest() {
        List<Task> expectedTasks = new ArrayList<>();
        LocalDateTime time1 = LocalDateTime.of(2023, 03, 30, 13, 00);
        LocalDateTime time2 = LocalDateTime.of(2023, 03, 30, 17, 00);
        LocalDateTime time3 = LocalDateTime.of(2023, 03, 30, 19, 00);
        subtask1.setDuration(2);
        subtask1.setStartTime(time2);
        subtask2.setDuration(2);
        subtask2.setStartTime(time3);
        task1.setStartTime(time1);
        task1.setDuration(2);

        expectedTasks.add(task1);
        expectedTasks.add(subtask1);
        expectedTasks.add(subtask2);

        taskManager.addEpic(epic1);
        taskManager.addTask(task1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        List<Task> actualTasks = taskManager.getPrioritizedTasks();

        assertEquals(expectedTasks.size(), actualTasks.size());
        for (int i = 0; i < expectedTasks.size(); i++) {
            assertEquals(expectedTasks.get(i), actualTasks.get(i));
        }
        taskManager.deleteAllTask();
        taskManager.deleteAllSubtasks();

        List<Task> actualNullTasks = taskManager.getPrioritizedTasks();
        assertTrue(actualNullTasks.isEmpty());
    }

    @Test
    public void HistoryManagerAddTest() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.IN_PROGRESS);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.DONE);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

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
        Task task1 = new Task(1, "Task 1", "Description 1", Status.IN_PROGRESS);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.DONE);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    public void HistoryManagerGetHistoryTest() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.IN_PROGRESS);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.DONE);
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task1);
        expectedHistory.add(task2);
        assertEquals(expectedHistory, history);
    }
}


