package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void addTaskTest() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        taskManager.addTask(task);

        assertEquals(task, taskManager.getListOfTasks().get(task.getId()), "Задачи не совпадают");
        assertTrue(taskManager.getListOfTasks().containsKey(task.getId()), "Задача не добавлена");
        assertNotNull(taskManager.getListOfTasks(), "Пустой список задач");
    }


    @Test
    void addSubtaskTest() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
//проверили список subtsaks
        assertEquals(subtask, taskManager.getListOfSubtasks().get(subtask.getId()), "Задачи не совпадают");
        assertTrue(taskManager.getListOfSubtasks().containsKey(subtask.getId()), "Задача не добавлена");
        assertNotNull(taskManager.getListOfSubtasks(), "Пустой список задач");
//проверили epicSubtasks
        assertEquals(subtask, taskManager.getListOfEpics().get(epic.getId()).getSubtasks().get(subtask.getId()), "Задачи не совпадают");
        assertTrue(taskManager.getListOfEpics().get(epic.getId()).getSubtasks().containsKey(subtask.getId()), "Задача не добавлена");
        assertNotNull(taskManager.getListOfEpics().get(epic.getId()).getSubtasks(), "Пустой список задач");
    }

    @Test
    void addEpicTest() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        taskManager.addEpic(epic);

        assertEquals(epic, taskManager.getListOfEpics().get(epic.getId()), "Задачи не совпадают");
        assertTrue(taskManager.getListOfEpics().containsKey(epic.getId()), "Задача не добавлена");
        assertNotNull(taskManager.getListOfEpics(), "Пустой список задач");
    }

    @Test
    void getListOfTasks() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        taskManager.addTask(task);
//проверили нормальную работу метода
        assertEquals(task, taskManager.getListOfTasks().get(task.getId()), "Список не вернул задачу");

//проверили работу при неверном идентификаторе
        assertTrue(taskManager.getListOfTasks().containsKey(task.getId()), "Идентификаторы не совпадают");

//проверили пустой список
        taskManager.deleteAllTask();
        assertTrue(taskManager.getListOfTasks().isEmpty(), "Список не пустой");
    }

    @Test
    void getListOfSubtasks() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
//проверили нормальную работу метода
        assertEquals(subtask, taskManager.getListOfSubtasks().get(subtask.getId()), "Список не вернул задачу");

//проверили работу при неверном идентификаторе
        assertTrue(taskManager.getListOfSubtasks().containsKey(subtask.getId()), "Идентификаторы не совпадают");

//проверили пустой список
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getListOfSubtasks().isEmpty(), "Список не пустой");
    }

    @Test
    void getListOfEpics() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        taskManager.addEpic(epic);
//проверили нормальную работу метода
        assertEquals(epic, taskManager.getListOfEpics().get(epic.getId()), "Список не вернул задачу");

//проверили работу при неверном идентификаторе
        assertTrue(taskManager.getListOfEpics().containsKey(epic.getId()), "Идентификаторы не совпадают");

//проверили пустой список
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getListOfEpics().isEmpty(), "Список не пустой");
    }

    @Test
    public void deleteAllTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        taskManager.addTask(task);

//проверили нормальную работу метода
        taskManager.deleteAllTask();
        assertNotEquals(task, taskManager.getListOfTasks().get(task.getId()), "Задача не удалена");

//проверили работу при пустом списке
        taskManager.deleteAllTask();
        assertTrue(taskManager.getListOfTasks().isEmpty(), "Список не пустой");
    }

    @Test
    public void deleteAllSubtasks() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

//проверили нормальную работу метода
        taskManager.deleteAllSubtasks();
        assertNotEquals(subtask, taskManager.getListOfSubtasks().get(subtask.getId()), "Задача не удалена");
        assertNotEquals(subtask, taskManager.getListOfEpics().get(epic.getId()).getSubtasks().get(subtask.getId()), "Задача не удалена");

//проверили работу при пустом списке
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getListOfSubtasks().isEmpty(), "Список не пустой");
        assertTrue(taskManager.getListOfEpics().get(epic.getId()).getSubtasks().isEmpty(), "Список не пустой");
    }

    @Test
    public void deleteAllEpics() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

