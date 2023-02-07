import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(taskManager.setId(), "", "", Status.NEW);
        Task task2 = new Task(taskManager.setId(), "", "", Status.NEW);
        Task task3 = new Task(taskManager.setId(), "", "", Status.NEW);
        Epic epic1 = new Epic(taskManager.setId(), "", "", Status.NEW);
        Subtask subtask1 = new Subtask(taskManager.setId(), "", "", Status.DONE, 3);
        Subtask subtask2 = new Subtask(taskManager.setId(), "", "", Status.NEW, 3);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.getSubtask(4);
        taskManager.getEpic(3);
        taskManager.getTask(2);
        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(0);

        System.out.println(taskManager.getHistory());
        taskManager.getTask(1);
        System.out.println(taskManager.getHistory());

    }
}
