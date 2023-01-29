import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task task1 = new Task(taskManager.setId(), "Задача1", "Описание задачи1", Status.NEW);
        Task task2 = new Task(taskManager.setId(), "Задача2", "Описание задачи2", Status.IN_PROGRESS);
        Epic epic1 = new Epic(taskManager.setId(), "Эпик1", "Описание эпика1", Status.NEW);
        Epic epic2 = new Epic(taskManager.setId(), "Эпик2", "Описание эпика2", Status.NEW);
        Subtask subtask1 = new Subtask(taskManager.setId(), "Подзадача 1", "Описание подзадачи 1", Status.DONE, 2);
        Subtask subtask2 = new Subtask(taskManager.setId(), "Подзадача 2", "Описание подзадачи 2", Status.NEW, 2);
        Subtask subtask3 = new Subtask(taskManager.setId(), "Подзадача 3", "Описание подзадачи 3", Status.NEW, 3);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);

        System.out.println(taskManager.tasks);
        System.out.println(taskManager.epics);
        System.out.println(taskManager.subtasks);

        taskManager.deleteTask(0);
        taskManager.deleteEpic(3);

        System.out.println(taskManager.tasks);
        System.out.println(taskManager.epics);
        System.out.println(taskManager.subtasks);
    }
}