//проверили нормальную работу метода
        taskManager.deleteAllEpics();
        assertNotEquals(epic, taskManager.getListOfEpics().get(epic.getId()), "Задача не удалена");
        assertTrue(taskManager.getListOfSubtasks().isEmpty(), "Задачи эпика не удалены");

//проверили работу при пустом списке
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getListOfEpics().isEmpty(), "Список не пустой");
    }

    @Test
    public void getTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        taskManager.addTask(task);

        Task getTask = taskManager.getTask(task.getId());
//проверили нормальную работу метода
        assertEquals(task, getTask, "Задачи не совпадают");

//проверили работу при пустом списке
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()), "Задача не удалена");

//проверили работу при неверном идентификаторе
        int invalidId = taskManager.setId();
        assertNull(taskManager.getTask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void getSubtask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

        Subtask getSubtask = taskManager.getSubtask(subtask.getId());
//проверили нормальную работу метода
        assertEquals(subtask, getSubtask, "Задачи не совпадают");

//проверили работу при пустом списке
        taskManager.deleteSubtask(subtask.getId());
        assertNull(taskManager.getSubtask(subtask.getId()), "Задача не удалена");

//проверили работу при неверном идентификаторе
        int invalidId = taskManager.setId();
        assertNull(taskManager.getSubtask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void getEpic() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        taskManager.addEpic(epic);

        Epic getEpic = taskManager.getEpic(epic.getId());
//проверили нормальную работу метода
        assertEquals(epic, getEpic, "Задачи не совпадают");

//проверили работу при пустом списке
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpic(epic.getId()), "Задача не удалена");

//проверили работу при неверном идентификаторе
        int invalidId = taskManager.setId();
        assertNull(taskManager.getEpic(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void deleteTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        taskManager.addTask(task);

//проверили нормальную работу метода
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()), "Задача не удалена");

//проверили работу при пустом списке
        assertEquals(-1, taskManager.deleteTask(task.getId()), "Задача не пустая");

//проверили работу при неверном идентификаторе
        int invalidId = taskManager.setId();
        assertNotEquals(task.getId(), taskManager.deleteTask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void deleteSubtask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

//проверили нормальную работу метода
        taskManager.deleteSubtask(subtask.getId());
        assertNull(taskManager.getSubtask(subtask.getId()), "Задача не удалена");

//проверили работу при пустом списке
        assertEquals(-1, taskManager.deleteSubtask(subtask.getId()), "Задача не пустая");

//проверили работу при неверном идентификаторе
        int invalidId = taskManager.setId();
        assertNotEquals(subtask.getId(), taskManager.deleteSubtask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void deleteEpic() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        taskManager.addEpic(epic);

//проверили нормальную работу метода
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpic(epic.getId()), "Задача не удалена");

//проверили работу при пустом списке
        assertEquals(-1, taskManager.deleteEpic(epic.getId()), "Задача не пустая");

//проверили работу при неверном идентификаторе
        int invalidId = taskManager.setId();
        assertNotEquals(epic.getId(), taskManager.deleteEpic(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void updateTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime newTime = LocalDateTime.of(2023, 3, 2, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        taskManager.addTask(task);

//проверили нормальную работу метода
        Task newTask = new Task(task.getId(), "newTask", "test", Status.NEW, newTime, 1);
        taskManager.updateTask(newTask);
        assertEquals("newTask", taskManager.getTask(task.getId()).getName(), "Задача не обновилась");

//проверили работу при пустом списке
        taskManager.deleteTask(newTask.getId());
        assertEquals(-1, taskManager.updateTask(task), "Задача не пустая");

//проверили работу при неверном идентификаторе
        Task invalidTask = new Task(taskManager.setId(), "newTask", "test", Status.NEW, newTime, 1);
        assertEquals(-1, taskManager.updateTask(invalidTask), "Неправильный идентификатор");
    }

    @Test
    public void updateSubtask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime newTime = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);

//проверили нормальную работу метода
        Subtask newSubtask = new Subtask(subtask.getId(), "newSubtask", "test", Status.NEW, newTime, 1, 0);
        taskManager.updateSubtask(newSubtask);
        assertEquals("newSubtask", taskManager.getSubtask(subtask.getId()).getName(), "Задача не обновилась");
        assertEquals("newSubtask", taskManager.getListOfEpics().get(epic.getId()).getSubtasks().get(newSubtask.getId()).getName(), "Задача внутри эпика не обновилась");

//проверили работу при пустом списке
        taskManager.deleteSubtask(newSubtask.getId());
        assertEquals(-1, taskManager.updateSubtask(subtask), "Задача не пустая");

//проверили работу при неверном идентификаторе
        Subtask invalidSubtask = new Subtask(taskManager.setId(), "newSubtask", "test", Status.NEW, newTime, 1, 0);
        assertEquals(-1, taskManager.updateTask(invalidSubtask), "Неправильный идентификатор");
    }

    @Test
    public void updateEpic() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime newTime = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        taskManager.addEpic(epic);

//проверили нормальную работу метода
        Epic newEpic = new Epic(epic.getId(), "newEpic", "test", Status.NEW, newTime, 1);
        taskManager.updateEpic(newEpic);
        assertEquals("newEpic", taskManager.getEpic(epic.getId()).getName(), "Задача не обновилась");

//проверили работу при пустом списке
        taskManager.deleteEpic(newEpic.getId());
        assertEquals(-1, taskManager.updateEpic(epic), "Задача не пустая");

//проверили работу при неверном идентификаторе
        Epic invalidEpic = new Epic(taskManager.setId(), "newEpic", "test", Status.NEW, newTime, 1);
        assertEquals(-1, taskManager.updateEpic(invalidEpic), "Неправильный идентификатор");
    }

    @Test
    public void getHistory() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time, 1);
        final HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void getEpicTimeTest() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, LocalDateTime.MAX, 0);
        Subtask subtask1 = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time1, 1, 0);
        Subtask subtask2 = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time2, 1, 0);


        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

