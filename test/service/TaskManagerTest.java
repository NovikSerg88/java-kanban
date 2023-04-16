//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    public TaskManagerTest() {
    }

    @Test
    void addTaskTest() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time, 1);
        this.taskManager.addTask(task);
        Assertions.assertEquals(task, this.taskManager.getListOfTasks().get(task.getId()), "Задачи не совпадают");
        assertTrue(this.taskManager.getListOfTasks().containsKey(task.getId()), "Задача не добавлена");
        Assertions.assertNotNull(this.taskManager.getListOfTasks(), "Пустой список задач");
    }

    @Test
    void addSubtaskTest() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.addSubtask(subtask);
        Assertions.assertEquals(subtask, this.taskManager.getListOfSubtasks().get(subtask.getId()), "Задачи не совпадают");
        assertTrue(this.taskManager.getListOfSubtasks().containsKey(subtask.getId()), "Задача не добавлена");
        Assertions.assertNotNull(this.taskManager.getListOfSubtasks(), "Пустой список задач");
        Assertions.assertEquals(subtask, ((Epic)this.taskManager.getListOfEpics().get(epic.getId())).getSubtasks().get(subtask.getId()), "Задачи не совпадают");
        assertTrue(((Epic)this.taskManager.getListOfEpics().get(epic.getId())).getSubtasks().containsKey(subtask.getId()), "Задача не добавлена");
        Assertions.assertNotNull(((Epic)this.taskManager.getListOfEpics().get(epic.getId())).getSubtasks(), "Пустой список задач");
    }

    @Test
    void addEpicTest() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        this.taskManager.addEpic(epic);
        Assertions.assertEquals(epic, this.taskManager.getListOfEpics().get(epic.getId()), "Задачи не совпадают");
        assertTrue(this.taskManager.getListOfEpics().containsKey(epic.getId()), "Задача не добавлена");
        Assertions.assertNotNull(this.taskManager.getListOfEpics(), "Пустой список задач");
    }

    @Test
    void getListOfTasks() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time, 1);
        this.taskManager.addTask(task);
        Assertions.assertEquals(task, this.taskManager.getListOfTasks().get(task.getId()), "Список не вернул задачу");
        assertTrue(this.taskManager.getListOfTasks().containsKey(task.getId()), "Идентификаторы не совпадают");
        this.taskManager.deleteAllTask();
        assertTrue(this.taskManager.getListOfTasks().isEmpty(), "Список не пустой");
    }

    @Test
    void getListOfSubtasks() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.addSubtask(subtask);
        Assertions.assertEquals(subtask, taskManager.getListOfSubtasks().get(subtask.getId()), "Список не вернул задачу");
        assertTrue(this.taskManager.getListOfSubtasks().containsKey(subtask.getId()), "Идентификаторы не совпадают");
        this.taskManager.deleteAllSubtasks();
        assertTrue(this.taskManager.getListOfSubtasks().isEmpty(), "Список не пустой");
    }

    @Test
    void getListOfEpics() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        this.taskManager.addEpic(epic);
        Assertions.assertEquals(epic, this.taskManager.getListOfEpics().get(epic.getId()), "Список не вернул задачу");
        assertTrue(this.taskManager.getListOfEpics().containsKey(epic.getId()), "Идентификаторы не совпадают");
        this.taskManager.deleteAllEpics();
        assertTrue(this.taskManager.getListOfEpics().isEmpty(), "Список не пустой");
    }

    @Test
    public void deleteAllTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time, 1);
        this.taskManager.addTask(task);
        this.taskManager.deleteAllTask();
        assertNotEquals(task, this.taskManager.getListOfTasks().get(task.getId()), "Задача не удалена");
        this.taskManager.deleteAllTask();
        assertTrue(this.taskManager.getListOfTasks().isEmpty(), "Список не пустой");
    }

    @Test
    public void deleteAllSubtasks() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
        taskManager.deleteAllSubtasks();
        assertNotEquals(subtask, taskManager.getListOfSubtasks().get(subtask.getId()), "Задача не удалена");
        assertNotEquals(subtask, taskManager.getListOfEpics().get(epic.getId()).getSubtasks().get(subtask.getId()), "Задача не удалена");
        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getListOfSubtasks().isEmpty(), "Список не пустой");
        assertTrue(taskManager.getListOfEpics().get(epic.getId()).getSubtasks().isEmpty(), "Список не пустой");
    }

    @Test
    public void deleteAllEpics() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.addSubtask(subtask);
        this.taskManager.deleteAllEpics();
        assertNotEquals(epic, this.taskManager.getListOfEpics().get(epic.getId()), "Задача не удалена");
        assertTrue(this.taskManager.getListOfSubtasks().isEmpty(), "Задачи эпика не удалены");
        this.taskManager.deleteAllEpics();
        assertTrue(this.taskManager.getListOfEpics().isEmpty(), "Список не пустой");
    }

    @Test
    public void getTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time, 1);
        this.taskManager.addTask(task);
        Task getTask = this.taskManager.getTask(task.getId());
        Assertions.assertEquals(task, getTask, "Задачи не совпадают");
        this.taskManager.deleteTask(task.getId());
        Assertions.assertNull(this.taskManager.getTask(task.getId()), "Задача не удалена");
        int invalidId = this.taskManager.setId();
        Assertions.assertNull(this.taskManager.getTask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void getSubtask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.addSubtask(subtask);
        Subtask getSubtask = this.taskManager.getSubtask(subtask.getId());
        Assertions.assertEquals(subtask, getSubtask, "Задачи не совпадают");
        this.taskManager.deleteSubtask(subtask.getId());
        Assertions.assertNull(this.taskManager.getSubtask(subtask.getId()), "Задача не удалена");
        int invalidId = this.taskManager.setId();
        Assertions.assertNull(this.taskManager.getSubtask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void getEpic() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        this.taskManager.addEpic(epic);
        Epic getEpic = this.taskManager.getEpic(epic.getId());
        Assertions.assertEquals(epic, getEpic, "Задачи не совпадают");
        this.taskManager.deleteEpic(epic.getId());
        Assertions.assertNull(this.taskManager.getEpic(epic.getId()), "Задача не удалена");
        int invalidId = this.taskManager.setId();
        Assertions.assertNull(this.taskManager.getEpic(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void deleteTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time, 1);
        this.taskManager.addTask(task);
        this.taskManager.deleteTask(task.getId());
        Assertions.assertNull(this.taskManager.getTask(task.getId()), "Задача не удалена");
        Assertions.assertEquals(-1, this.taskManager.deleteTask(task.getId()), "Задача не пустая");
        int invalidId = this.taskManager.setId();
        assertNotEquals(task.getId(), this.taskManager.deleteTask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void deleteSubtask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.addSubtask(subtask);
        this.taskManager.deleteSubtask(subtask.getId());
        Assertions.assertNull(this.taskManager.getSubtask(subtask.getId()), "Задача не удалена");
        Assertions.assertEquals(-1, this.taskManager.deleteSubtask(subtask.getId()), "Задача не пустая");
        int invalidId = this.taskManager.setId();
        assertNotEquals(subtask.getId(), this.taskManager.deleteSubtask(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void deleteEpic() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.deleteEpic(epic.getId());
        Assertions.assertNull(this.taskManager.getEpic(epic.getId()), "Задача не удалена");
        Assertions.assertEquals(-1, this.taskManager.deleteEpic(epic.getId()), "Задача не пустая");
        int invalidId = this.taskManager.setId();
        assertNotEquals(epic.getId(), this.taskManager.deleteEpic(invalidId), "Неправильный идентификатор");
    }

    @Test
    public void updateTask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime newTime = LocalDateTime.of(2023, 3, 2, 17, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time, 1);
        this.taskManager.addTask(task);
        Task newTask = new Task(task.getId(), "newTask", "test", Status.NEW, newTime, 1);
        this.taskManager.updateTask(newTask);
        Assertions.assertEquals("newTask", this.taskManager.getTask(task.getId()).getName(), "Задача не обновилась");
        this.taskManager.deleteTask(newTask.getId());
        Assertions.assertEquals(-1, this.taskManager.updateTask(task), "Задача не пустая");
        Task invalidTask = new Task(this.taskManager.setId(), "newTask", "test", Status.NEW, newTime, 1);
        Assertions.assertEquals(-1, this.taskManager.updateTask(invalidTask), "Неправильный идентификатор");
    }

    @Test
    public void updateSubtask() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime newTime = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time, 1, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.addSubtask(subtask);
        Subtask newSubtask = new Subtask(subtask.getId(), "newSubtask", "test", Status.NEW, newTime, 1, 0);
        this.taskManager.updateSubtask(newSubtask);
        Assertions.assertEquals("newSubtask", this.taskManager.getSubtask(subtask.getId()).getName(), "Задача не обновилась");
        Assertions.assertEquals("newSubtask", ((Subtask)((Epic)this.taskManager.getListOfEpics().get(epic.getId())).getSubtasks().get(newSubtask.getId())).getName(), "Задача внутри эпика не обновилась");
        this.taskManager.deleteSubtask(newSubtask.getId());
        Assertions.assertEquals(-1, this.taskManager.updateSubtask(subtask), "Задача не пустая");
        Subtask invalidSubtask = new Subtask(this.taskManager.setId(), "newSubtask", "test", Status.NEW, newTime, 1, 0);
        Assertions.assertEquals(-1, this.taskManager.updateTask(invalidSubtask), "Неправильный идентификатор");
    }

    @Test
    public void updateEpic() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime newTime = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, time, 0);
        this.taskManager.addEpic(epic);
        Epic newEpic = new Epic(epic.getId(), "newEpic", "test", Status.NEW, newTime, 1);
        this.taskManager.updateEpic(newEpic);
        Assertions.assertEquals("newEpic", this.taskManager.getEpic(epic.getId()).getName(), "Задача не обновилась");
        this.taskManager.deleteEpic(newEpic.getId());
        Assertions.assertEquals(-1, this.taskManager.updateEpic(epic), "Задача не пустая");
        Epic invalidEpic = new Epic(this.taskManager.setId(), "newEpic", "test", Status.NEW, newTime, 1);
        Assertions.assertEquals(-1, this.taskManager.updateEpic(invalidEpic), "Неправильный идентификатор");
    }

    @Test
    public void getHistory() {
        LocalDateTime time = LocalDateTime.of(2023, 3, 1, 17, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time, 1);
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        Assertions.assertNotNull(history, "История не пустая.");
        Assertions.assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void getEpicTimeTest() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, LocalDateTime.MAX, 0);
        Subtask subtask1 = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time1, 1, 0);
        Subtask subtask2 = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time2, 1, 0);
        this.taskManager.addEpic(epic);
        this.taskManager.addSubtask(subtask1);
        this.taskManager.addSubtask(subtask2);
        this.taskManager.getEpicTime(epic.getId());
        Assertions.assertEquals(time1, epic.getStartTime());
        Assertions.assertEquals(subtask2.getEndTime(), epic.getEndTime());
        Assertions.assertEquals(subtask1.getDuration() + subtask2.getDuration(), epic.getDuration());
        epic.getSubtasks().clear();
        this.taskManager.getEpicTime(epic.getId());
        Assertions.assertEquals((Object)null, epic.getStartTime());
        Assertions.assertEquals((Object)null, epic.getEndTime());
        Assertions.assertEquals(0, epic.getDuration());
        Assertions.assertDoesNotThrow(() -> {
            this.taskManager.getEpicTime(epic.getId());
        });
    }

    @Test
    public void getPrioritizedTasks() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 2, 17, 0);
        Epic epic = new Epic(this.taskManager.setId(), "epic", "test", Status.NEW, LocalDateTime.MIN, 0);
        Subtask subtask = new Subtask(this.taskManager.setId(), "subtask", "test", Status.NEW, time1, 1, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time2, 1);
        List<Task> expectedTasks = new ArrayList();
        expectedTasks.add(subtask);
        expectedTasks.add(task);
        this.taskManager.addEpic(epic);
        this.taskManager.addTask(task);
        this.taskManager.addSubtask(subtask);
        List<Task> actualTasks = this.taskManager.getPrioritizedTasks();
        Assertions.assertEquals(expectedTasks.size(), actualTasks.size());

        for(int i = 0; i < expectedTasks.size(); ++i) {
            Assertions.assertEquals(expectedTasks.get(i), actualTasks.get(i));
        }

        this.taskManager.deleteAllTask();
        this.taskManager.deleteAllSubtasks();
        List<Task> actualNullTasks = this.taskManager.getPrioritizedTasks();
        assertTrue(actualNullTasks.isEmpty());
    }

    @Test
    public void checkTaskOverlap() {
        LocalDateTime time1 = LocalDateTime.of(2023, 3, 1, 17, 0);
        LocalDateTime time2 = LocalDateTime.of(2023, 3, 1, 19, 0);
        Task task = new Task(this.taskManager.setId(), "task", "test", Status.NEW, time1, 1);
        Task newTask = new Task(this.taskManager.setId(), "newTask", "test", Status.NEW, time2, 1);
        this.taskManager.addTask(task);
        List<Task> tasks = this.taskManager.getPrioritizedTasks();
        Assertions.assertFalse(this.taskManager.checkTaskOverlap(newTask), "задачи пересекаются");
        this.taskManager.getListOfTasks().clear();
        Assertions.assertFalse(this.taskManager.checkTaskOverlap(newTask), "задачи пересекаются если список пуст");
    }
}
