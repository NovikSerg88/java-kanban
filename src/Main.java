import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryHistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(taskManager.setId(), "", "", Status.NEW);
        Task task2 = new Task(taskManager.setId(), "", "", Status.NEW);
        Epic epic1 = new Epic(taskManager.setId(), "", "", Status.NEW);
        Subtask subtask1 = new Subtask(taskManager.setId(), "", "", Status.DONE, 2);
        Subtask subtask2 = new Subtask(taskManager.setId(), "", "", Status.NEW, 2);
        Subtask subtask3 = new Subtask(taskManager.setId(), "", "", Status.NEW, 2);
        Epic epic2 = new Epic(taskManager.setId(), "", "", Status.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.addEpic(epic2);

        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        taskManager.getSubtask(4);
        taskManager.getSubtask(5);
        taskManager.getEpic(6);
        System.out.println(taskManager.getHistory());

        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        taskManager.getEpic(6);
        taskManager.getTask(0);
        taskManager.getTask(1);
        taskManager.getSubtask(5);
        taskManager.getSubtask(4);
        System.out.println(taskManager.getHistory());

        taskManager.deleteTask(0);
        System.out.println(taskManager.getHistory());

        taskManager.deleteEpic(2);
        System.out.println(taskManager.getHistory());
    }
}