//проверили нормальную работу метода
        taskManager.getEpicTime(epic.getId());
        assertEquals(time1, epic.getStartTime());
        assertEquals(subtask2.getEndTime(), epic.getEndTime());
        assertEquals(subtask1.getDuration() + subtask2.getDuration(), epic.getDuration());

//проверили работу при пустом списке
        epic.getSubtasks().clear();
        taskManager.getEpicTime(epic.getId());
        assertEquals(null, epic.getStartTime());
        assertEquals(null, epic.getEndTime());
        assertEquals(0, epic.getDuration());

//проверили работу при неверном идентификаторе
        assertDoesNotThrow(() -> taskManager.getEpicTime(epic.getId()));
    }

    @Test
    public void getPrioritizedTasks() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(taskManager.setId(), "epic", "test", Status.NEW, LocalDateTime.MIN, 0);
        Subtask subtask = new Subtask(taskManager.setId(), "subtask", "test", Status.NEW, time1, 1, 0);
        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time2, 1);

        List<Task> expectedTasks = new ArrayList<>();

        expectedTasks.add(subtask);
        expectedTasks.add(task);

        taskManager.addEpic(epic);
        taskManager.addTask(task);
        taskManager.addSubtask(subtask);

        List<Task> actualTasks = taskManager.getPrioritizedTasks();

//проверили нормальную работу метода
        assertEquals(expectedTasks.size(), actualTasks.size());
        for (int i = 0; i < expectedTasks.size(); i++) {
            assertEquals(expectedTasks.get(i), actualTasks.get(i));
        }

//проверили работу при пустом списке
        taskManager.deleteAllTask();
        taskManager.deleteAllSubtasks();
        List<Task> actualNullTasks = taskManager.getPrioritizedTasks();
        assertTrue(actualNullTasks.isEmpty());
    }

    @Test
    public void checkTaskOverlap() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 1, 19, 0);

        Task task = new Task(taskManager.setId(), "task", "test", Status.NEW, time1, 1);
        Task newTask = new Task(taskManager.setId(), "newTask", "test", Status.NEW, time2, 1);

        taskManager.addTask(task);
        List<Task> tasks = taskManager.getPrioritizedTasks();

        //проверили нормальную работу метода
        assertFalse(taskManager.checkTaskOverlap(newTask), "задачи пересекаются");

        //проверили работу при пустом списке
        taskManager.getListOfTasks().clear();
        assertFalse(taskManager.checkTaskOverlap(newTask), "задачи пересекаются если список пуст");
    }
}


